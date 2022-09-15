package com.isacore.notificacion.servicio;


import com.isacore.notificacion.ConfiguracionNotificacion;
import com.isacore.notificacion.dominio.DireccionesDestino;
import com.isacore.notificacion.dominio.Mensaje;
import com.isacore.notificacion.dominio.MensajeFormato;
import com.isacore.notificacion.dominio.MensajeTipo;
import org.apache.commons.logging.Log;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

public abstract class ServicioNotificacionBase {
	
	@FunctionalInterface
	protected interface AgregadorVariablesContexto {
		void agregarVariables(Context context);
	}
	
	private final ConfiguracionNotificacion configuracionNotificacion;
	
	private final Log log;
	
	private final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico;
	
	private final SpringTemplateEngine springTemplateEngine;
	
	public ServicioNotificacionBase(
			final ConfiguracionNotificacion configuracionNotificacion,
			final Log log,
			final ProveedorCorreoElectronicoOffice365 proveedorCorreoElectronico,
			final SpringTemplateEngine springTemplateEngine) {

		this.configuracionNotificacion = configuracionNotificacion;
		this.log = log;
		this.proveedorCorreoElectronico = proveedorCorreoElectronico;
		this.springTemplateEngine = springTemplateEngine;
	}
	
	protected void enviarHtml(DireccionesDestino direccionesDestino, String asunto, String template, AgregadorVariablesContexto agregadorVariablesContexto) {
		
		if (direccionesDestino.isTieneDireccionesA()) {

			try {

				final Context context = new Context();
				context.setVariable("imagenLogo", configuracionNotificacion.getImagenLogo());
				context.setVariable("urlBase", configuracionNotificacion.getUrlBase());
				agregadorVariablesContexto.agregarVariables(context);

				final String cuerpo = springTemplateEngine.process(template, context);

				proveedorCorreoElectronico.enviar(new Mensaje(
						tipo(),
						MensajeFormato.HTML,
						direccionesDestino,
						asunto,
						cuerpo));

			} catch (ProveedorCorreoElectronicoException e) {
				logErrorEnvio(direccionesDestino, asunto, e);
			}
		} else {
			logSinDirecciones(asunto);
		}
	}
	
	protected void enviarTexto(DireccionesDestino direccionesDestino, String asunto, String cuerpo) {
		if (direccionesDestino.isTieneDireccionesA()) {
			try {
				proveedorCorreoElectronico.enviar(new Mensaje(tipo(), MensajeFormato.TEXTO, direccionesDestino, asunto, cuerpo));
			} catch (ProveedorCorreoElectronicoException e) {
				logErrorEnvio(direccionesDestino, asunto, e);
			}
		} else {
			logSinDirecciones(asunto);
		}
	}

	protected ConfiguracionNotificacion getConfiguracionNotificacion() {
		return configuracionNotificacion;
	}

	protected Log getLog() {
		return log;
	}

	protected SpringTemplateEngine getSpringTemplateEngine() {
		return springTemplateEngine;
	}
	
	protected void logDebug(String mensaje) {
		if (log.isDebugEnabled()) {
			log.debug(mensaje);
		}
	}

	private void logErrorEnvio(DireccionesDestino direccionesDestino, String asunto, ProveedorCorreoElectronicoException e) {
		log.error(String.format("No se pudo enviar la notificación '%s' a %s", asunto, direccionesDestino), e);
	}
	
	private void logSinDirecciones(String asunto) {
		log.warn(String.format("No se pudo enviar notificación '%s' porque no tiene direcciones disponibles", asunto));
	}

	protected abstract MensajeTipo tipo();
}
