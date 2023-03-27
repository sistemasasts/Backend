package com.isacore.quality.model.pnc;

public enum PncOrdenFlujo {
    INGRESO_SALIDA_MATERIAL("INGRESO SALIDA MATERIAL"),
    APROBACION("APROBACIÓN"),
    PROCESAR_PLAN_ACCION("PROCESAR PLAN ACCIÓN"),
    VALIDAR_PLAN_ACCION("VALIDAR PLAN ACCIÓN")
    ;

    private String descripcion;

    PncOrdenFlujo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
