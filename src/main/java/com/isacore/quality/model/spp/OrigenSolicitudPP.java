package com.isacore.quality.model.spp;

public enum OrigenSolicitudPP {
    DESARROLLO_PRODUCTOS("Desarrollo de Productos"),
    REINGENIERIA("Reingeniería");

    private String descripcion;

    OrigenSolicitudPP(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
