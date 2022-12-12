package com.isacore.quality.model.se;

public enum TipoAprobacionSolicitud {

    NIVEL_LABORATORIO("NIVEL LABORATORIO"),
    NIVEL_PLANTA("NIVEL PLANTA"),
    GESTION_COMPRA("GESTIÓN COMPRA"),
	NO_APROBADO("NO APROBADO"),
    SOLICITUD_PRUEBA_PROCESO("SOLICITUD PRUEBA PROCESO");

    private String descripcion;

    TipoAprobacionSolicitud(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
