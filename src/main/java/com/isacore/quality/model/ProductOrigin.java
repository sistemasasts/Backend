package com.isacore.quality.model;

public enum ProductOrigin {

	NACIONAL("Nacional"),
	IMPORTACION("Importación"),
	NACIONAL_IMPORTACION("Nacional/Importación");
	
	private String descripcion;
	
	private ProductOrigin(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}
