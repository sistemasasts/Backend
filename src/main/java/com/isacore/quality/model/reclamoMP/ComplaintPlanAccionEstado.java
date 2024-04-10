package com.isacore.quality.model.reclamoMP;

public enum ComplaintPlanAccionEstado {

    CREADA("CREADA"),
    ASIGNADA("ASIGNADA"),
    REGRESADO("REGRESADO"),
    PENDIENTE_APROBACION("PENDIENTE APROBACIÓN"),
    FINALIZADO("FINALIZADO");

    private String descripcion;

    ComplaintPlanAccionEstado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
