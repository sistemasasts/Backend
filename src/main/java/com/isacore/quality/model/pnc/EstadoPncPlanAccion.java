package com.isacore.quality.model.pnc;

public enum EstadoPncPlanAccion {
    ASIGNADO("ASIGNADO"),
    FINALIZADO("FINALIZADO"),
    PENDIENTE_REVISION("PENDIENTE REVISIÓN"),
    REGRESADO("REGRESADO");

    private String descripcion;

    EstadoPncPlanAccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
