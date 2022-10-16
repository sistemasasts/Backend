package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class SolicitudPruebaProcesoErrorException extends QualityException {

	private String mensaje;

	public SolicitudPruebaProcesoErrorException(String mensaje) {
		this.mensaje = mensaje;
	}


	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return this.mensaje;
	}
	

}
