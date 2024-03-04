package com.isacore.quality.model.se;

public enum TipoSolicitud {

	SOLICITUD_ENSAYOS ("SOLICITUD ENSAYOS"),
	SOLICITUD_PRUEBAS_EN_PROCESO("SOLICITUD PRUEBAS EN PROCESO"),
    SALIDA_MATERIAL("SALIDA MATERIAL"),
    INVENTARIO_PRODUCTO("INVENTARIO PRODUCTO"),
    DESVIACION_REQUISITO("DESVIACIÓN REQUISITO");

	private String descripcion;

    TipoSolicitud(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
