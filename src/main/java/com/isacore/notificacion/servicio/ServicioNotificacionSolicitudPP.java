package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;

@Service
@Async
public class ServicioNotificacionSolicitudPP extends ServicioNotificacionBase {

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionSolicitudPP.class);

    private UsuarioRepositorio UsuarioRepo;

    private final IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;

    @Autowired
    public ServicioNotificacionSolicitudPP(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            UsuarioRepositorio UsuarioRepo,
            IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.UsuarioRepo = UsuarioRepo;
        this.configuracionGeneralFlujoRepo = configuracionGeneralFlujoRepo;
    }

    public void mensajePrueba(String correo) {
        enviarHtml(new DireccionesDestino(correo), "Correo prueba portal ISA", "emailCivilesPrueba", (context) -> {
            context.setVariable("texto", "Correo de prueba  portal ISA.");
        });
    }

    public void notificarPruebaEjecutada(SolicitudPruebasProceso solicitud) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s PRUEBA EJECUTADA - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioCalidad = this.obtenerUsuario(solicitud.getUsuarioGestionCalidadJefe());
        Usuario usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        Usuario usuarioPlantaResponsable = this.obtenerUsuario(solicitud.getUsuarioGestionPlanta());
        Usuario usuarioProduccion = this.obtenerUsuario(solicitud.getUsuarioGestion());
        Usuario usuarioValidacion = this.obtenerUsuario(solicitud.getUsuarioValidador());

        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioCalidad.getEmail());
        destinos.agregarDireccionA(usuarioSolicitante.getEmail());
        destinos.agregarDireccionA(usuarioMantenimiento.getEmail());
        destinos.agregarDireccionA(usuarioProduccion.getEmail());
        destinos.agregarDireccionA(usuarioPlantaResponsable.getEmail());
        destinos.agregarDireccionA(usuarioValidacion.getEmail());
        this.agregarUsuariosComprasLogistica(destinos);
        enviarHtml(destinos, asunto, "emailPruebaEjecutada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuarioResponsable", usuarioPlantaResponsable.getNombre());
            context.setVariable("fechaPrueba", UtilidadesFecha.formatear(solicitud.getFechaPrueba(), "dd-MM-yyyy hh:mm"));
            context.setVariable("fechaEntregaInforme", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaEntregaInforme(), "dd-MM-yyyy"));
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarPruebaNoEjecutada(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD %s PRUEBA NO EJECUTADA - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioCalidad = this.obtenerUsuario(solicitud.getUsuarioGestionCalidadJefe());
        Usuario usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        Usuario usuarioPlantaResponsable = this.obtenerUsuario(solicitud.getUsuarioGestionPlanta());
        Usuario usuarioProduccion = this.obtenerUsuario(solicitud.getUsuarioGestion());

        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioCalidad.getEmail());
        destinos.agregarDireccionA(usuarioSolicitante.getEmail());
        destinos.agregarDireccionA(usuarioMantenimiento.getEmail());
        destinos.agregarDireccionA(usuarioProduccion.getEmail());
        destinos.agregarDireccionA(usuarioPlantaResponsable.getEmail());

        enviarHtml(destinos, asunto, "emailPruebaNoEjecutada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("observacion", observacion);
            context.setVariable("usuarioResponsable", usuarioPlantaResponsable.getNombre());
            context.setVariable("fechaPrueba", UtilidadesFecha.formatear(solicitud.getFechaPrueba(), "dd-MM-yyyy hh:mm"));
            context.setVariable("fechaEntregaInforme", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaEntregaInforme(), "dd-MM-yyyy"));
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarPruebaNoEjecutadaDefinitiva(SolicitudPruebasProceso solicitud, String observacion, Usuario usuarioAprobador) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD FINALIZADA %s PRUEBA NO EJECUTADA - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioCalidad = this.obtenerUsuario(solicitud.getUsuarioGestionCalidadJefe());
        Usuario usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        Usuario usuarioProduccion = this.obtenerUsuario(solicitud.getUsuarioGestion());

        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioCalidad.getEmail());
        destinos.agregarDireccionA(usuarioSolicitante.getEmail());
        destinos.agregarDireccionA(usuarioMantenimiento.getEmail());
        destinos.agregarDireccionA(usuarioProduccion.getEmail());
        destinos.agregarDireccionA(usuarioAprobador.getEmail());

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
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioAprobador = this.obtenerUsuario(solicitud.getUsuarioAprobador());
        Set<String> direccionesA = new HashSet<>();
        direccionesA.add(usuarioSolicitante.getEmail());
        DireccionesDestino destinatarios = new DireccionesDestino(direccionesA, crearDireccionCC(usuarioAprobador.getEmail()));
        enviarHtml(destinatarios, asunto, "emailAprobacionSolicitudPP", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuario", usuarioSolicitante.getNombre());
            context.setVariable("aprobado", mensajeAprobado);
            context.setVariable("tipoAprobacion", solicitud.getTipoAprobacion().getDescripcion());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarAjusteMaquinaria(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("Solicitud %s %s - ", solicitud.getCodigo(), solicitud.getTipoAprobacion().getDescripcion()), solicitud);
        Usuario usuarioMantenimiento = this.obtenerUsuario(solicitud.getUsuarioGestionMantenimientoJefe());
        Usuario usuarioAprobador = this.obtenerUsuario(solicitud.getUsuarioAprobador());
        DireccionesDestino destinatarios = new DireccionesDestino(usuarioMantenimiento.getEmail());
        destinatarios.agregarDireccionCC(usuarioAprobador.getEmail());
        enviarHtml(destinatarios, asunto, "emailAjusteMaquinariaSolicitudPP", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuario", usuarioMantenimiento.getNombre());
            context.setVariable("tipoAprobacion", solicitud.getTipoAprobacion().getDescripcion());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarIngresoSolicitud(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("INGRESO DE SOLICITUD DDP04 %s - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioValidador = this.obtenerUsuario(solicitud.getUsuarioValidador());
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioValidador.getEmail());
        destinos.agregarDireccionCC(usuarioSolicitante.getEmail());
        enviarHtml(destinos, asunto, "emailIngresoSolicitudDDP04", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioValidador.getNombre());
            context.setVariable("nombreSolicitante", usuarioSolicitante.getNombre());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarSolicitudValidada(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = this.crearAsunto(String.format("INGRESO DE SOLICITUD DDP04 %s - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioValidador = this.obtenerUsuario(solicitud.getUsuarioValidador());
        Usuario usuarioGestion = this.obtenerUsuario(solicitud.getUsuarioGestion());
        DireccionesDestino destinos = new DireccionesDestino();
        destinos.agregarDireccionA(usuarioGestion.getEmail());
        destinos.agregarDireccionCC(usuarioValidador.getEmail());
        enviarHtml(destinos, asunto, "emailIngresoSolicitudDDP04", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioGestion.getNombre());
            context.setVariable("observacion", observacion);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarSolicitudReasignada(SolicitudPruebasProceso solicitud, String usuarioAsignado, String jefe, String orden) throws Exception {
        String asunto = this.crearAsunto(String.format("SOLICITUD DDP04 %s REASIGNADA - ", solicitud.getCodigo()), solicitud);
        Usuario usuarioAsignadoNuevo = this.obtenerUsuario(usuarioAsignado);
        Usuario usuarioJefe = this.obtenerUsuario(jefe);
        DireccionesDestino destinos = new DireccionesDestino(usuarioAsignadoNuevo.getEmail(), usuarioJefe.getEmail());
        enviarHtml(destinos, asunto, "emailSolicitudReasignada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("nombreUsuario", usuarioAsignadoNuevo.getNombre());
            context.setVariable("tipoSolicitud", TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO.getDescripcion());
            context.setVariable("nombreJefe", usuarioJefe.getNombre());
            context.setVariable("orden", orden);
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    public void notificarSolicitudEstado(SolicitudPruebasProceso solicitud, String observacion) throws Exception {
        String asunto = String.format("SOLICITUD DDP04 %s %s", solicitud.getCodigo(), solicitud.getEstado().toString());
        Usuario usuarioSolicitante = this.obtenerUsuario(solicitud.getNombreSolicitante());
        Usuario usuarioValidador = this.obtenerUsuario(solicitud.getUsuarioValidador());
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getEmail(), usuarioValidador.getEmail());
        enviarHtml(destinos, asunto, "emailSolicitudDDP04Estado", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("tipoSolicitud", TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO.getDescripcion());
            context.setVariable("nombreUsuario", usuarioSolicitante.getNombre());
            context.setVariable("estado", solicitud.getEstado().toString());
            context.setVariable("observacion", observacion);
            context.setVariable("revisadoPor", usuarioValidador.getNombre());
            context.setVariable("area", solicitud.getArea().getNameArea());
            context.setVariable("motivoPrueba", solicitud.getObservacion());
        });
    }

    private Usuario obtenerUsuario(String usuarioId) throws Exception {
        Usuario usuario = this.UsuarioRepo.findByNombreUsuario(usuarioId).orElse(null);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    private void agregarUsuariosComprasLogistica(DireccionesDestino destinos) {
        this.UsuarioRepo.findByActivoAndAreaComprasLogistica().forEach(x -> {
            if (noEsNuloNiBlanco(x.getEmail()))
                destinos.agregarDireccionA(x.getEmail());
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

    private Set<String> crearDireccionCC(String correoAprobador) {
        Set<String> direccion = new HashSet<>();
        direccion.add(correoAprobador);
        ConfiguracionGeneralFlujo config = this.configuracionGeneralFlujoRepo
                .findByTipoSolicitudAndNombreConfiguracionFlujo(
                        TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO,
                        NombreConfiguracionFlujo.CORREOS_ADICIONALES_SOLICITUD_APROBADA)
                .orElse(null);
        if (config != null) {
            if (noEsNuloNiBlanco(config.getValorConfiguracion())) {
                List<String> datos = Arrays.asList(config.getValorConfiguracion().split(";"));
                direccion.addAll(datos);
            }
        }
        return direccion;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.SOLICITUDES_PRUEBA_PROCESO;
    }
}


