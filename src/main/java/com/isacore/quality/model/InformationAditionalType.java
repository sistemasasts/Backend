package com.isacore.quality.model;

public enum InformationAditionalType {
	CRITERIOS_APROBACION("Criterios de Aprobación"),
	CARATERISTICAS_PALETIZADO("Caraterísticas de Paletizado"),
	NACIONAL_IMPORTACION("Nacional/Importación");
	
	private String descripcion;
	
	private InformationAditionalType(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
