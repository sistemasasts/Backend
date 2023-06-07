package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.model.cardex.InventarioProducto;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.pnc.PncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccionDto;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.UtilidadesCadena;
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
public class ServicioNotificacionInventario extends ServicioNotificacionBase {

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionInventario.class);

    private IUserImptekRepo userImptekRepo;
    private IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;

    @Autowired
    public ServicioNotificacionInventario(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            IUserImptekRepo userImptekRepo,
            IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo
    ) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.userImptekRepo = userImptekRepo;
        this.configuracionGeneralFlujoRepo = configuracionGeneralFlujoRepo;
    }

    public void notificarNecesitaCompra(InventarioProducto inventarioProducto) throws Exception {
        String asunto = String.format("%s - STOCK EN EL LÍMITE",
                inventarioProducto.getProducto().getNameProduct());
        UserImptek usuarioAprobador = this.obtenerDestinatarios();
        if(usuarioAprobador != null){
            DireccionesDestino destinos = new DireccionesDestino(usuarioAprobador.getCorreo());
            enviarHtml(destinos, asunto, "InventarioProducto/emailCompraProducto", (context) -> {
                context.setVariable("nombreUsuario", usuarioAprobador.getEmployee().getCompleteName());
                context.setVariable("producto", inventarioProducto.getProducto().getNameProduct());
            });
        }else{
            LOG.error("No es posible enviar la notificación no se encontró la configuracion");
        }
    }


    private UserImptek obtenerDestinatarios() throws Exception {
        ConfiguracionGeneralFlujo configuracion = configuracionGeneralFlujoRepo.findByTipoSolicitudAndNombreConfiguracionFlujo(
                TipoSolicitud.INVENTARIO_PRODUCTO, NombreConfiguracionFlujo.NOTIFICAR_COMPRA_PRODUCTOS).orElse(null);
        if (configuracion != null) {
            if (UtilidadesCadena.noEsNuloNiBlanco(configuracion.getValorConfiguracion())) {
                return this.obtenerUsuario(configuracion.getValorConfiguracion());
            }
        }
        return null;
    }

    private UserImptek obtenerUsuario(String usuarioId) throws Exception {
        UserImptek usuario = this.userImptekRepo.findOneByNickName(usuarioId);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.INVENTARIO_PRODUCTO;
    }
}
