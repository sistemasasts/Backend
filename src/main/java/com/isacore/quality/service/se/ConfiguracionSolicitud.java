package com.isacore.quality.service.se;

import static com.isacore.util.UtilidadesConfiguracion.verificarValor;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "solicitud")
public class ConfiguracionSolicitud {

private static final Log LOG = LogFactory.getLog(ConfiguracionSolicitud.class);
	
	private String rutaBase;
	
	@PostConstruct
	public void asegurarValores() {
		verificarValor("Sistema ISA Solicitudes Ruta Base", rutaBase, LOG, true);
	}

	public String getRutaBase() {
		return rutaBase;
	}

	public void setRutaBase(String rutaBase) {
		this.rutaBase = rutaBase;
	}
	
	
}
