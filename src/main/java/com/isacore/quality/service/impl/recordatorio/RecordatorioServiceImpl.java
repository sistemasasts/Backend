package com.isacore.quality.service.impl.recordatorio;

import com.isacore.notificacion.servicio.ServicioNotificacionRecordatorio;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.spp.EstadoSolicitudPPResponsable;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoResponsable;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoResponsableRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.esNuloOBlanco;
import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;
import static com.isacore.util.UtilidadesFecha.formatearLocalDateATexto;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordatorioServiceImpl {

    private final ISolicitudPruebaProcesoResponsableRepo solicitudPruebaProcesoResponsableRepo;
    private final ServicioNotificacionRecordatorio servicioNotificacionRecordatorio;

    @Scheduled(cron ="0 0 8 ? * MON,TUE,WED,THU,FRI")
//    @Scheduled(cron ="0 0/2 * 1/1 * ?")
    public void notificarSolicitudesPendientes() {
        log.info("Se inicia proceso recordatorio de solicitudes pendientes");
        List<SolicitudPruebaProcesoResponsable> pendientes = this.listarSolicitudesPendientes();
        this.notificarPendientesAsignadas(pendientes);
        this.notificarPendientesSinAsignar(pendientes);
        log.info("Se finaliza proceso recordatorio de solicitudes pendientes");
    }

    private List<SolicitudPruebaProcesoResponsable> listarSolicitudesPendientes() {
        return this.solicitudPruebaProcesoResponsableRepo.findByEstado(EstadoSolicitudPPResponsable.PENDIENTE)
                .stream()
                .filter(x -> x.getSolicitudPruebasProceso().getVigencia() <= 1)
                .collect(Collectors.toList());
    }

    private void notificarPendientesSinAsignar(List<SolicitudPruebaProcesoResponsable> pendientes) {
        Map<String, List<SolicitudPruebaProcesoResponsable>> pendientesAsignados = pendientes
                .stream()
                .filter(x -> esNuloOBlanco(x.getUsuarioAsignado()))
                .collect(Collectors.groupingBy(SolicitudPruebaProcesoResponsable::getUsuarioResponsable));
        log.info(String.format("Notificando solicitudes DDP04 pendientes a %s responsables", pendientesAsignados.size()));
        this.enviarNotificacion(pendientesAsignados);
    }

    private void notificarPendientesAsignadas(List<SolicitudPruebaProcesoResponsable> pendientes) {
        Map<String, List<SolicitudPruebaProcesoResponsable>> pendientesAsignados = pendientes
                .stream()
                .filter(x -> noEsNuloNiBlanco(x.getUsuarioAsignado()))
                .collect(Collectors.groupingBy(SolicitudPruebaProcesoResponsable::getUsuarioAsignado));
        log.info(String.format("Notificando solicitudes DDP04 pendientes sin asignar a %s responsables", pendientesAsignados.size()));
        this.enviarNotificacion(pendientesAsignados);
    }

    private void enviarNotificacion( Map<String, List<SolicitudPruebaProcesoResponsable>> pendientesAsignados){
        pendientesAsignados.forEach((responsable, solicitudes) -> {
            try {
                this.servicioNotificacionRecordatorio.notificarSolicitudesPendientes(responsable, this.mapearLista(solicitudes));
            } catch (Exception e) {
                log.error(String.format("Error al notificar recordatorio SPP: %s", e));
            }
        });
    }

    private List<RecordatorioDetalleDto> mapearLista(List<SolicitudPruebaProcesoResponsable> solicitudes) {
        return solicitudes.stream().map(x -> new RecordatorioDetalleDto(
                x.getSolicitudPruebasProceso().getCodigo(),
                TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO.getDescripcion(),
                formatearLocalDateATexto(x.getSolicitudPruebasProceso().getFechaEntregaInforme(), "dd-MM-yyyy"),
                String.format("%s Día(s)", x.getSolicitudPruebasProceso().getVigencia())
        )).collect(Collectors.toList());
    }

}
