package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.Adjunto;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.reclamoMP.Complaint;
import com.isacore.quality.model.reclamoMP.ComplaintOrdenFlujo;
import com.isacore.quality.model.reclamoMP.ComplaintPlanAccionEstado;
import com.isacore.quality.model.reclamoMP.ProviderActionPlan;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
import com.isacore.util.UtilidadesCadena;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;

@Service
@Async
public class ServicioNotificacionReclamoMP extends ServicioNotificacionBase {

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionReclamoMP.class);

    private UsuarioRepositorio UsuarioRepo;
    private IProductRepo productRepo;

    @Autowired
    public ServicioNotificacionReclamoMP(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            UsuarioRepositorio UsuarioRepo,
            IProductRepo productRepo
    ) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.UsuarioRepo = UsuarioRepo;
        this.productRepo = productRepo;
    }

    public void notificarPendienteAprobacion(Complaint salidaMaterial, String observacion, ComplaintOrdenFlujo ordenFlujo) throws Exception {
        Product producto = this.obtenerProducto(salidaMaterial.getIdProduct());
        String asunto = String.format("Reclamo MP %s - %s",
                salidaMaterial.getNumber(), producto == null ? "" : producto.getNameProduct());

        String aprobador = ordenFlujo.equals(ComplaintOrdenFlujo.APROBACION_CALIDAD) ? salidaMaterial.getAprobadorCalidad() : salidaMaterial.getAprobadorCompras();
        Usuario usuarioAprobador = this.obtenerUsuario(aprobador);
        Usuario usuarioResponsable = this.obtenerUsuario(salidaMaterial.getAsUser());
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getEmail(), usuarioResponsable.getEmail());
        enviarHtml(destinos, asunto, "ReclamoMP/emailAprobacionPendiente", new ArrayList<>(), (context) -> {
            context.setVariable("numero", salidaMaterial.getNumber());
            context.setVariable("nombreAprobador", usuarioAprobador.getNombre());
            context.setVariable("nombreSolicitante", salidaMaterial.getUserName());
            context.setVariable("producto", producto == null ? "" : producto.getNameProduct());
            context.setVariable("cantidadTotal", salidaMaterial.getTotalAmount());
            context.setVariable("cantidadAfectada", salidaMaterial.getAffectedAmount());
            context.setVariable("observacion", observacion);
        });
    }

    public void notificarCambioEstado(Complaint salidaMaterial, String observacion, String usuarioProceso) throws Exception {
        Product producto = this.obtenerProducto(salidaMaterial.getIdProduct());
        String asunto = String.format("Reclamo MP %s - %s - %s",
                salidaMaterial.getNumber(), salidaMaterial.getState().getDescripcion(), producto == null ? "" : producto.getNameProduct());

        Usuario usuarioSolicitante = this.obtenerUsuario(salidaMaterial.getAsUser());
        Usuario usuarioResponsable = this.obtenerUsuario(usuarioProceso);
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getEmail(), usuarioResponsable.getEmail());
        enviarHtml(destinos, asunto, "ReclamoMP/emailCambioEstado", (context) -> {
            context.setVariable("numero", salidaMaterial.getNumber());
            context.setVariable("estado", salidaMaterial.getState().getDescripcion());
            context.setVariable("nombreUsuario", usuarioSolicitante.getNombre());
            context.setVariable("nombreSolicitante", usuarioResponsable.getNombre());
            context.setVariable("producto", producto == null ? "" : producto.getNameProduct());
            context.setVariable("cantidadTotal", salidaMaterial.getTotalAmount());
            context.setVariable("cantidadAfectada", salidaMaterial.getAffectedAmount());
            context.setVariable("observacion", observacion);
        });
    }

    public void notificarReclamoAprobado(Complaint salidaMaterial, String observacion, List<String> destinatarios, List<Adjunto> adjuntoCorreo, String asunto) throws Exception {
        Product producto = this.obtenerProducto(salidaMaterial.getIdProduct());
        if (UtilidadesCadena.esNuloOBlanco(asunto))
            asunto = String.format("Reclamo MP %s - %s",
                    salidaMaterial.getNumber(), producto == null ? "" : producto.getNameProduct());

        Usuario usuarioSolicitante = this.obtenerUsuario(salidaMaterial.getAsUser());
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getEmail());
        destinatarios.forEach(destinos::agregarDireccionA);
        enviarHtml(destinos, asunto, "ReclamoMP/emailReclamoFinal", adjuntoCorreo, (context) -> {
            context.setVariable("observacion", observacion);
        });
    }


    public void notificarPlanAccionAsignado(Complaint reclamo, List<ProviderActionPlan> planes) throws Exception {
        Product producto = this.obtenerProducto(reclamo.getIdProduct());
        String asunto = String.format("Reclamo MP %s - PLAN DE ACCIÓN ASIGNADO %s",
                reclamo.getNumber(),
                producto.getNameProduct());
        Usuario usuarioResponsable = this.obtenerUsuario(planes.stream().findFirst().get().getResponsable());
        DireccionesDestino destinos = new DireccionesDestino(usuarioResponsable.getEmail(), usuarioResponsable.getEmail());
        enviarHtml(destinos, asunto, "ReclamoMP/emailPlanAccionAsignado", (context) -> {
            context.setVariable("numero", reclamo.getNumber());
            context.setVariable("nombreUsuario", usuarioResponsable.getNombre());
            context.setVariable("planes", planes);
        });
    }

    public void notificarPlanAccionEstado(Complaint reclamo, List<ProviderActionPlan> planes, ComplaintPlanAccionEstado estado,
                                          String observacion, String usuarioProcesa) throws Exception {
        Product producto = this.obtenerProducto(reclamo.getIdProduct());
        String asunto = String.format("Reclamo MP %s - PLAN DE ACCIÓN PROCESADO %s",
                reclamo.getNumber(),
                producto.getNameProduct());
        Usuario usuarioResponsable = this.obtenerUsuario(planes.stream().findFirst().get().getResponsable());
        Usuario usuarioSesion = this.obtenerUsuario(usuarioProcesa);
        DireccionesDestino destinos = new DireccionesDestino(usuarioResponsable.getEmail(), usuarioResponsable.getEmail());
        String observacionFinal;
        if (estado.equals(ComplaintPlanAccionEstado.REGRESADO)) {
            observacionFinal = "REGRESADO " + observacion;
        } else {
            observacionFinal = observacion;
        }
        enviarHtml(destinos, asunto, "ReclamoMP/emailPlanAccionAsignadoEstado", (context) -> {
            context.setVariable("numero", reclamo.getNumber());
            context.setVariable("nombreUsuario", usuarioResponsable.getNombre());
            context.setVariable("responsable", usuarioSesion.getNombre());
            context.setVariable("observacion", observacionFinal);
            context.setVariable("planes", planes);
        });
    }

    private Usuario obtenerUsuario(String usuarioId) throws Exception {
        Usuario usuario = this.UsuarioRepo.findByNombreUsuario(usuarioId).orElse(null);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    private Product obtenerProducto(Integer id) {
        return this.productRepo.findById(id).orElse(null);
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.PRODUCTO_NO_CONFORME;
    }
}


