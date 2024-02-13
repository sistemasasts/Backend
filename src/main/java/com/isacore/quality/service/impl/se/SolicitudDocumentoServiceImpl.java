package com.isacore.quality.service.impl.se;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.*;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoDocumento;
import com.isacore.quality.model.spp.VerImagenDTO;
import com.isacore.quality.repository.se.ISolicitudDocumentoRepo;
import com.isacore.quality.repository.se.ISolicitudEnsayoAdjuntoRequeridoRepo;
import com.isacore.quality.repository.se.ISolicitudEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.quality.service.se.ISolicitudDocumentoService;
import com.isacore.util.PassFileToRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class SolicitudDocumentoServiceImpl implements ISolicitudDocumentoService {

    private static final Log LOG = LogFactory.getLog(SolicitudDocumentoServiceImpl.class);

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Autowired
    private ISolicitudDocumentoRepo repo;

    @Autowired
    private ISolicitudEnsayoRepo repoSolicitud;

    @Autowired
    private ConfiguracionSolicitud configuracion;

    @Autowired
    private ISolicitudHistorialRepo repoHistorial;

    @Autowired
    private ISolicitudEnsayoAdjuntoRequeridoRepo solicitudEnsayoAdjuntoRequeridoRepo;


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

    @Transactional
    @Override
    public SolicitudDocumento subir(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            SolicitudDocumentoDTO dto = JSON_MAPPER.readValue(jsonDTO, SolicitudDocumentoDTO.class);
            if (dto != null) {
                Optional<SolicitudEnsayo> solicitudOp = repoSolicitud.findById(dto.getIdSolicitud());

                if (!solicitudOp.isPresent())
                    throw new SolicitudEnsayoErrorException(String.format("Solicitud con id= %s no existe. no es posible subir archivo", dto.getIdSolicitud()));

					SolicitudEnsayo solicitud = solicitudOp.get();
					try {
                        String nombreArchivoFinal = obtenerNombreArchivo(solicitudOp.get(), nombreArchivo, dto.getAdjuntoRequeridoId());
						final String path = crearPathArchivo(solicitud, nombreArchivoFinal);
						PassFileToRepository.saveLocalFile(path, file);

					File archivoGuradado = new File(path);
                    SolicitudDocumento documento = guardarDocumento(solicitud, archivoGuradado.getName(), path, dto.getOrden());
                    if (dto.getAdjuntoRequeridoId() > 0)
                        solicitud.marcarAdjuntoRequeridoComoCargado(documento.getId(), dto.getAdjuntoRequeridoId());

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

    @Override
    public byte[] descargar(long id) {
        try {

            Optional<SolicitudDocumento> documentoOP = repo.findById(id);
            if (!documentoOP.isPresent())
                throw new SolicitudEnsayoErrorException("Documento no encontrado");
            return PassFileToRepository.readLocalFile(documentoOP.get().getPath());

        } catch (IOException e) {
            throw new SolicitudEnsayoErrorException("Problemas al construir el documento.");
        }

    }

    @Transactional
    @Override
    public boolean eliminar(long id) {
        repo.deleteById(id);
        Optional<SolicitudEnsayoAdjuntoRequerido> solicitudEnsayoAdjuntoRequerido = this.solicitudEnsayoAdjuntoRequeridoRepo.findByDocumentoId(id);
        solicitudEnsayoAdjuntoRequerido.ifPresent(ensayoAdjuntoRequerido -> ensayoAdjuntoRequerido.setDocumentoId(null));
        return true;
    }

	private String crearPathArchivo(SolicitudBase solicitudBase, String nombreArchivo) {

		String path = crearRutaAlmacenamiento(solicitudBase).concat(File.separator).concat(nombreArchivo);

        if (PassFileToRepository.fileExists(path)){
            nombreArchivo = PassFileToRepository.generateDateAsId().concat("_").concat(nombreArchivo);
            path = crearRutaAlmacenamiento(solicitudBase).concat(File.separator).concat(nombreArchivo);
        }

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
        List<OrdenFlujo> ordenListado = new ArrayList<>(Arrays.asList(OrdenFlujo.INGRESO_SOLICITUD, orden));
        List<EstadoSolicitud> estadoListado = new ArrayList<>(Arrays.asList(EstadoSolicitud.NUEVO, estado));
        if (orden.equals(OrdenFlujo.REVISION_INFORME)) {
            ordenListado.add(OrdenFlujo.RESPONDER_SOLICITUD);
        }
        return repo.findByOrdenFlujoInAndSolicitudEnsayo_Id(ordenListado, solicitudId);
    }

    @Override
    public byte[] descargarPorHistorialId(long historialId) {
        Optional<SolicitudHistorial> historialOP = repoHistorial.findById(historialId);
        if (historialOP.isPresent()) {
            SolicitudHistorial historial = historialOP.get();

            List<SolicitudDocumento> archivos = new ArrayList<>();
            archivos = repo.findByEstadoInAndOrdenFlujoInAndSolicitudEnsayo_Id(Arrays.asList(historial.getEstadoSolicitud()), Arrays.asList(historial.getOrden()), historial.getSolicitudEnsayo().getId());

            try {
                String rutaComprimido = crearRutaComprimido(historial.getSolicitudEnsayo().getCodigo());
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
        return configuracion.getRutaBase().concat(File.separator).concat(codigoSolicitud).concat(File.separator).concat(codigoSolicitud + ".zip");
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
        if (!existeAdjunto)
            throw new SolicitudEnsayoErrorException("Falta adjuntar el informe respectivo.");

    }

    @Transactional
    @Override
    public VerImagenDTO subirImg1(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            SolicitudDocumento documento = this.subir(jsonDTO, file, nombreArchivo, tipo);
            SolicitudDocumentoDTO dto = JSON_MAPPER.readValue(jsonDTO, SolicitudDocumentoDTO.class);
            SolicitudEnsayo solicitud = this.repoSolicitud.findById(dto.getIdSolicitud()).orElse(null);
            if (solicitud != null) {
                solicitud.guardarDatosMuestraImagen(documento.getId(), documento.getPath());
            }

            return new VerImagenDTO(file, documento);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        }
    }

    @Override
    public VerImagenDTO verImg(long documentoId) {
        try {
            SolicitudDocumento documento = this.repo.findById(documentoId).orElse(null);
            if (documento != null) {
                byte[] file = PassFileToRepository.readLocalFile(documento.getPath());
                return new VerImagenDTO(file, documento);
            }
        } catch (IOException e) {
            throw new SolicitudEnsayoErrorException("Problemas al construir el documento.");
        }
        return null;
    }

    private String obtenerNombreArchivo(SolicitudEnsayo solicitudPago, String nombreArchivoOriginal, long adjuntoRequeridoId) {
        if (adjuntoRequeridoId > 0) {
            Optional<SolicitudEnsayoAdjuntoRequerido> adjunto = solicitudPago.getAdjuntosRequeridos().stream().filter(a -> a.getId() == adjuntoRequeridoId).findFirst();
            String[] extension = nombreArchivoOriginal.split("\\.");
            return adjunto.isPresent() ? String.format("%s.%s", adjunto.get().getNombre(), extension[extension.length - 1]) : nombreArchivoOriginal;
        }
        return nombreArchivoOriginal;

    }

}
