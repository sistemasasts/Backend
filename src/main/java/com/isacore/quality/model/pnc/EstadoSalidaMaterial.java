package com.isacore.quality.model.pnc;

public enum EstadoSalidaMaterial {

    CREADO("CREADO"),
    PENDIENTE_APROBACION("PENDIENTE APROBACIÓN"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADA"),
    CERRADO("CERRADO"),
    ;

    private String descripcion;

    EstadoSalidaMaterial(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
