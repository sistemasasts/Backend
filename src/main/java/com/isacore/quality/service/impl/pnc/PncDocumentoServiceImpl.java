package com.isacore.quality.service.impl.pnc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.pnc.*;
import com.isacore.quality.model.se.SolicitudDocumento;
import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.repository.pnc.*;
import com.isacore.quality.service.pnc.IPncDocumentoService;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.UtilidadesCadena;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RequiredArgsConstructor
@Slf4j
@Service
public class PncDocumentoServiceImpl implements IPncDocumentoService {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private final IPncDocumentoRepo repositorio;
    private final ConfiguracionSolicitud configuracion;
    private final IProductoNoConformeRepo productoNoConformeRepo;
    private final IPncSalidaMaterialRepo salidaMaterialRepo;
    private final IPncPlanAccionRepo planAccionRepo;
    private final IPncHistorialRepo historialRepo;

    @Transactional
    @Override
    public PncDocumento registrar(ProductoNoConforme productoNoConforme, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden) {
        try {
            Long idDefecto = productoNoConforme.getDefectos().stream().filter(PncDefecto::isNuevo).findFirst().map(PncDefecto::getId).orElse(null);
            String path = this.crearPathArchivo(productoNoConforme.getNumero(), null, nombreArchivo);
            PassFileToRepository.saveLocalFile(path, bytes);
            PncDocumento documento = new PncDocumento(
                    productoNoConforme.getId(),
                    null,
                    idDefecto,
                    null,
                    path,
                    null,
                    nombreArchivo,
                    orden,
                    tipo,
                    null
            );
            this.repositorio.save(documento);
            log.info(String.format("PNC %s -> Documento guardado %s", productoNoConforme.getId(), documento));
            return documento;

        } catch (IOException e) {
            log.error(String.format("PNC %s -> Error al subir Documento %s", productoNoConforme.getId(), e.getMessage()));
            throw new ApprobationCriteriaErrorException();
        }
    }

    @Transactional
    @Override
    public PncDocumento registrar(PncSalidaMaterial salidaMaterial, Optional<PncPlanAccion> planAccion, byte[] bytes,
                                  String nombreArchivo, String tipo, PncOrdenFlujo orden) {
        try {
            String path = this.crearPathArchivo(salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId(), nombreArchivo);
            PassFileToRepository.saveLocalFile(path, bytes);
            PncDocumento documento = new PncDocumento(
                    salidaMaterial.getProductoNoConforme().getId(),
                    salidaMaterial.getId(),
                    null,
                    planAccion.map(PncPlanAccion::getId).orElse(null),
                    path,
                    salidaMaterial.getEstado(),
                    nombreArchivo,
                    orden,
                    tipo,
                    planAccion.map(PncPlanAccion::getEstado).orElse(null)
            );
            this.repositorio.save(documento);
            log.info(String.format("PNC %s : Salida Material -> Documento guardado %s", salidaMaterial.getProductoNoConforme().getNumero(), documento));
            return documento;

        } catch (IOException e) {
            log.error(String.format("PNC %s : Salida Material -> Error al subir Documento %s", salidaMaterial.getProductoNoConforme().getNumero(), e.getMessage()));
            throw new ApprobationCriteriaErrorException();
        }
    }

