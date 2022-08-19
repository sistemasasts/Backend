package com.isacore.quality.model;

public enum PropertyPeriodicity {
	
	DIARIA("Diaria"),
	SEMANAL("Semanal"),
	MENSUAL("Mensal"),
	TRIMESTRAL("Trimestral"),
	SEMESTRAL("Semestral"),
	ANUAL("Anual"),
	CADA_5_ANIOS("Cada 5 Años");
	
	private String descripcion;
	
	private PropertyPeriodicity(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

}
