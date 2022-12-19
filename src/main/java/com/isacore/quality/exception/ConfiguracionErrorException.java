package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class ConfiguracionErrorException extends QualityException {

    private String mensaje;

    public ConfiguracionErrorException(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
	public String getMessage() {
		return mensaje;
	}
}
