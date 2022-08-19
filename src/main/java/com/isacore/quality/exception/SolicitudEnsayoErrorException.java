package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class SolicitudEnsayoErrorException extends QualityException {

	private String mensaje;
	
	public SolicitudEnsayoErrorException(String mensaje) {		
		this.mensaje = mensaje;
	}


	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return this.mensaje;
	}
	

}
