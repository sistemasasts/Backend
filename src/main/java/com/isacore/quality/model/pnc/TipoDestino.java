package com.isacore.quality.model.pnc;

public enum TipoDestino {
    DESPERDICIO("DAR DE BAJA"),
    DONACION("DONACIÓN"),
    RETRABAJO("RETRABAJO"),
    REPROCESO("REPROCESO"),
    SALIDA_CONCESION("SALIDA DE CONCESIÓN"),
    ;

    private String descripcion;

    TipoDestino(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
