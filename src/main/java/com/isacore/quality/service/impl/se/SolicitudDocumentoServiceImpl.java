package com.isacore.quality.service.impl.se;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudBase;
import com.isacore.quality.model.se.SolicitudDocumento;
import com.isacore.quality.model.se.SolicitudDocumentoDTO;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.model.se.SolicitudPruebasProceso;
import com.isacore.quality.repository.se.ISolicitudDocumentoRepo;
import com.isacore.quality.repository.se.ISolicitudEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.repository.se.ISolicitudPruebasProcesoRepo;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.quality.service.se.ISolicitudDocumentoService;
import com.isacore.util.PassFileToRepository;

@Service
public class SolicitudDocumentoServiceImpl implements ISolicitudDocumentoService {

	private static final Log LOG = LogFactory.getLog(SolicitudDocumentoServiceImpl.class);
	
	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	
	@Autowired
	private ISolicitudDocumentoRepo repo;
	
	@Autowired
	private ISolicitudEnsayoRepo repoSolicitud;
	
	@Autowired
	private ISolicitudPruebasProcesoRepo repoSolicitudPruebasProceso;
	
	@Autowired
	private ConfiguracionSolicitud configuracion;
	
	@Autowired
	private ISolicitudHistorialRepo repoHistorial;
	
	
	@Override
	public List<SolicitudDocumento> findAll() {
		return repo.findAll();
	}

	@Override
	public SolicitudDocumento create(SolicitudDocumento obj) {
		SolicitudDocumento nuevo = new SolicitudDocumento(
				obj.getSolicitudEnsayo(), 
				obj.getPath(), 
				obj.getNombreArchivo(), 
				obj.getOrdenFlujo());
		
		LOG.info(String.format("Documento a guardar %s", nuevo));
		return repo.save(nuevo);
	}

	@Override
	public SolicitudDocumento findById(SolicitudDocumento id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SolicitudDocumento update(SolicitudDocumento obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return false;
	}

	@Override
	public SolicitudDocumento subir(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
		try {
			
			SolicitudDocumentoDTO dto = JSON_MAPPER.readValue(jsonDTO, SolicitudDocumentoDTO.class);
			if(dto != null) {
				Optional<SolicitudEnsayo> solicitudOp = repoSolicitud.findById(dto.getIdSolicitud());
				
				if(!solicitudOp.isPresent())
					throw new SolicitudEnsayoErrorException(String.format("Solicitud con id= %s no existe. no es posible subir archivo", dto.getIdSolicitud()));
					
					SolicitudEnsayo solicitud = solicitudOp.get();
					try {
						final String path = crearPathArchivo(solicitud, nombreArchivo);
						PassFileToRepository.saveLocalFile(path, file);
						
						SolicitudDocumento documento = guardarDocumento(solicitud, nombreArchivo, path, dto.getOrden());	
						
						LOG.info(String.format("Documento guardado %s", documento));
						
						return documento;
						
					} catch (IOException e) {
						LOG.error(String.format("Error al subir Documento %s", e.getMessage()));
						throw new ApprobationCriteriaErrorException();
					}
			}
			return null;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new ApprobationCriteriaErrorException();
		}
	}
	
	@Override
	public SolicitudDocumento subirParaSolicitudPruebasProceso(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
		try {
			
			SolicitudDocumentoDTO dto = JSON_MAPPER.readValue(jsonDTO, SolicitudDocumentoDTO.class);
			if(dto != null) {
				Optional<SolicitudPruebasProceso> solicitudOp = repoSolicitudPruebasProceso.findById(dto.getIdSolicitud());
				
				if(!solicitudOp.isPresent())
					throw new SolicitudEnsayoErrorException(String.format("Solicitud con id= %s no existe. no es posible subir archivo", dto.getIdSolicitud()));
					
					SolicitudPruebasProceso solicitud = solicitudOp.get();
					try {
						final String path = crearPathArchivo(solicitud, nombreArchivo);
						PassFileToRepository.saveLocalFile(path, file);
						
						SolicitudDocumento documento = guardarDocumentoSPP(solicitud, nombreArchivo, path, dto.getOrden());	
						
						LOG.info(String.format("Documento guardado %s", documento));
						
						return documento;
						
					} catch (IOException e) {
						LOG.error(String.format("Error al subir Documento %s", e.getMessage()));
						throw new ApprobationCriteriaErrorException();
					}
			}
			return null;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new ApprobationCriteriaErrorException();
		}
	}
	
	private SolicitudDocumento guardarDocumento(SolicitudEnsayo solicitud, String nombreArchivo, String ruta, OrdenFlujo orden) {
		SolicitudDocumento nuevo = new SolicitudDocumento(
				solicitud, 
				ruta, 
				nombreArchivo, 
				orden);
		
		LOG.info(String.format("Documento a guardar %s", nuevo));
		return repo.save(nuevo);
	}
	
	private SolicitudDocumento guardarDocumentoSPP(SolicitudPruebasProceso solicitud, String nombreArchivo, String ruta, OrdenFlujo orden) {
		SolicitudDocumento nuevo = new SolicitudDocumento(
				solicitud, 
				ruta, 
				nombreArchivo, 
				orden);
		
		LOG.info(String.format("Documento a guardar %s", nuevo));
		return repo.save(nuevo);
	}

	@Override
	public byte[] descargar(long id) {
		try {
			
			Optional<SolicitudDocumento> documentoOP = repo.findById(id);
			if(!documentoOP.isPresent())
				throw new SolicitudEnsayoErrorException("Documento no encontrado");
			return PassFileToRepository.readLocalFile(documentoOP.get().getPath());
			
		} catch (IOException e) {
			throw new SolicitudEnsayoErrorException("Problemas al construir el documento.");
		}
		
	}

	@Override
	public boolean eliminar(long id) {
		repo.deleteById(id);
		return true;
	}
	
	private String crearPathArchivo(SolicitudBase solicitudBase, String nombreArchivo) {
		
		
		String path = crearRutaAlmacenamiento(solicitudBase).concat(File.separator).concat(nombreArchivo);

		if (PassFileToRepository.fileExists(path))
			path = crearRutaAlmacenamiento(solicitudBase).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);

		LOG.info(String.format("Ruta creada %s para guardar archivo %s", path, nombreArchivo));
		
		return path;
	}
	
