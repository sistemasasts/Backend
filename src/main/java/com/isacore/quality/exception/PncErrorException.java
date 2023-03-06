package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class PncErrorException extends QualityException {

    private String mensaje;

    public PncErrorException(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
	public String getMessage() {
		return mensaje;
	}
}