    @Transactional
    @Override
    public PncDocumento subirArchivo(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            PncDocumentoDto dto = JSON_MAPPER.readValue(jsonDTO, PncDocumentoDto.class);
            if (dto != null) {
                Optional<PncSalidaMaterial> salidaOp = salidaMaterialRepo.findById(dto.getSalidaMaterialId());
                if (!salidaOp.isPresent())
                    throw new SolicitudEnsayoErrorException(String.format("PNC con id= %s -> salida de material %s no existe. no es posible subir archivo",
                            dto.getSalidaMaterialId(), dto.getSalidaMaterialId()));
                Optional<PncPlanAccion> planAccionOP = this.planAccionRepo.findById(dto.getPlanAccionId() == null ? 0 : dto.getPlanAccionId());
                return this.registrar(salidaOp.get(), planAccionOP, file, nombreArchivo, tipo, dto.getOrden());
            }
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        }
    }

    @Transactional
    @Override
    public PncDocumento subirArchivoPnc(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            PncDocumentoDto dto = JSON_MAPPER.readValue(jsonDTO, PncDocumentoDto.class);
            if (dto != null) {
                Optional<ProductoNoConforme> pnc = productoNoConformeRepo.findById(dto.getProductoNoConformeId());
                if (!pnc.isPresent())
                    throw new SolicitudEnsayoErrorException(String.format("PNC con id= %s no existe. no es posible subir archivo",
                            dto.getProductoNoConformeId()));
                return this.registrar(pnc.get(), file, nombreArchivo, tipo, null);
            }
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncDocumento> buscarPorEstadoYOrdenYSalidaId(EstadoSalidaMaterial estado, PncOrdenFlujo orden, long salidaId) {
        List<PncDocumento> documentos = new ArrayList<>();
        if (!orden.equals(PncOrdenFlujo.INGRESO_SALIDA_MATERIAL)) {
            List<PncDocumento> documentosOrdenCreado = this.repositorio.findByEstadoInAndOrdenFlujoAndSalidaMaterialId(Arrays.asList(EstadoSalidaMaterial.CREADO, estado),
                    PncOrdenFlujo.INGRESO_SALIDA_MATERIAL, salidaId);
            documentos.addAll(documentosOrdenCreado);
        }
        List<PncDocumento> documentosActuales = this.repositorio.findByEstadoInAndOrdenFlujoAndSalidaMaterialId(Arrays.asList(EstadoSalidaMaterial.CREADO, estado),
                orden, salidaId);
        documentos.addAll(documentosActuales);
        return documentos;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncDocumento> buscarPorOrdenYSalidaIdYPlanAccionId(PncOrdenFlujo orden, long salidaId, long planAccionId) {
        List<PncDocumento> documentos = new ArrayList<>();
        List<PncDocumento> documentosOrdenCreado = this.repositorio.findByEstadoInAndOrdenFlujoAndSalidaMaterialId(Arrays.asList(EstadoSalidaMaterial.CREADO),
                PncOrdenFlujo.INGRESO_SALIDA_MATERIAL, salidaId);
        documentos.addAll(documentosOrdenCreado);

        List<PncDocumento> documentosPlanes = this.repositorio.findByOrdenFlujoAndSalidaMaterialIdAndPncPlanAccionId(orden, salidaId, planAccionId);
        documentos.addAll(documentosPlanes);
        return documentos;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PncDocumento> buscarPorPncId(long pncId) {
        List<PncDocumento> documentos = this.repositorio.findByProductoNoConformeId(pncId)
                .stream()
                .filter(x -> x.getPncDefectoId() == null)
                .filter(x -> x.getPncPlanAccionId() == null)
                .filter(x -> x.getSalidaMaterialId() == null)
                .collect(Collectors.toList());
        return documentos;
    }

    @Override
    public boolean eliminarDocumento(long documentoId) {
        PncDocumento documento = this.repositorio.findById(documentoId).orElse(null);
        if (documento != null) {
            if (UtilidadesCadena.noEsNuloNiBlanco(documento.getPath()))
                PassFileToRepository.eliminarArchivoFisico(documento.getPath());
            log.info(String.format("Documento PNC eliminado %s", documento));
            this.repositorio.deleteById(documentoId);
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public PncDocumento listarDocumentoPorId(long id) {
        PncDocumento documento = this.obtenerPorId(id);
        try {
            if (UtilidadesCadena.noEsNuloNiBlanco(documento.getPath())) {
                byte[] archivo = PassFileToRepository.readLocalFile(documento.getPath());
                if (archivo.length > 0) {
                    documento.setBase64(Base64.getEncoder().encodeToString(archivo));
                    return documento;
                }
                throw new PncErrorException(String.format("Archivo %s no encontrado", documento.getNombreArchivo()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] descargar(long id) {
        try {
            Optional<PncDocumento> documentoOP = repositorio.findById(id);
            if (!documentoOP.isPresent())
                throw new PncErrorException("Documento no encontrado");
            return PassFileToRepository.readLocalFile(documentoOP.get().getPath());

        } catch (IOException e) {
            throw new PncErrorException("Problemas al construir el documento.");
        }
    }

    @Override
    public byte[] descargarPorHistorialId(long historialId) {
        Optional<PncHistorial> historialOP = historialRepo.findById(historialId);
        if (historialOP.isPresent()) {
            PncHistorial historial = historialOP.get();

            List<PncDocumento> archivos = new ArrayList<>();
            archivos = this.repositorio.findByOrdenFlujoAndSalidaMaterialId(
                    historial.getOrden(), historial.getSalidaMaterialId());

            try {
                String rutaComprimido = crearRutaComprimido(historial.getSalidaMaterialId());
                compressZip(archivos, rutaComprimido);
                return PassFileToRepository.readLocalFile(rutaComprimido);

            } catch (Exception e) {
                log.error(String.format("Error al comprimir archivo de PNC %s: %s", historial.getPncId(), e.getMessage()));
                throw new PncErrorException("Error al momento de descargar los archivos.");
            }
        }
        return null;
    }

    private void compressZip(List<PncDocumento> archivos, String ruta) throws Exception {
        // crea un buffer temporal para ir poniendo los archivos a comprimir
        ZipOutputStream zous = new ZipOutputStream(new FileOutputStream(ruta));
        for (PncDocumento archivo : archivos) {

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

    private PncDocumento obtenerPorId(long id) {
        Optional<PncDocumento> documento = this.repositorio.findById(id);
        if (!documento.isPresent())
            throw new PncErrorException("Archivo no encontrado");
        return documento.get();
    }

    private String crearPathArchivo(long pncSecuencial, Long pncSalidaMaterial, String nombreArchivo) {

        String path = crearRutaAlmacenamiento(pncSecuencial, pncSalidaMaterial).concat(File.separator).concat(nombreArchivo);
        if (PassFileToRepository.fileExists(path))
            path = crearRutaAlmacenamiento(pncSecuencial, pncSalidaMaterial).concat(File.separator).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);
        log.info(String.format("Ruta creada %s para guardar archivo %s", path, nombreArchivo));
        return path;
    }

    private String crearRutaAlmacenamiento(long pncSecuencial, Long pncSalidaMaterial) {
        try {
            String carpeta = configuracion.getRutaBase().concat(File.separator).concat("PNC").concat(File.separator).concat(String.valueOf(pncSecuencial));
            if (pncSalidaMaterial != null)
                carpeta = carpeta.concat(File.separator).concat("SALIDA_MATERIAL").concat(File.separator).concat(String.valueOf(pncSalidaMaterial));
            Path path = Paths.get(carpeta);
            if (!Files.exists(path))
                Files.createDirectories(path);
            return carpeta;
        } catch (IOException e) {
            log.error(String.format("Error al subir Documento %s", e.getMessage()));
            throw new PncErrorException("Error al crear el directorio");
        }
    }

    private String crearRutaComprimido(long salidaMaterialId) {
        PncSalidaMaterial salidaMaterial = this.salidaMaterialRepo.findById(salidaMaterialId).orElse(null);
        if (salidaMaterial == null)
            throw new PncErrorException("Salida de material no encontrado");
        String ruta = this.crearRutaAlmacenamiento(salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getId());
        return ruta.concat(File.separator).concat(salidaMaterial.getId() + ".zip");
    }


}
