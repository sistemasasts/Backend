package com.isacore.quality.model;

public enum PriorityLevel {

	ALTO("Alto"),
	MEDIO("Medio"),
	BAJO("Bajo");
	
	private String descripcion;
	
	private PriorityLevel(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}
