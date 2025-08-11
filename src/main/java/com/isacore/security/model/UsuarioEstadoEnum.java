package com.isacore.security.model;

public enum UsuarioEstadoEnum {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO"),
    SUSPENDIDO("SUSPENDIDO"),
    BLOQUEADO("BLOQUEADO");
    private final String descripcion;

    UsuarioEstadoEnum(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
