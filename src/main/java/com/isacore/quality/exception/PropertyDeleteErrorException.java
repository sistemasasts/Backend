package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class PropertyDeleteErrorException extends QualityException {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "No se puede eliminar la propiedad, está asociada a varios productos";
	}
	

}
