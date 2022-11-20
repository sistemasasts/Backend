package com.isacore.quality.model.configuracionFlujo;

public enum NombreConfiguracionFlujo {
    TIEMPO_ENTREGA_INFORME("TIEMPO ENTREGA INFORME");

    private String descripcion;

    private NombreConfiguracionFlujo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}