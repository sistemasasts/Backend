package com.isacore.quality.model.spp;

public enum CondicionOperacionTipo {
    PRODUCCION("PRODUCCIÓN"),
    MANTENIMIENTO("MANTENIMIENTO");

    private String descripcion;

    CondicionOperacionTipo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
