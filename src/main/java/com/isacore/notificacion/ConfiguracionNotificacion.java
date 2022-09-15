package com.isacore.notificacion;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.isacore.util.UtilidadesConfiguracion.verificarValor;

@Component
@ConfigurationProperties(prefix = "notificacion")
public class ConfiguracionNotificacion {

	private static final Log LOG = LogFactory.getLog(ConfiguracionNotificacion.class);
	
	private String imagenLogo;
	
	private String urlBase;
	
	@PostConstruct
	public void asegurarValores() {
		verificarValor("Imagen Logo", imagenLogo, LOG, true);
		verificarValor("URL Base", urlBase, LOG, true);
	}

	public String getImagenLogo() {
		return imagenLogo;
	}

	public String getUrlBase() {
		return urlBase;
	}

	public void setImagenLogo(String imagenLogo) {
		this.imagenLogo = imagenLogo;
	}

	public void setUrlBase(String urlBase) {
		this.urlBase = urlBase;
	}
}
