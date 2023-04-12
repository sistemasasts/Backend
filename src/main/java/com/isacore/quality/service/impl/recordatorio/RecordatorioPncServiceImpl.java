package com.isacore.quality.service.impl.recordatorio;

import com.isacore.notificacion.servicio.ServicioNotificacionRecordatorio;
import com.isacore.quality.model.pnc.EstadoPncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccion;
import com.isacore.quality.repository.pnc.IPncPlanAccionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordatorioPncServiceImpl {

    private final IPncPlanAccionRepo planAccionRepo;
    private final ServicioNotificacionRecordatorio servicioNotificacionRecordatorio;

    @Scheduled(cron = "0 0 8 ? * MON,TUE,WED,THU,FRI")
//    @Scheduled(cron ="0 0/2 * 1/1 * ?")
    public void notificarPlanesAccionPendientes() {
        log.info("Se inicia proceso recordatorio de planes de accion PNC pendientes");
        this.enviarNotificacion(listarPlanesAccionPendientes());
        log.info("Se finaliza proceso recordatorio de planes de accion PNC pendientes");
    }

    private Map<String, List<PncPlanAccion>> listarPlanesAccionPendientes() {
        return this.planAccionRepo.findByEstadoAndEnTurnoOrderByFechaFin(EstadoPncPlanAccion.ASIGNADO, "x")
                .stream()
                .collect(Collectors.groupingBy(PncPlanAccion::getResponsable));
    }

    private void enviarNotificacion(Map<String, List<PncPlanAccion>> pendientesAsignados) {
        pendientesAsignados.forEach((responsable, solicitudes) -> {
            try {
                this.servicioNotificacionRecordatorio.notificarPlanesAccionPendientes(responsable, this.mapearLista(solicitudes));
            } catch (Exception e) {
                log.error(String.format("Error al notificar recordatorio PNC: %s", e));
            }
        });
    }

    private List<RecordatorioPncDetalleDto> mapearLista(List<PncPlanAccion> solicitudes) {
        return solicitudes.stream().map(x -> new RecordatorioPncDetalleDto(
                x.getSalidaMaterial().getProductoNoConforme().getProducto().getNameProduct(),
                x.getSalidaMaterial().getProductoNoConforme().getNumero(),
                x.getSalidaMaterial().cantidadConUnidad(),
                x.getFechaFin1(),
                String.format("%s Día(s)", x.getVigencia())
        )).collect(Collectors.toList());
    }
}
