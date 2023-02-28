package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.service.impl.recordatorio.RecordatorioDetalleDto;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.List;

@Service
@Async
public class ServicioNotificacionRecordatorio extends ServicioNotificacionBase {
    private static final Log LOG = LogFactory.getLog(ServicioNotificacionRecordatorio.class);
    private IUserImptekRepo userImptekRepo;

    @Autowired
    public ServicioNotificacionRecordatorio(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            IUserImptekRepo userImptekRepo) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.userImptekRepo = userImptekRepo;
    }

    public void notificarSolicitudesPendientes(String usuario, List<RecordatorioDetalleDto> detalle) throws Exception {
        String asunto = String.format("SOLICITUDES PENDIENTES");
        UserImptek responsable = this.obtenerUsuario(usuario);
        DireccionesDestino destinos = new DireccionesDestino(responsable.getCorreo());
        enviarHtml(destinos, asunto, "emailSolicitudPendiente", (context) -> {
            context.setVariable("nombreUsuario", responsable.getEmployee().getCompleteName());
            context.setVariable("solicitudes", detalle);
        });
    }

    private UserImptek obtenerUsuario(String usuarioId) throws Exception {
        UserImptek usuario = this.userImptekRepo.findOneByNickName(usuarioId);
        if (usuario == null)
            throw new Exception(String.format("Usuario %s no encontrado", usuarioId));
        return usuario;
    }

    @Override
    protected MensajeTipo tipo() {
        return MensajeTipo.RECORDATORIO;
    }
}
