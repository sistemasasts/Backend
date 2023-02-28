package com.isacore.quality.model.pnc;

public enum TipoDestino {
    DESPERDICIO("DESPERDICIO"),
    DONACION("DONACIÓN"),
    RETRABAJO("RETRABAJO"),
    REPROCESO("REPROCESO"),
    ;

    private String descripcion;

    TipoDestino(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
