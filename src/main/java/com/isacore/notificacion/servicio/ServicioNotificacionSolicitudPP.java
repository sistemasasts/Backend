package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
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

@Service
@Async
public class ServicioNotificacionSolicitudPP extends ServicioNotificacionBase{

    private static final Log LOG = LogFactory.getLog(ServicioNotificacionSolicitudPP.class);

    private IUserImptekRepo userImptekRepo;

    @Autowired
    public ServicioNotificacionSolicitudPP(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            IUserImptekRepo userImptekRepo){
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.userImptekRepo = userImptekRepo;
    }

    public void mensajePrueba(String correo){
        enviarHtml(new DireccionesDestino(correo),  "Correo prueba módulo civiles", "emailCivilesPrueba", (context) -> {
            context.setVariable("texto", "Correo de prueba módulo civiles.");
        });
    }

    public void notificarPruebaEjecutada(SolicitudPruebasProceso solicitud) throws Exception {
        String asunto = String.format("SOLICITUD %s PRUEBA EJECUTADA", solicitud.getCodigo());
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

        enviarHtml(destinos,  asunto, "emailPruebaEjecutada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuarioResponsable", usuarioPlantaResponsable.getEmployee().getCompleteName());
            context.setVariable("fechaPrueba", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaPrueba(), "dd-MM-yyyy"));
            context.setVariable("fechaEntregaInforme", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaPrueba(), "dd-MM-yyyy"));
        });
    }

    public void notificarPruebaNoEjecutada(SolicitudPruebasProceso solicitud) throws Exception {
        String asunto = String.format("SOLICITUD %s PRUEBA NO EJECUTADA", solicitud.getCodigo());
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

        enviarHtml(destinos,  asunto, "emailPruebaNoEjecutada", (context) -> {
            context.setVariable("codigo", solicitud.getCodigo());
            context.setVariable("usuarioResponsable", usuarioPlantaResponsable.getEmployee().getCompleteName());
            context.setVariable("fechaPrueba", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaPrueba(), "DD-MM-YYYY"));
            context.setVariable("fechaEntregaInforme", UtilidadesFecha.formatearLocalDateATexto(solicitud.getFechaPrueba(), "DD-MM-YYYY"));
        });
    }

    private UserImptek obtenerUsuario(String usuarioId) throws Exception {
        UserImptek usuario = this.userImptekRepo.findOneByNickName(usuarioId);
        if(usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.SOLICITUDES_PRUEBA_PROCESO;
    }
}
