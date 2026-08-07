package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.Adjunto;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.comunes.TipoAprobacion;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.Lote;
import com.isacore.quality.model.desviacionRequisito.SolicitudAprobacionAdicional;
import com.isacore.quality.model.pnc.PncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccionDto;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.repository.desviacionRequisito.ILoteRepo;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
import com.isacore.util.UtilidadesSeguridad;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Async
public class ServicioNotificacionDesviacion extends ServicioNotificacionBase{

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionDesviacion.class);

    private UsuarioRepositorio UsuarioRepo;
    private ILoteRepo loteRepo;

    @Value("${APROBACION_URL_BASE}")
    private String urlBaseAprobacion;

    @Autowired
    public ServicioNotificacionDesviacion(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            UsuarioRepositorio UsuarioRepo,
            ILoteRepo loteRepo
            ) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.UsuarioRepo = UsuarioRepo;
        this.loteRepo = loteRepo;
    }

    public void notificarIngreso(DesviacionRequisito salidaMaterial, String observacion, String usuario) throws Exception {
        String asunto = String.format("DESVIACIÓN REQUISITO %s POR APROBAR", salidaMaterial.getSecuencial());

        Usuario usuarioAprobador = this.obtenerUsuario(salidaMaterial.getUsuarioAprobador());
        Usuario usuarioResponsable = this.obtenerUsuario(usuario);
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getEmail(), usuarioResponsable.getEmail());
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailIngresoDesviacionRequisito", (context) -> {
            context.setVariable("numero", salidaMaterial.getSecuencial());
            context.setVariable("nombreUsuario", usuarioAprobador.getNombre());
            context.setVariable("nombreSolicitante", usuarioResponsable.getNombre());
            context.setVariable("producto", salidaMaterial.getProduct().getNameProduct());
            context.setVariable("tipo", salidaMaterial.getProduct().getTypeProductTxt());
            context.setVariable("cantidad", recuperarCantidadYUnidad(salidaMaterial));
            context.setVariable("observacion", observacion);
        });
    }

    public void notificarAprobacionUrl(DesviacionRequisito desviacionRequisito, String observacion, SolicitudAprobacionAdicional solicitud, List<Adjunto> adjuntoCorreo) throws Exception {
        String asunto = String.format("DESVIACIÓN REQUISITO %s POR APROBAR", desviacionRequisito.getSecuencial());

        Usuario usuarioAprobador = this.obtenerUsuario(solicitud.getDesviacionAprobacionAdicional().getUsuario());
        Usuario usuarioResponsable = this.obtenerUsuario(solicitud.getCreadoPor());
        Usuario usuarioAprobadorPrincipal = this.obtenerUsuario((desviacionRequisito.getUsuarioAprobador()));
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getEmail(), usuarioResponsable.getEmail());
        destinos.agregarDireccionCC(usuarioAprobadorPrincipal.getEmail());
        boolean enviarUrl = !solicitud.getDesviacionAprobacionAdicional().getTipoAprobacion().equals(TipoAprobacion.GERENCIA_GERENCIAL);
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailAprobacionDesviacionRequisito", adjuntoCorreo,(context) -> {
            context.setVariable("numero", desviacionRequisito.getSecuencial());
            context.setVariable("nombreUsuario", usuarioAprobador.getNombre());
            context.setVariable("nombreAprobador", usuarioAprobadorPrincipal.getNombre());
            context.setVariable("producto", desviacionRequisito.getProduct().getNameProduct());
            context.setVariable("lote", obtenerLotes(desviacionRequisito));
            context.setVariable("cantidad", recuperarCantidadYUnidad(desviacionRequisito));
            context.setVariable("urlAprobacion", crearUrlAprobacion(solicitud));
            context.setVariable("descripcionDesviacion", desviacionRequisito.getDescripcion());
            context.setVariable("alcanceDesviacion", desviacionRequisito.getAlcance());
            context.setVariable("verUrlAprobacion", enviarUrl);
        });
    }


    private Usuario obtenerUsuario(String usuarioId) throws Exception {
        Usuario usuario = this.UsuarioRepo.findByNombreUsuario(usuarioId).orElse(null);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    private String recuperarCantidadYUnidad(DesviacionRequisito desviacionRequisito){
        List<Lote> lotes = loteRepo.findByDesviacionRequisito(desviacionRequisito);
        if(lotes.isEmpty())
            return "";
        BigDecimal cantidad = lotes.stream().map(Lote::getCantidad).reduce(BigDecimal.ZERO,BigDecimal::add);
        return cantidad + " " + lotes.stream().findFirst().map(x-> x.getUnidad().getAbreviatura()).get();
    }

    private String crearUrlAprobacion(SolicitudAprobacionAdicional solicitud){
        return urlBaseAprobacion
                .concat(String.valueOf(solicitud.getId()))
                .concat("/").concat(solicitud.getPrenda());
    }

    private String obtenerLotes(DesviacionRequisito desviacionRequisito){
        List<Lote> lotes = loteRepo.findByDesviacionRequisito(desviacionRequisito);
        if(lotes.isEmpty())
            return "";
        return lotes.stream().map(Lote::getLote).collect(Collectors.joining(", "));
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.PRODUCTO_NO_CONFORME;
    }
}


