package com.isacore.quality.model.reclamoMP;

public enum ComplaintEstado {

    CREADA("CREADA"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADO"),
    ANULADO("ANULADO"),
    REGRESADO("REGRESADO"),
    PENDIENTE_APROBACION_CALIDAD("PENDIENTE APROBACIÓN CALIDAD"),
    PENDIENTE_APROBACION_COMPRAS("PENDIENTE APROBACIÓN COMPRAS"),
    CERRADO("CERRADO");

    private String descripcion;

    ComplaintEstado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
