package com.isacore.quality.model.spp;

public enum OrdenFlujoPP {

	INGRESO_SOLICITUD("INGRESO DE SOLICITUD"),
	VALIDAR_SOLICITUD("VALIDAR SOLICITUD"),
	MANTENIMIENTO("MANTENIMIENTO"),
	CALIDAD("CALIDAD"),
	ASIGNAR_RESPONSABLE("ASGINAR RESPONSABLE"),
	PRODUCCION("PRODUCCIÓN"),
	APROBAR_SOLICITUD("APROBAR SOLICITUD"),
	AJUSTE_MAQUINARIA("AJUSTE DE MAQUINARIA");

	private String descripcion;

	private OrdenFlujoPP(String descripcion){
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
