package com.isacore.quality.service.impl.spp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.SolicitudBase;
import com.isacore.quality.model.se.SolicitudDocumentoDTO;
import com.isacore.quality.model.spp.*;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoDocumentoRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoHistorialRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebasProcesoRepo;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.quality.service.spp.ISolicitudPruebaProcesoDocumentoService;
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
public class SolicitudPruebaProcesoDocumentoServiceImpl implements ISolicitudPruebaProcesoDocumentoService {

    private static final Log LOG = LogFactory.getLog(SolicitudPruebaProcesoDocumentoServiceImpl.class);

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Autowired
    private ISolicitudPruebaProcesoDocumentoRepo repo;

    @Autowired
    private ISolicitudPruebasProcesoRepo repoSolicitudPruebasProceso;

    @Autowired
    private ConfiguracionSolicitud configuracion;

    @Autowired
    private ISolicitudPruebaProcesoHistorialRepo repoHistorial;

    @Override
    public SolicitudPruebaProcesoDocumento subir(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {

            SolicitudDocumentoDTO dto = JSON_MAPPER.readValue(jsonDTO, SolicitudDocumentoDTO.class);
            if (dto != null) {
                Optional<SolicitudPruebasProceso> solicitudOp = repoSolicitudPruebasProceso.findById(dto.getIdSolicitud());

                if (!solicitudOp.isPresent())
                    throw new SolicitudEnsayoErrorException(String.format("Solicitud con id= %s no existe. no es posible subir archivo",
                            dto.getIdSolicitud()));

                SolicitudPruebasProceso solicitud = solicitudOp.get();
                try {
                    final String path = crearPathArchivo(solicitud, nombreArchivo);
                    PassFileToRepository.saveLocalFile(path, file);

                    SolicitudPruebaProcesoDocumento documento = guardarDocumentoSPP(solicitud, nombreArchivo, path, dto.getOrdenPP(), tipo);

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

    private SolicitudPruebaProcesoDocumento guardarDocumentoSPP(SolicitudPruebasProceso solicitud, String nombreArchivo, String ruta,
                                                                OrdenFlujoPP orden, String tipo) {
        SolicitudPruebaProcesoDocumento nuevo = new SolicitudPruebaProcesoDocumento(
                solicitud,
                ruta,
                nombreArchivo,
                orden,
                tipo);

        LOG.info(String.format("Documento a guardar %s", nuevo));
        return repo.save(nuevo);
    }

    @Override
    public byte[] descargar(long id) {
        try {

            Optional<SolicitudPruebaProcesoDocumento> documentoOP = repo.findById(id);
            if (!documentoOP.isPresent())
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
            path = crearRutaAlmacenamiento(solicitudBase).concat(File.separator).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);

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
    public List<SolicitudPruebaProcesoDocumento> buscarPorEstadoYOrdenYSolicitudId(EstadoSolicitudPP estado, OrdenFlujoPP orden, long solicitudId) {
        return repo.findByEstadoInAndOrdenFlujoInAndSolicitudPruebasProceso_Id(Arrays.asList(EstadoSolicitudPP.NUEVO, estado),
                Arrays.asList(OrdenFlujoPP.INGRESO_SOLICITUD, orden), solicitudId);
    }

    @Override
    public byte[] descargarPorHistorialId(long historialId) {
        Optional<SolicitudPruebaProcesoHistorial> historialOP = repoHistorial.findById(historialId);
        if (historialOP.isPresent()) {
            SolicitudPruebaProcesoHistorial historial = historialOP.get();

            List<SolicitudPruebaProcesoDocumento> archivos = new ArrayList<>();
            archivos = repo.findByEstadoInAndOrdenFlujoInAndSolicitudPruebasProceso_Id(Arrays.asList(historial.getEstadoSolicitud()),
                    Arrays.asList(historial.getOrden()), historial.getSolicitudPruebasProceso().getId());

            try {
                String rutaComprimido = crearRutaComprimido(historial.getSolicitudPruebasProceso().getCodigo());
                compressZip(archivos, rutaComprimido);
                return PassFileToRepository.readLocalFile(rutaComprimido);

            } catch (Exception e) {
                LOG.error(String.format("Error al comprimir archivo de la solicitud %s: %s", historial.getSolicitudPruebasProceso().getId(),
                        e.getMessage()));
                throw new SolicitudEnsayoErrorException("Error al momento de descargar los archivos.");
            }
        }
        return null;
    }

    @Transactional
    @Override
    public VerImagenDTO subirImg1(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            SolicitudPruebaProcesoDocumento documento = this.subir(jsonDTO, file, nombreArchivo, tipo);
            SolicitudDocumentoDTO dto = JSON_MAPPER.readValue(jsonDTO, SolicitudDocumentoDTO.class);
            SolicitudPruebasProceso solicitud = this.repoSolicitudPruebasProceso.findById(dto.getIdSolicitud()).orElse(null);
            if (solicitud != null) {
                solicitud.guardarDatosImagen1(documento.getId(), documento.getPath());
            }

            return new VerImagenDTO(file, documento);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        }
    }

    @Override
    public VerImagenDTO verImg(long documentoId) {
        try{
            SolicitudPruebaProcesoDocumento documento = this.repo.findById(documentoId).orElse(null);
            if (documento != null){
                byte[] file = PassFileToRepository.readLocalFile(documento.getPath());
                return new VerImagenDTO(file,documento);
            }
        } catch (IOException e) {
            throw new SolicitudEnsayoErrorException("Problemas al construir el documento.");
        }
        return null;
    }

    private String crearRutaComprimido(String codigoSolicitud) {
        return configuracion.getRutaBase().concat(File.separator).concat(codigoSolicitud).concat(File.separator).concat(codigoSolicitud + ".zip");
    }

    private void compressZip(List<SolicitudPruebaProcesoDocumento> archivos, String ruta) throws Exception {
        // crea un buffer temporal para ir poniendo los archivos a comprimir
        ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(ruta));
        for (SolicitudPruebaProcesoDocumento archivo : archivos) {

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
}
