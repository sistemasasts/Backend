package com.isacore.quality.service.impl.desviacionRequisito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.DocumentoDto;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoDocumento;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoHistorial;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoOrdenFlujo;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoDocumentoRepo;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoHistorialRepo;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoRepo;
import com.isacore.quality.service.desviacionRequisito.IDesviacionRequisitoDocumentoService;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.UtilidadesCadena;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesArchivo.compressZip;

@RequiredArgsConstructor
@Slf4j
@Service
public class DesviacionRequisitoDocumentoServiceImpl implements IDesviacionRequisitoDocumentoService {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private final IDesviacionRequisitoDocumentoRepo repositorio;
    private final IDesviacionRequisitoRepo desviacionRequisitoRepo;
    private final ConfiguracionSolicitud configuracion;
    private final IDesviacionRequisitoHistorialRepo historialRepo;


    @Transactional
    @Override
    public DesviacionRequisitoDocumento subirArchivo(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            DocumentoDto dto = JSON_MAPPER.readValue(jsonDTO, DocumentoDto.class);
            if (dto != null) {
                DesviacionRequisito salidaOp = buscarDesviacionPorId(dto.getSolicitudId());
                String path = this.crearPathArchivo(salidaOp.getSecuencial(), nombreArchivo);
                PassFileToRepository.saveLocalFile(path, file);

                DesviacionRequisitoDocumento documento = new DesviacionRequisitoDocumento(
                        path,
                        nombreArchivo,
                        salidaOp,
                        DesviacionRequisitoOrdenFlujo.valueOf(dto.getOrden())
                );
                this.repositorio.save(documento);
                log.info(String.format("DESVIACION REQUISITO %s : Documento guardado %s", salidaOp.getSecuencial(), documento));
                return documento;
            }
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        } catch (IOException e) {
            throw new PncErrorException("Error al cargar el documento");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<DesviacionRequisitoDocumento> buscarPorEstadoYOrdenYSalidaId(DesviacionRequisitoOrdenFlujo orden, long salidaId) {
        return this.repositorio.findByOrdenAndDesviacionRequisito_Id(orden, salidaId);
    }

    @Override
    public boolean eliminarDocumento(long documentoId) {
        DesviacionRequisitoDocumento documento = this.repositorio.findById(documentoId).orElse(null);
        if (documento != null) {
            if (UtilidadesCadena.noEsNuloNiBlanco(documento.getPath()))
                PassFileToRepository.eliminarArchivoFisico(documento.getPath());
            log.info(String.format("Documento Desviacion Requisito eliminado %s", documento));
            this.repositorio.deleteById(documentoId);
        }
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] descargar(long id) {
        try {
            Optional<DesviacionRequisitoDocumento> documentoOP = repositorio.findById(id);
            if (!documentoOP.isPresent())
                throw new PncErrorException("Documento no encontrado");
            return PassFileToRepository.readLocalFile(documentoOP.get().getPath());

        } catch (IOException e) {
            throw new PncErrorException("Problemas al construir el documento.");
        }
    }

    @Override
    public byte[] descargarPorHistorialId(long historialId) {
        Optional<DesviacionRequisitoHistorial> historialOP = historialRepo.findById(historialId);
        if (historialOP.isPresent()) {
            DesviacionRequisitoHistorial historial = historialOP.get();

            List<DesviacionRequisitoDocumento> archivos = new ArrayList<>();
            archivos = this.repositorio.findByOrdenAndDesviacionRequisito_Id(
                    historial.getOrden(), historial.getDesviacionRequisito().getId());

            try {
                String rutaComprimido = crearRutaComprimido(historial.getDesviacionRequisito().getId());
                compressZip(archivos.stream().collect(Collectors.toMap(DesviacionRequisitoDocumento::getNombreArchivo, DesviacionRequisitoDocumento::getPath)), rutaComprimido);
                return PassFileToRepository.readLocalFile(rutaComprimido);

            } catch (Exception e) {
                log.error(String.format("Error al comprimir archivo Desviacion Requisito %s: %s", historial.getDesviacionRequisito().getSecuencial(), e.getMessage()));
                throw new PncErrorException("Error al momento de descargar los archivos.");
            }
        }
        return null;
    }

    private DesviacionRequisito buscarDesviacionPorId(long id) {
        return this.desviacionRequisitoRepo.findById(id).orElseThrow(() -> new PncErrorException("Solicitud no encontrada"));
    }

    private String crearPathArchivo(long secuencial, String nombreArchivo) {
        String path = crearRutaAlmacenamiento(secuencial).concat(File.separator).concat(nombreArchivo);
        if (PassFileToRepository.fileExists(path))
            path = crearRutaAlmacenamiento(secuencial).concat(File.separator).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);
        log.info(String.format("Ruta creada %s para guardar archivo %s", path, nombreArchivo));
        return path;
    }

    private String crearRutaAlmacenamiento(long secuencial) {
        try {
            String carpeta = configuracion.getRutaBase().concat(File.separator).concat("DESVIACION_REQUISITO").concat(File.separator).concat(String.valueOf(secuencial));
            Path path = Paths.get(carpeta);
            if (!Files.exists(path))
                Files.createDirectories(path);
            return carpeta;
        } catch (IOException e) {
            log.error(String.format("Error al subir Documento %s", e.getMessage()));
            throw new PncErrorException("Error al crear el directorio");
        }
    }

    private String crearRutaComprimido(long secuencial) {
        String ruta = this.crearRutaAlmacenamiento(secuencial);
        return ruta.concat(File.separator).concat(".zip");
    }
}
