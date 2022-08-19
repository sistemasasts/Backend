package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class ProveedorEliminarErrorException extends QualityException {

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "No se puede eliminar el proveedor, está asociado a varios productos";
	}
	

}
