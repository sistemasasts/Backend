package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.Lote;
import com.isacore.quality.model.desviacionRequisito.SolicitudAprobacionAdicional;
import com.isacore.quality.model.pnc.PncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccionDto;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.repository.desviacionRequisito.ILoteRepo;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
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

@Service
@Async
public class ServicioNotificacionDesviacion extends ServicioNotificacionBase{

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionDesviacion.class);

    private IUserImptekRepo userImptekRepo;
    private ILoteRepo loteRepo;

    @Value("${APROBACION_URL_BASE}")
    private String urlBaseAprobacion;

    @Autowired
    public ServicioNotificacionDesviacion(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            IUserImptekRepo userImptekRepo,
            ILoteRepo loteRepo
            ) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.userImptekRepo = userImptekRepo;
        this.loteRepo = loteRepo;
    }

    public void notificarIngreso(DesviacionRequisito salidaMaterial, String observacion, String usuario) throws Exception {
        String asunto = String.format("DESVIACIÓN REQUISITO %s POR APROBAR", salidaMaterial.getSecuencial());

        UserImptek usuarioAprobador = this.obtenerUsuario(salidaMaterial.getUsuarioAprobador());
        UserImptek usuarioResponsable = this.obtenerUsuario(usuario);
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getCorreo(), usuarioResponsable.getCorreo());
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailIngresoDesviacionRequisito", (context) -> {
            context.setVariable("numero", salidaMaterial.getSecuencial());
            context.setVariable("nombreUsuario", usuarioAprobador.getEmployee().getCompleteName());
            context.setVariable("nombreSolicitante", usuarioResponsable.getEmployee().getCompleteName());
            context.setVariable("producto", salidaMaterial.getProduct().getNameProduct());
            context.setVariable("tipo", salidaMaterial.getProduct().getTypeProductTxt());
            context.setVariable("cantidad", recuperarCantidadYUnidad(salidaMaterial));
            context.setVariable("observacion", observacion);
        });
    }

    public void notificarAprobacionUrl(DesviacionRequisito salidaMaterial, String observacion, SolicitudAprobacionAdicional solicitud) throws Exception {
        String asunto = String.format("DESVIACIÓN REQUISITO %s POR APROBAR", salidaMaterial.getSecuencial());

        UserImptek usuarioAprobador = this.obtenerUsuario(solicitud.getDesviacionAprobacionAdicional().getUsuario());
        UserImptek usuarioResponsable = this.obtenerUsuario(solicitud.getCreadoPor());
        DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getCorreo(), usuarioResponsable.getCorreo());
        enviarHtml(destinos, asunto, "ProductoNoConforme/emailAprobacionDesviacionRequisito", (context) -> {
            context.setVariable("numero", salidaMaterial.getSecuencial());
            context.setVariable("nombreUsuario", usuarioAprobador.getEmployee().getCompleteName());
            context.setVariable("nombreSolicitante", usuarioResponsable.getEmployee().getCompleteName());
            context.setVariable("producto", salidaMaterial.getProduct().getNameProduct());
            context.setVariable("tipo", salidaMaterial.getProduct().getTypeProductTxt());
            context.setVariable("cantidad", recuperarCantidadYUnidad(salidaMaterial));
            context.setVariable("observacion", observacion);
            context.setVariable("urlAprobacion", crearUrlAprobacion(solicitud));
        });
    }


    private UserImptek obtenerUsuario(String usuarioId) throws Exception {
        UserImptek usuario = this.userImptekRepo.findOneByNickName(usuarioId);
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

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.PRODUCTO_NO_CONFORME;
    }
}
