package com.isacore.quality.model.desviacionRequisito;

public enum EstadoDesviacion {
    NUEVO("NUEVO"),
    PENDIENTE_APROBACION("PENDIENTE APROBACIÓN"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADO"),
    ANULADO("ANULADO");

    private String descripcion;

    EstadoDesviacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
