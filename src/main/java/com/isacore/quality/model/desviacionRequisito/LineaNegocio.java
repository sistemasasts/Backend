package com.isacore.quality.model.desviacionRequisito;

public enum LineaNegocio {
    CONSTRUCCION_LIVIANA("CONSTRUCCIÓN LIVIANA"),
    IMPERMEABILIZACION("IMPERMEABILIZACIÓN"),
    VIALES("VIALES"),
    ;

    private String descripcion;

    LineaNegocio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
