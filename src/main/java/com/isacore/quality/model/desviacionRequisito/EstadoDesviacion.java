package com.isacore.quality.model.desviacionRequisito;

public enum EstadoDesviacion {
    NUEVO("NUEVO"),
    PENDIENTE_APROBACION("PENDIENTE APROBACIÓN"),
    APROBADO_CC("APROBADO CC"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADO"),
    ANULADO("ANULADO"),
    REGRESADO("REGRESADO"),
    ;

    private String descripcion;

    EstadoDesviacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
