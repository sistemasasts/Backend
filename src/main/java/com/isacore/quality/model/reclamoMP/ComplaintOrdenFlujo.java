package com.isacore.quality.model.reclamoMP;

public enum ComplaintOrdenFlujo {
    INGRESO("INGRESO"),
    APROBACION_CALIDAD("APROBACIÓN CALIDAD"),
    GESTION_CALIDAD("GESTIÓN CALIDAD"),
    GESTION_PLANES_ACCION("GESTIÓN PLANES ACCIÓN"),
    APROBACION_COMPRAS("APROBACIÓN COMPRAS");

    private String descripcion;

    ComplaintOrdenFlujo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
