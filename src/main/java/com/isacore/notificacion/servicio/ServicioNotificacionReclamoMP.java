package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.Adjunto;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.pnc.PncPlanAccion;
import com.isacore.quality.model.reclamoMP.Complaint;
import com.isacore.quality.model.reclamoMP.ComplaintOrdenFlujo;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.UtilidadesCadena;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Async
public class ServicioNotificacionReclamoMP extends ServicioNotificacionBase {

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionReclamoMP.class);

    private IUserImptekRepo userImptekRepo;
    private IProductRepo productRepo;

    @Autowired
    public ServicioNotificacionReclamoMP(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            IUserImptekRepo userImptekRepo,
            IProductRepo productRepo
    ) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.userImptekRepo = userImptekRepo;
        this.productRepo = productRepo;
    }

    public void notificarPendienteAprobacion(Complaint salidaMaterial, String observacion, ComplaintOrdenFlujo ordenFlujo) throws Exception {
        Product producto = this.obtenerProducto(salidaMaterial.getIdProduct());
        String asunto = String.format("Reclamo MP %s - %s",
                salidaMaterial.getNumber(), producto == null ? "" : producto.getNameProduct());

        String aprobador = ordenFlujo.equals(ComplaintOrdenFlujo.APROBACION_CALIDAD) ? salidaMaterial.getAprobadorCalidad() : salidaMaterial.getAprobadorCompras();
        UserImptek usuarioAprobador = this.obtenerUsuario(aprobador);
        UserImptek usuarioResponsable = this.obtenerUsuario(salidaMaterial.getAsUser());
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getCorreo(), usuarioResponsable.getCorreo());
        enviarHtml(destinos, asunto, "ReclamoMP/emailAprobacionPendiente", new ArrayList<>(), (context) -> {
            context.setVariable("numero", salidaMaterial.getNumber());
            context.setVariable("nombreAprobador", usuarioAprobador.getEmployee().getCompleteName());
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

        UserImptek usuarioSolicitante = this.obtenerUsuario(salidaMaterial.getAsUser());
        UserImptek usuarioResponsable = this.obtenerUsuario(usuarioProceso);
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getCorreo(), usuarioResponsable.getCorreo());
        enviarHtml(destinos, asunto, "ReclamoMP/emailCambioEstado", (context) -> {
            context.setVariable("numero", salidaMaterial.getNumber());
            context.setVariable("estado", salidaMaterial.getState().getDescripcion());
            context.setVariable("nombreUsuario", usuarioSolicitante.getEmployee().getCompleteName());
            context.setVariable("nombreSolicitante", usuarioResponsable.getEmployee().getCompleteName());
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

        UserImptek usuarioSolicitante = this.obtenerUsuario(salidaMaterial.getAsUser());
        DireccionesDestino destinos = new DireccionesDestino(usuarioSolicitante.getCorreo());
        destinatarios.forEach(destinos::agregarDireccionA);
        enviarHtml(destinos, asunto, "ReclamoMP/emailReclamoFinal", adjuntoCorreo, (context) -> {
            context.setVariable("observacion", observacion);
        });
    }


    public void notificarPlanAccionHabilito(PncPlanAccion planAccion) throws Exception {
        String asunto = String.format("PNC %s - PLAN DE ACCIÓN ASIGNADO %s",
                planAccion.getSalidaMaterial().getProductoNoConforme().getNumero(),
                planAccion.getSalidaMaterial().getProductoNoConforme().getProducto().getNameProduct());
        UserImptek usuarioResponsable = this.obtenerUsuario(planAccion.getResponsable());
        DireccionesDestino destinos = new DireccionesDestino(usuarioResponsable.getCorreo(), usuarioResponsable.getCorreo());
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailPlanAccionAsignado", (context) -> {
            context.setVariable("numero", planAccion.getSalidaMaterial().getProductoNoConforme().getNumero());
            context.setVariable("nombreUsuario", usuarioResponsable.getEmployee().getCompleteName());
            context.setVariable("producto", planAccion.getSalidaMaterial().getProductoNoConforme().getProducto().getNameProduct());
            context.setVariable("destino", planAccion.getSalidaMaterial().getDestino().getDescripcion());
            context.setVariable("cantidad", planAccion.getSalidaMaterial().getCantidad());
            context.setVariable("planes", Arrays.asList(planAccion));
        });
    }

    private UserImptek obtenerUsuario(String usuarioId) throws Exception {
        UserImptek usuario = this.userImptekRepo.findOneByNickName(usuarioId);
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
