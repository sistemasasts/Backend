package com.isacore.quality.service.aprobacionSolicitud;

import com.isacore.quality.dto.SolicitudAprobacionDto;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.spp.EstadoSolicitudPP;
import com.isacore.quality.model.spp.EstadoSolicitudPPResponsable;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import com.isacore.quality.repository.se.ISolicitudEnsayoRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoResponsableRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebasProcesoRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@Service
public class AprobacionSolicitudService {

    private static final Log LOG = LogFactory.getLog(AprobacionSolicitudService.class);

    private ISolicitudEnsayoRepo solicitudEnsayoRepo;
    private ISolicitudPruebasProcesoRepo solicitudPruebasProcesoRepo;
    private ISolicitudPruebaProcesoResponsableRepo responsableRepo;

    @Autowired
    public AprobacionSolicitudService(ISolicitudEnsayoRepo solicitudEnsayoRepo, ISolicitudPruebasProcesoRepo solicitudPruebasProcesoRepo,
                                      ISolicitudPruebaProcesoResponsableRepo responsableRepo) {
        this.solicitudEnsayoRepo = solicitudEnsayoRepo;
        this.solicitudPruebasProcesoRepo = solicitudPruebasProcesoRepo;
        this.responsableRepo = responsableRepo;
    }

    @Transactional(readOnly = true)
    public List<SolicitudAprobacionDto> listarPendientesValidar() {
        List<SolicitudAprobacionDto> solicitudes = new ArrayList<>();
        this.solicitudEnsayoRepo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.ENVIADO_REVISION, nombreUsuarioEnSesion()).forEach(x -> {
            solicitudes.add(this.crearDto(TipoSolicitud.SOLICITUD_ENSAYOS, x));
        });
        this.solicitudPruebasProcesoRepo.findByEstadoAndUsuarioValidadorOrderByFechaCreacionDesc(EstadoSolicitudPP.ENVIADO_REVISION, nombreUsuarioEnSesion()).forEach(x -> {
            solicitudes.add(this.crearDto(TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO, x));
        });

        return solicitudes;
    }

    @Transactional(readOnly = true)
    public List<SolicitudAprobacionDto> listarPendientesRevisarInforme() {
        List<SolicitudAprobacionDto> solicitudes = new ArrayList<>();
        this.solicitudEnsayoRepo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.REVISION_INFORME, nombreUsuarioEnSesion()).forEach(x -> {
            solicitudes.add(this.crearDto(TipoSolicitud.SOLICITUD_ENSAYOS, x));
        });
        this.responsableRepo.findByUsuarioResponsableAndActivoTrueAndOrdenAndEstadoIn(nombreUsuarioEnSesion(), OrdenFlujoPP.CALIDAD,
            Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE, EstadoSolicitudPPResponsable.POR_APROBAR))
            .forEach(x -> {
                solicitudes.add(this.crearDto(TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO, x.getSolicitudPruebasProceso()));
            });
        return solicitudes;
    }

    @Transactional(readOnly = true)
    public List<SolicitudAprobacionDto> listarPendientesAprobarSolicitud() {
        List<SolicitudAprobacionDto> solicitudes = new ArrayList<>();
        this.solicitudEnsayoRepo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_APROBACION, nombreUsuarioEnSesion()).forEach(x -> {
            solicitudes.add(this.crearDto(TipoSolicitud.SOLICITUD_ENSAYOS, x));
        });
        this.solicitudPruebasProcesoRepo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitudPP.PENDIENTE_APROBACION, nombreUsuarioEnSesion()).forEach(x -> {
            solicitudes.add(this.crearDto(TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO, x));
        });
        return solicitudes;
    }

    private SolicitudAprobacionDto crearDto(TipoSolicitud tipo, Object obj) {
        switch (tipo) {
            case SOLICITUD_ENSAYOS:
                SolicitudEnsayo solicitud = (SolicitudEnsayo) obj;
                return new SolicitudAprobacionDto(solicitud.getId(), solicitud.getCodigo(), solicitud.getFechaCreacion(), solicitud.getProveedorNombre(), solicitud.getEstado().toString(),
                    solicitud.getNombreSolicitante(), solicitud.getFechaEntrega(), solicitud.getDetalleMaterial(), solicitud.getFechaEntregaInforme(), solicitud.getVigencia(), TipoSolicitud.SOLICITUD_ENSAYOS, "");
            case SOLICITUD_PRUEBAS_EN_PROCESO:
                SolicitudPruebasProceso solicitudPP = (SolicitudPruebasProceso) obj;
                SolicitudAprobacionDto pp = new SolicitudAprobacionDto(solicitudPP.getId(), solicitudPP.getCodigo(), solicitudPP.getFechaCreacion(), "", solicitudPP.getEstado().toString(),
                    solicitudPP.getNombreSolicitante(), solicitudPP.getFechaEntrega(), "", solicitudPP.getFechaEntregaInforme(), solicitudPP.getVigencia(), TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO, solicitudPP.getObservacion());
                pp.setLineaAplicacion(solicitudPP.getLineaAplicacion());
                pp.setMotivo(solicitudPP.getMotivo());
                return pp;
            default:
                return new SolicitudAprobacionDto();
        }
    }

}
