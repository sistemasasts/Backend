package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class UsuarioErrorException extends QualityException {

    private String mensaje;

    public UsuarioErrorException(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
	public String getMessage() {
		return mensaje;
	}
}
