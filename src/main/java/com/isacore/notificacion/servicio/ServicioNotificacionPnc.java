package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.pnc.PncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccionDto;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.service.pnc.IPncPlanAccionService;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Arrays;
import java.util.List;

@Service
@Async
public class ServicioNotificacionPnc extends ServicioNotificacionBase{

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionPnc.class);

    private UsuarioRepositorio UsuarioRepo;

    @Autowired
    public ServicioNotificacionPnc(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            UsuarioRepositorio UsuarioRepo
            ) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.UsuarioRepo = UsuarioRepo;
    }

    public void notificarIngresoSalidaMaterial(PncSalidaMaterial salidaMaterial, String observacion, List<PncPlanAccionDto> planes) throws Exception {
        String asunto = String.format("PNC %s - INGRESO SALIDA MATERIAL %s",
                salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getDestino().getDescripcion());

        Usuario usuarioAprobador = this.obtenerUsuario(salidaMaterial.getUsuarioAprobador());
        Usuario usuarioResponsable = this.obtenerUsuario(salidaMaterial.getUsuario());
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getEmail(), usuarioResponsable.getEmail());
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailIngresoSalidaMaterial", (context) -> {
            context.setVariable("numero", salidaMaterial.getProductoNoConforme().getNumero());
            context.setVariable("nombreUsuario", usuarioAprobador.getNombre());
            context.setVariable("nombreSolicitante", usuarioResponsable.getNombre());
            context.setVariable("producto", salidaMaterial.getProductoNoConforme().getProducto().getNameProduct());
            context.setVariable("destino", salidaMaterial.getDestino().getDescripcion());
            context.setVariable("cantidad", salidaMaterial.getCantidad());
            context.setVariable("verPlanesAccion", salidaMaterial.verPlanesAccion());
            context.setVariable("planes", planes);
            context.setVariable("observacion", observacion);
        });
    }

    public void notificarSalidaMaterialEstado(PncSalidaMaterial salidaMaterial, String observacion, List<PncPlanAccionDto> planes) throws Exception {
        String asunto = String.format("PNC %s - SALIDA MATERIAL %s - %s",
                salidaMaterial.getProductoNoConforme().getNumero(), salidaMaterial.getDestino().getDescripcion(),
                salidaMaterial.getEstado().getDescripcion());

        Usuario usuarioAprobador = this.obtenerUsuario(salidaMaterial.getUsuarioAprobador());
        Usuario usuarioResponsable = this.obtenerUsuario(salidaMaterial.getUsuario());
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getEmail(), usuarioResponsable.getEmail());
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailEstadoSalidaMaterial", (context) -> {
            context.setVariable("numero", salidaMaterial.getProductoNoConforme().getNumero());
            context.setVariable("nombreUsuario", usuarioResponsable.getNombre());
            context.setVariable("estado", salidaMaterial.getEstado().getDescripcion());
            context.setVariable("nombreResponsable", usuarioAprobador.getNombre());
            context.setVariable("producto", salidaMaterial.getProductoNoConforme().getProducto().getNameProduct());
            context.setVariable("destino", salidaMaterial.getDestino().getDescripcion());
            context.setVariable("cantidad",salidaMaterial.cantidadConUnidad());
            context.setVariable("verPlanesAccion", salidaMaterial.verPlanesAccion());
            context.setVariable("planes", planes);
            context.setVariable("observacion", observacion);
        });
    }

    public void notificarPlanAccionHabilito(PncPlanAccion planAccion) throws Exception {
        String asunto = String.format("PNC %s - PLAN DE ACCIÓN ASIGNADO %s",
                planAccion.getSalidaMaterial().getProductoNoConforme().getNumero(),
                planAccion.getSalidaMaterial().getProductoNoConforme().getProducto().getNameProduct());
        Usuario usuarioResponsable = this.obtenerUsuario(planAccion.getResponsable());
        DireccionesDestino destinos = new DireccionesDestino(usuarioResponsable.getEmail(), usuarioResponsable.getEmail());
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailPlanAccionAsignado", (context) -> {
            context.setVariable("numero", planAccion.getSalidaMaterial().getProductoNoConforme().getNumero());
            context.setVariable("nombreUsuario", usuarioResponsable.getNombre());
            context.setVariable("producto", planAccion.getSalidaMaterial().getProductoNoConforme().getProducto().getNameProduct());
            context.setVariable("destino", planAccion.getSalidaMaterial().getDestino().getDescripcion());
            context.setVariable("cantidad", planAccion.getSalidaMaterial().getCantidad());
            context.setVariable("planes", Arrays.asList(planAccion));
        });
    }

    private Usuario obtenerUsuario(String usuarioId) throws Exception {
        Usuario usuario = this.UsuarioRepo.findByNombreUsuario(usuarioId).orElse(null);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.PRODUCTO_NO_CONFORME;
    }
}


