package com.isacore.notificacion.servicio;

import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.MensajeTipo;
import com.isacore.quality.service.impl.recordatorio.RecordatorioDetalleDto;
import com.isacore.quality.service.impl.recordatorio.RecordatorioPncDetalleDto;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
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
    private UsuarioRepositorio UsuarioRepo;

    @Autowired
    public ServicioNotificacionRecordatorio(
            final ConfiguracionNotificacion configuracionNotificacion,
            final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
            final SpringTemplateEngine springTemplateEngine,
            UsuarioRepositorio UsuarioRepo) {
        super(configuracionNotificacion, LOG, proveedorCorreoElectronico, springTemplateEngine);
        this.UsuarioRepo = UsuarioRepo;
    }

    public void notificarSolicitudesPendientes(String usuario, List<RecordatorioDetalleDto> detalle) throws Exception {
        String asunto = String.format("SOLICITUDES PENDIENTES");
        Usuario responsable = this.obtenerUsuario(usuario);
        DireccionesDestino destinos = new DireccionesDestino(responsable.getEmail());
        enviarHtml(destinos, asunto, "emailSolicitudPendiente", (context) -> {
            context.setVariable("nombreUsuario", responsable.getNombre());
            context.setVariable("solicitudes", detalle);
        });
    }

    public void notificarPlanesAccionPendientes(String usuario, List<RecordatorioPncDetalleDto> detalle) throws Exception {
        String asunto = String.format("PLANES DE ACCIÓN PENDIENTES");
        Usuario responsable = this.obtenerUsuario(usuario);
        DireccionesDestino destinos = new DireccionesDestino(responsable.getEmail());
        enviarHtml(destinos, asunto, "emailPlanAccionPendiente", (context) -> {
            context.setVariable("nombreUsuario", responsable.getNombre());
            context.setVariable("planes", detalle);
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
        return MensajeTipo.RECORDATORIO;
    }
}


