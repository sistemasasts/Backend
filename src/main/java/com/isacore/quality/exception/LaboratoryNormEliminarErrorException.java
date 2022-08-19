package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class LaboratoryNormEliminarErrorException extends QualityException {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "No se puede eliminar la norma, está asociada a varias propiedades";
	}
	

}
