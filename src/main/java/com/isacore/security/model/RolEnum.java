package com.isacore.security.model;

public enum RolEnum {
    ADMINISTRADOR_SISTEMA("ADMINISTRADOR SISTEMA"),
    ADMINISTRADOR("ADMINISTRADOR"),
    CALIDAD("JEFE"),
    COMPRAS("COMPRAS"),
    GERENCIA("GERENCIA"),
    COMERCIAL("COMERCIAL"),
    PLANTA("PLANTA"),
    OPERACIONES("OPERACIONES"),
    MANTENIMIENTO("MANTENIMIENTO"),
    JEFATURA_PLANTA("JEFATURA PLANTA"),
    JEFATURA_PROYECTOS_MANTENIMIENTO("JEFATURA PROYECTOS MANTENIMIENTO"),

    CAJERO("CAJERO"),
    OPERADOR("OPERADOR"),
    SUPERVISOR_CAJA("SUPERVISOR_CAJA");
    private final String descripcion;

    RolEnum(String descripcion){
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
