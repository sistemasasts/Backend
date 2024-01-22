package com.isacore.quality.model;

public enum ProductType {

	PRODUCTO_TERMINADO("Producto Terminado"),
	PRODUCTO_EN_PROCESO("Producto en Proceso"),
	PRODUCTO_MAQUILA("Producto Maquila"),
	MATERIA_PRIMA("Materia Prima"),
	SUMINISTROS_CIMI("Suministros - CIMI")
	;
	
	private String descripcion;
	
	private ProductType(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
}
