package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class CardexErrorException extends QualityException {

    private String mensaje;

    public CardexErrorException(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
	public String getMessage() {
		return mensaje;
	}
}
