package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.UtilidadesFecha;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
@Async
public class ServicioNotificacionSolicitudEnsayo extends ServicioNotificacionBase {

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionSolicitudEnsayo.class);

    private IUserImptekRepo userImptekRepo;

    @Autowired
    public ServicioNotificacionSolicitudEnsayo(
        final ConfiguracionNotificacion configuracionNotificacion,
        final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
        final SpringTemplateEngine springTemplateEngine,
        IUserImptekRepo userImptekRepo) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.userImptekRepo = userImptekRepo;
    }

    public void notificarSolicitudFinalizada(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s FINALIZADA - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioValidador = this.obtenerUsuario(solicitud.getValidador());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioSolicitante.getCorreo());
        destinos.agregarDireccionCC(usuarioValidador.getCorreo());
        enviarHtml(destinos, asunto, "emailSolicitudEnsayoFinalizada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuarioSolicitante", usuarioSolicitante.getEmployee().getCompleteName());
            context.setVariable("tipoAprobacion", this.castearTipoAprobacion(solicitud.getTipoAprobacion()));
            context.setVariable("observacion", observacion);
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", solicitud.getNombreComercial());
        });
    }

    public void notificarIngresoMuestra(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s INGRESO DE MUESTRA - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioResponsable = this.obtenerUsuario(solicitud.getUsuarioGestion());
        UserImptek usuarioValidador = this.obtenerUsuario(solicitud.getValidador());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioSolicitante.getCorreo());
        destinos.agregarDireccionCC(usuarioResponsable.getCorreo());
        destinos.agregarDireccionCC(usuarioValidador.getCorreo());
        enviarHtml(destinos, asunto, "emailSolicitudEnsayoIngresoMuestra", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioSolicitante.getEmployee().getCompleteName());
            context.setVariable("fechaEntregaResultados", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaEntregaInforme(), "dd-MM-yyyy"));
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", solicitud.getNombreComercial());
        });
    }

    public void notificarSolicitudEstado(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s %s - ", solicitud.getCodigo(), solicitud.getEstado().toString()), solicitud);
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioValidador = this.obtenerUsuario(solicitud.getValidador());
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getCorreo());
        enviarHtml(destinos, asunto, "emailSolicitudEstado", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("tipoSolicitud", TipoSolicitud.SOLICITUD_ENSAYOS.getDescripcion());
            context.setVariable("nombreUsuario", usuarioSolicitante.getEmployee().getCompleteName());
            context.setVariable("estado", solicitud.getEstado().toString());
            context.setVariable("observacion", observacion);
            context.setVariable("revisadoPor", usuarioValidador.getEmployee().getCompleteName());
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", solicitud.getNombreComercial());
        });
    }

    private UserImptek obtenerUsuario(String usuarioId) throws Exception {
        UserImptek usuario = this.userImptekRepo.findOneByNickName(usuarioId);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    private String castearTipoAprobacion(TipoAprobacionSolicitud tipoAprobacion) {
        switch (tipoAprobacion) {
            case NO_APROBADO:
                return tipoAprobacion.getDescripcion();
            case NIVEL_PLANTA:
            case GESTION_COMPRA:
            case NIVEL_LABORATORIO:
            case SOLICITUD_PRUEBA_PROCESO:
                return String.format("APROBADO %s", tipoAprobacion.getDescripcion());
            default:
                return "";
        }
    }

    private String crearAsunto(String trama, SolicitudEnsayo solicitud){
        String asunto = trama + " " +
                solicitud.getPrioridad().toString() + " " +
                solicitud.getNombreComercial() + " " +
                solicitud.getProveedorNombre();
        if(asunto.length() > 200)
            asunto = asunto.substring(0,200);
        return asunto;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.SOLICITUD_ENSAYO;
    }
}
