package com.isacore.quality.model;

public enum ProviderStatus {

	VIGENTE("VIGENTE"),
	NO_VIGENTE("NO VIGENTE");
	
	private String descripcion;
	
	private ProviderStatus(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}
