package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.UtilidadesFecha;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;

@Service
@Async
public class ServicioNotificacionSolicitudPP extends ServicioNotificacionBase {

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionSolicitudPP.class);

    private IUserImptekRepo userImptekRepo;

    @Autowired
    public ServicioNotificacionSolicitudPP(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            IUserImptekRepo userImptekRepo) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.userImptekRepo = userImptekRepo;
    }

    public void mensajePrueba(String correo) {
        enviarHtml(new DireccionesDestino(correo), "Correo prueba portal ISA", "emailCivilesPrueba", (context) -> {
            context.setVariable("texto", "Correo de prueba  portal ISA.");
        });
    }

    public void notificarPruebaEjecutada(SolicitudPruebasProceso solicitud) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s PRUEBA EJECUTADA - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioCalidad = this.obtenerUsuario(solicitud.getUsuarioGestionCalidadJefe());
        UserImptek usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        UserImptek usuarioPlantaResponsable = this.obtenerUsuario(solicitud.getUsuarioGestionPlanta());
        UserImptek usuarioProduccion = this.obtenerUsuario(solicitud.getUsuarioGestion());
        UserImptek usuarioValidacion = this.obtenerUsuario(solicitud.getUsuarioValidador());

        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioCalidad.getCorreo());
        destinos.agregarDireccionA(usuarioSolicitante.getCorreo());
        destinos.agregarDireccionA(usuarioMantenimiento.getCorreo());
        destinos.agregarDireccionA(usuarioProduccion.getCorreo());
        destinos.agregarDireccionA(usuarioPlantaResponsable.getCorreo());
        destinos.agregarDireccionA(usuarioValidacion.getCorreo());
        this.agregarUsuariosComprasLogistica(destinos);
        enviarHtml(destinos, asunto, "emailPruebaEjecutada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuarioResponsable", usuarioPlantaResponsable.getEmployee().getCompleteName());
            context.setVariable("fechaPrueba", UtilidadesFecha.formatear(solicitud.getFechaPrueba(), "dd-MM-yyyy hh:mm"));
            context.setVariable("fechaEntregaInforme", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaEntregaInforme(), "dd-MM-yyyy"));
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarPruebaNoEjecutada(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s PRUEBA NO EJECUTADA - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioCalidad = this.obtenerUsuario(solicitud.getUsuarioGestionCalidadJefe());
        UserImptek usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        UserImptek usuarioPlantaResponsable = this.obtenerUsuario(solicitud.getUsuarioGestionPlanta());
        UserImptek usuarioProduccion = this.obtenerUsuario(solicitud.getUsuarioGestion());

        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioCalidad.getCorreo());
        destinos.agregarDireccionA(usuarioSolicitante.getCorreo());
        destinos.agregarDireccionA(usuarioMantenimiento.getCorreo());
        destinos.agregarDireccionA(usuarioProduccion.getCorreo());
        destinos.agregarDireccionA(usuarioPlantaResponsable.getCorreo());

        enviarHtml(destinos, asunto, "emailPruebaNoEjecutada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("observacion", observacion);
            context.setVariable("usuarioResponsable", usuarioPlantaResponsable.getEmployee().getCompleteName());
            context.setVariable("fechaPrueba", UtilidadesFecha.formatear(solicitud.getFechaPrueba(), "dd-MM-yyyy hh:mm"));
            context.setVariable("fechaEntregaInforme", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaEntregaInforme(), "dd-MM-yyyy"));
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarPruebaNoEjecutadaDefinitiva(SolicitudPruebasProceso solicitud, String observacion, UserImptek usuarioAprobador) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD FINALIZADA %s PRUEBA NO EJECUTADA - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioCalidad = this.obtenerUsuario(solicitud.getUsuarioGestionCalidadJefe());
        UserImptek usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        UserImptek usuarioProduccion = this.obtenerUsuario(solicitud.getUsuarioGestion());

        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioCalidad.getCorreo());
        destinos.agregarDireccionA(usuarioSolicitante.getCorreo());
        destinos.agregarDireccionA(usuarioMantenimiento.getCorreo());
        destinos.agregarDireccionA(usuarioProduccion.getCorreo());
        destinos.agregarDireccionA(usuarioAprobador.getCorreo());

        enviarHtml(destinos, asunto, "emailPruebaNoEjecutadaDefinitiva", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarSolicitudAprobada(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String mensajeAprobado = solicitud.isAprobado() ? "APROBADA" : "NO APROBADA";
        String asunto = this.crearAsunto(String.format("Solicitud %s %s - ", solicitud.getCodigo(), mensajeAprobado), solicitud);
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioAprobador = this.obtenerUsuario(solicitud.getUsuarioAprobador());
        DireccionesDestino destinatarios = new DireccionesDestino(usuarioSolicitante.getCorreo());
        destinatarios.agregarDireccionCC(usuarioAprobador.getCorreo());
        enviarHtml(destinatarios, asunto, "emailAprobacionSolicitudPP", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuario", usuarioSolicitante.getEmployee().getCompleteName());
            context.setVariable("aprobado", mensajeAprobado);
            context.setVariable("tipoAprobacion", solicitud.getTipoAprobacion().getDescripcion());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarAjusteMaquinaria(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("Solicitud %s %s - ", solicitud.getCodigo(), solicitud.getTipoAprobacion().getDescripcion()), solicitud);
        UserImptek usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        UserImptek usuarioAprobador = this.obtenerUsuario(solicitud.getUsuarioAprobador());
        DireccionesDestino destinatarios = new DireccionesDestino(usuarioMantenimiento.getCorreo());
        destinatarios.agregarDireccionCC(usuarioAprobador.getCorreo());
        enviarHtml(destinatarios, asunto, "emailAjusteMaquinariaSolicitudPP", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuario", usuarioMantenimiento.getEmployee().getCompleteName());
            context.setVariable("tipoAprobacion", solicitud.getTipoAprobacion().getDescripcion());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarIngresoSolicitud(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("INGRESO DE SOLICITUD DDP04 %s - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioValidador = this.obtenerUsuario(solicitud.getUsuarioValidador());
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioValidador.getCorreo());
        destinos.agregarDireccionCC(usuarioSolicitante.getCorreo());
        enviarHtml(destinos, asunto, "emailIngresoSolicitudDDP04", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioValidador.getEmployee().getCompleteName());
            context.setVariable("nombreSolicitante", usuarioSolicitante.getEmployee().getCompleteName());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarSolicitudValidada(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("INGRESO DE SOLICITUD DDP04 %s - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioValidador = this.obtenerUsuario(solicitud.getUsuarioValidador());
        UserImptek usuarioGestion = this.obtenerUsuario(solicitud.getUsuarioGestion());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioGestion.getCorreo());
        destinos.agregarDireccionCC(usuarioValidador.getCorreo());
        enviarHtml(destinos, asunto, "emailIngresoSolicitudDDP04", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioGestion.getEmployee().getCompleteName());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarSolicitudReasignada(SolicitudPruebasProceso solicitud, String usuarioAsignado, String jefe, String orden) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD DDP04 %s REASIGNADA - ", solicitud.getCodigo()), solicitud);
        UserImptek usuarioAsignadoNuevo = this.obtenerUsuario(usuarioAsignado);
        UserImptek usuarioJefe = this.obtenerUsuario(jefe);
        DireccionesDestino destinos = new DireccionesDestino(usuarioAsignadoNuevo.getCorreo(), usuarioJefe.getCorreo());
        enviarHtml(destinos, asunto, "emailSolicitudReasignada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioAsignadoNuevo.getEmployee().getCompleteName());
            context.setVariable("tipoSolicitud", TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO.getDescripcion());
            context.setVariable("nombreJefe", usuarioJefe.getEmployee().getCompleteName());
            context.setVariable("orden", orden);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarSolicitudEstado(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = String.format("SOLICITUD DDP04 %s %s", solicitud.getCodigo(), solicitud.getEstado().toString());
        UserImptek usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        UserImptek usuarioValidador = this.obtenerUsuario(solicitud.getUsuarioValidador());
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getCorreo(), usuarioValidador.getCorreo());
        enviarHtml(destinos, asunto, "emailSolicitudDDP04Estado", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("tipoSolicitud", TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO.getDescripcion());
            context.setVariable("nombreUsuario", usuarioSolicitante.getEmployee().getCompleteName());
            context.setVariable("estado", solicitud.getEstado().toString());
            context.setVariable("observacion", observacion);
            context.setVariable("revisadoPor", usuarioValidador.getEmployee().getCompleteName());
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    private UserImptek obtenerUsuario(String usuarioId) throws Exception {
        UserImptek usuario = this.userImptekRepo.findOneByNickName(usuarioId);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    private void agregarUsuariosComprasLogistica(DireccionesDestino destinos) {
        this.userImptekRepo.findByActivoAndAreaComprasLogistica().forEach(x -> {
            if (noEsNuloNiBlanco(x.getCorreo()))
                destinos.agregarDireccionA(x.getCorreo());
        });
    }

    private String crearAsunto(String trama, SolicitudPruebasProceso solicitud) {
        String asunto = trama + " " +
                solicitud.getArea().getNameArea() + " " +
                solicitud.getObservacion();
        if (asunto.length() > 200)
            asunto = asunto.substring(0, 200);
        return asunto;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.SOLICITUDES_PRUEBA_PROCESO;
    }
}
