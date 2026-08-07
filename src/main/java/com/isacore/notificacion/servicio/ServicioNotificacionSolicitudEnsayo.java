package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.se.EstadoExtensionPlazo;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.security.model.Usuario;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
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

    private UsuarioRepositorio UsuarioRepo;

    @Autowired
    public ServicioNotificacionSolicitudEnsayo(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            UsuarioRepositorio UsuarioRepo) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.UsuarioRepo = UsuarioRepo;
    }

    public void notificarSolicitudFinalizada(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s FINALIZADA - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioValidador = this.obtenerUsuario(solicitud.getValidador());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioSolicitante.getEmail());
        destinos.agregarDireccionCC(usuarioValidador.getEmail());
        enviarHtml(destinos, asunto, "emailSolicitudEnsayoFinalizada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuarioSolicitante", usuarioSolicitante.getNombre());
            context.setVariable("tipoAprobacion", this.castearTipoAprobacion(solicitud.getTipoAprobacion()));
            context.setVariable("observacion", observacion);
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", solicitud.getNombreComercial());
        });
    }

    public void notificarIngresoMuestra(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s INGRESO DE MUESTRA - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioResponsable = this.obtenerUsuario(solicitud.getUsuarioGestion());
        Usuario usuarioValidador = this.obtenerUsuario(solicitud.getValidador());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioSolicitante.getEmail());
        destinos.agregarDireccionCC(usuarioResponsable.getEmail());
        destinos.agregarDireccionCC(usuarioValidador.getEmail());
        boolean tieneExtensionFecha = solicitud.getExtensionFecha() != null;
        enviarHtml(destinos, asunto, "emailSolicitudEnsayoIngresoMuestra", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioSolicitante.getNombre());
            context.setVariable("fechaEntregaResultados", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaEntregaInforme(), "dd-MM-yyyy"));
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", solicitud.getNombreComercial());
            context.setVariable("tieneExtensionFecha", tieneExtensionFecha);
        });
    }

    public void notificarSolicitudEstado(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s %s - ", solicitud.getCodigo(), solicitud.getEstado().toString()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioValidador = this.obtenerUsuario(solicitud.getValidador());
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getEmail());
        enviarHtml(destinos, asunto, "emailSolicitudEstado", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("tipoSolicitud", TipoSolicitud.SOLICITUD_ENSAYOS.getDescripcion());
            context.setVariable("nombreUsuario", usuarioSolicitante.getNombre());
            context.setVariable("estado", solicitud.getEstado().toString());
            context.setVariable("observacion", observacion);
            context.setVariable("revisadoPor", usuarioValidador.getNombre());
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", solicitud.getNombreComercial());
        });
    }

    public void notificarSolicitudRecepcionInforme(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s %s - ", solicitud.getCodigo(), solicitud.getEstado().toString()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioValidador = this.obtenerUsuario(solicitud.getValidador());
        Usuario usuarioAprobador = this.obtenerUsuario(solicitud.getUsuarioAprobador());
        Usuario usuarioGestion = this.obtenerUsuario(solicitud.getUsuarioGestion());
        DireccionesDestino destinos = new DireccionesDestino(usuarioValidador.getEmail());
        destinos.agregarDireccionCC(usuarioSolicitante.getEmail());
        destinos.agregarDireccionCC(usuarioAprobador.getEmail());
        destinos.agregarDireccionCC(usuarioGestion.getEmail());
        enviarHtml(destinos, asunto, "emailSolicitudEstado", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("tipoSolicitud", TipoSolicitud.SOLICITUD_ENSAYOS.getDescripcion());
            context.setVariable("nombreUsuario", usuarioAprobador.getNombre());
            context.setVariable("estado", solicitud.getEstado().toString());
            context.setVariable("observacion", observacion);
            context.setVariable("revisadoPor", usuarioSolicitante.getNombre());
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", "");
        });
    }

    public void notificarSolicitudExtensionIngreso(SolicitudEnsayo solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD EXTENSIÓN INGRESADA %s - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioAprobador = this.obtenerUsuario(solicitud.getUsuarioAprobadorExtensionPlazo());
        Usuario usuarioGestion = this.obtenerUsuario(solicitud.getUsuarioGestion());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioAprobador.getEmail());
        enviarHtml(destinos, asunto, "ExtensionPlazo/emailSolicitudExtensionIngreso", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuarioResponsable", usuarioGestion.getNombre());
            context.setVariable("nombreUsuario", usuarioAprobador.getNombre());
            context.setVariable("observacion", observacion);
            context.setVariable("prioridad", solicitud.getPrioridad().toString());
            context.setVariable("proveedor", solicitud.getProveedorNombre());
            context.setVariable("nombreComercial", solicitud.getNombreComercial());
        });
    }

    public void notificarSolicitudExtensionEstado(SolicitudEnsayo solicitud, String observacion, EstadoExtensionPlazo estadoExtensionPlazo) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD EXTENSIÓN %s - %s ", estadoExtensionPlazo, solicitud.getCodigo()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioGestion = this.obtenerUsuario(solicitud.getUsuarioGestion());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioGestion.getEmail());
        if (EstadoExtensionPlazo.APROBADA.equals(estadoExtensionPlazo)) {
            destinos.agregarDireccionCC(usuarioSolicitante.getEmail());
        }
        enviarHtml(destinos, asunto, "ExtensionPlazo/emailSolicitudExtensionEstado", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioGestion.getNombre());
            context.setVariable("estado", estadoExtensionPlazo);
            context.setVariable("aprobado", EstadoExtensionPlazo.APROBADA.equals(estadoExtensionPlazo));
            context.setVariable("nuevaFecha", solicitud.getFechaEntregaInforme());
        });
    }

    private Usuario obtenerUsuario(String usuarioId) throws Exception {
        Usuario usuario = this.UsuarioRepo.findByNombreUsuario(usuarioId).orElse(null);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    private String castearTipoAprobacion(TipoAprobacionSolicitud tipoAprobacion) {
        switch (tipoAprobacion) {
            case NO_APROBADO:
            case VALIDACION_NO_APROBADA:
                return tipoAprobacion.getDescripcion();
            case NIVEL_PLANTA:
            case GESTION_COMPRA:
            case NIVEL_LABORATORIO:
            case SOLICITUD_PRUEBA_PROCESO:
            case LIBRE_USO_GESTION_COMPRA:
            case REQUIERE_PRUEBA_PROCESO:
                return String.format("APROBADO %s", tipoAprobacion.getDescripcion());
            default:
                return "";
        }
    }

    private String crearAsunto(String trama, SolicitudEnsayo solicitud) {
        String asunto = trama + " " +
                solicitud.getPrioridad().toString() + " " +
                solicitud.getNombreComercial() + " " +
                solicitud.getProveedorNombre();
        if (asunto.length() > 200)
            asunto = asunto.substring(0, 200);
        return asunto;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.SOLICITUD_ENSAYO;
    }
}


