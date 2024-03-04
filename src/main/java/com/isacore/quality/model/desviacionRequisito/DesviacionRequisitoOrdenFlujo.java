package com.isacore.quality.model.desviacionRequisito;

public enum DesviacionRequisitoOrdenFlujo {
    INGRESO("INGRESO"),
    APROBACION_GERENCIA_CALIDAD("APROBACIÓN GERENCIA I+D")
    ;

    private String descripcion;

    DesviacionRequisitoOrdenFlujo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
