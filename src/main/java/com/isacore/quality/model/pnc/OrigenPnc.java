package com.isacore.quality.model.pnc;

public enum OrigenPnc {
    NACIONAL("Nacional"),
    IMPORTACION("Importación")
    ;
    private String descripcion;

    private OrigenPnc(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
