package com.isacore.security.model;

public enum MenuTipoEnum {
    MENU("MENU"),
    SUBMENU("SUBMENU");
    private final String descripcion;

    MenuTipoEnum(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
