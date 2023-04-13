package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class HCCErrorException extends QualityException {

    private String mensaje;

    public HCCErrorException(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
	public String getMessage() {
		return mensaje;
	}
}