	private String crearRutaAlmacenamiento(SolicitudBase solicitudBase) {

		try {
			String carpeta = configuracion.getRutaBase().concat(File.separator).concat(solicitudBase.getCodigo());
			Path path = Paths.get(carpeta);
			if (!Files.exists(path))
				Files.createDirectory(path);
			
			return carpeta;
		} catch (IOException e) {
			LOG.error(String.format("Error al subir Documento %s", e.getMessage()));
			throw new SolicitudEnsayoErrorException("Error al crear el directorio");
		}
	}

	@Override
	public List<SolicitudDocumento> buscarPorEstadoYOrdenYSolicitudId(EstadoSolicitud estado, OrdenFlujo orden, long solicitudId) {
			return repo.findByEstadoInAndOrdenFlujoInAndSolicitudEnsayo_Id(Arrays.asList(EstadoSolicitud.NUEVO, estado),Arrays.asList(OrdenFlujo.INGRESO_SOLICITUD, orden), solicitudId);
	}

	@Override
	public byte[] descargarPorHistorialId(long historialId) {
		Optional<SolicitudHistorial> historialOP = repoHistorial.findById(historialId);
		if (historialOP.isPresent()) {
			SolicitudHistorial historial = historialOP.get();

			List<SolicitudDocumento> archivos = new ArrayList<>();
			if(historial.getSolicitudEnsayo() != null)
				archivos = repo.findByEstadoInAndOrdenFlujoInAndSolicitudEnsayo_Id(Arrays.asList(historial.getEstadoSolicitud()),Arrays.asList(historial.getOrden()), historial.getSolicitudEnsayo().getId());
			else if(historial.getSolicitudPruebasProceso() != null) 
				archivos = repo.findByEstadoInAndOrdenFlujoInAndSolicitudPruebasProceso_Id(Arrays.asList(historial.getEstadoSolicitud()),Arrays.asList(historial.getOrden()), historial.getSolicitudPruebasProceso().getId());
			
			try {
				String rutaComprimido = crearRutaComprimido(historial.getSolicitudEnsayo() != null? historial.getSolicitudEnsayo().getCodigo(): historial.getSolicitudPruebasProceso().getCodigo());
				compressZip(archivos, rutaComprimido);
				return PassFileToRepository.readLocalFile(rutaComprimido);
				
			} catch (Exception e) {
				LOG.error(String.format("Error al comprimir archivo de la solicitud %s: %s", historial.getSolicitudEnsayo().getId(), e.getMessage()));
				throw new SolicitudEnsayoErrorException("Error al momento de descargar los archivos.");
			}
		}
		return null;
	}
	
	private String crearRutaComprimido(String codigoSolicitud) {
		return configuracion.getRutaBase().concat(File.separator).concat(codigoSolicitud).concat(File.separator).concat(codigoSolicitud+".zip");
	}
	
	private void compressZip(List<SolicitudDocumento> archivos, String ruta) throws Exception {
		// crea un buffer temporal para ir poniendo los archivos a comprimir
		ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(ruta));
		for (SolicitudDocumento archivo : archivos) {			
			
			//nombre con el que se va guardar el archivo dentro del zip
			ZipEntry entrada = new ZipEntry(archivo.getNombreArchivo());
			zous.putNextEntry(entrada);
			
				//System.out.println("Nombre del Archivo: " + entrada.getName());
				System.out.println("Comprimiendo.....");
				//obtiene el archivo para irlo comprimiendo
				FileInputStream fis = new FileInputStream(archivo.getPath());
				int leer;
				byte[] buffer = new byte[1024];
				while (0 < (leer = fis.read(buffer))) {
					zous.write(buffer, 0, leer);
				}
				fis.close();
				zous.closeEntry();
		}
		zous.close();		
    }

	@Override
	public void validarInformeSubido(long solicitudId, EstadoSolicitud estado) {
		boolean existeAdjunto = repo.existsByEstadoAndOrdenFlujoAndSolicitudEnsayo_Id(estado, OrdenFlujo.RESPONDER_SOLICITUD, solicitudId);
		if(!existeAdjunto)
			throw new SolicitudEnsayoErrorException("Falta adjuntar el informe respectivo.");
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<SolicitudDocumento> buscarPorEstadoYOrdenYSolicitudPruebaProcesoId(EstadoSolicitud estado,
			OrdenFlujo orden, long solicitudId) {
		return repo.findByEstadoInAndOrdenFlujoInAndSolicitudPruebasProceso_Id(Arrays.asList(EstadoSolicitud.NUEVO, estado),Arrays.asList(OrdenFlujo.INGRESO_SOLICITUD, orden), solicitudId);
	}
}
