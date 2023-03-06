package com.isacore.quality.model.pnc;

public enum PncOrdenFlujo {
    INGRESO_SALIDA_MATERIAL("INGRESO SALIDA MATERIAL"),
    APROBACION("APROBACIÓN"),
    ;

    private String descripcion;

    PncOrdenFlujo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
