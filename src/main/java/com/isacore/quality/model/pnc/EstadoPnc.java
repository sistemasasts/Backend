package com.isacore.quality.model.pnc;

public enum EstadoPnc {
    CREADO("CREADO"),
    EN_PROCESO("EN PROCESO"),
    FINALIZADO("FINALIZADO"),
    ANULADO("ANULADO");

    private String descripcion;

    EstadoPnc(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
