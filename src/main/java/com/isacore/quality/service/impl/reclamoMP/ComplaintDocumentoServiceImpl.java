package com.isacore.quality.service.impl.reclamoMP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.DocumentoDto;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.model.reclamoMP.*;
import com.isacore.quality.repository.reclamoMP.ComplainDocumentoRepo;
import com.isacore.quality.repository.reclamoMP.ComplaintHistorialRepo;
import com.isacore.quality.repository.reclamoMP.IComplaintRepo;
import com.isacore.quality.service.reclamoMP.IComplaintDocumentoService;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.UtilidadesCadena;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesArchivo.compressZip;
import static com.isacore.util.UtilidadesArchivo.crearPathArchivo;

@RequiredArgsConstructor
@Slf4j
@Service
public class ComplaintDocumentoServiceImpl implements IComplaintDocumentoService {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private final ComplainDocumentoRepo repositorio;
    private final IComplaintRepo reclamoReporitorio;
    private final ConfiguracionSolicitud configuracion;
    private final ComplaintHistorialRepo historialRepo;


    @Transactional
    @Override
    public ComplaintDocumento subirArchivo(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            DocumentoDto dto = JSON_MAPPER.readValue(jsonDTO, DocumentoDto.class);
            if (dto != null) {
                Complaint salidaOp = buscarReclamoPorId(dto.getSolicitudId());
                String path = crearPathArchivo(salidaOp.getNumber(), nombreArchivo, configuracion.getRutaBase(), "RECLAMO_MP");
                PassFileToRepository.saveLocalFile(path, file);

                ComplaintDocumento documento = new ComplaintDocumento(
                        path,
                        nombreArchivo,
                        salidaOp.getId(),
                        salidaOp.getState().name(),
                        dto.getOrden(),
                        OrigenSolicitud.RECLAMO,
                        dto.getPlanAccionId()
                );
                this.repositorio.save(documento);
                log.info(String.format("RECLAMO MP %s : Documento guardado %s", salidaOp.getNumber(), documento));
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
    public List<ComplaintDocumento> buscarPorOrdenYReclamoId(ComplaintOrdenFlujo orden, long salidaId) {
        return this.repositorio.findByOrdenAndSolicitudId(orden.name(), salidaId);
    }

    @Override
    public boolean eliminarDocumento(long documentoId) {
        ComplaintDocumento documento = this.repositorio.findById(documentoId).orElse(null);
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
            Optional<ComplaintDocumento> documentoOP = repositorio.findById(id);
            if (!documentoOP.isPresent())
                throw new PncErrorException("Documento no encontrado");
            return PassFileToRepository.readLocalFile(documentoOP.get().getPath());

        } catch (IOException e) {
            throw new PncErrorException("Problemas al construir el documento.");
        }
    }

    @Override
    public byte[] descargarPorHistorialId(long historialId) {
        Optional<ComplaintHistorial> historialOP = historialRepo.findById(historialId);
        if (historialOP.isPresent()) {
            ComplaintHistorial historial = historialOP.get();

            List<ComplaintDocumento> archivos = new ArrayList<>();
            archivos = this.repositorio.findByOrdenAndSolicitudId(
                    historial.getOrden().name(), historial.getSolicitudId());

            try {
                String rutaComprimido = crearRutaComprimido(historial.getSolicitudId());
                compressZip(archivos.stream().collect(Collectors.toMap(ComplaintDocumento::getNombreArchivo, ComplaintDocumento::getPath)), rutaComprimido);
                return PassFileToRepository.readLocalFile(rutaComprimido);

            } catch (Exception e) {
                log.error(String.format("Error al comprimir archivo Reclamo MP id:%s: %s", historial.getSolicitudId(), e.getMessage()));
                throw new PncErrorException("Error al momento de descargar los archivos.");
            }
        }
        return null;
    }

    private Complaint buscarReclamoPorId(long id) {
        return this.reclamoReporitorio.findById(id).orElseThrow(() -> new PncErrorException("Solicitud no encontrada"));
    }


    private String crearRutaComprimido(long secuencial) {
        String ruta = crearPathArchivo(secuencial, "test.zip", configuracion.getRutaBase(), "RECLAMO_MP");
        return ruta;
    }
}
