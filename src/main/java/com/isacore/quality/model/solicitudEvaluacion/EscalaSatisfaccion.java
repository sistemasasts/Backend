package com.isacore.quality.model.solicitudEvaluacion;

public enum EscalaSatisfaccion {
    MUY_MALO("MUY MALO"),
    MALO("MALO"),
    REGULAR("REGULAR"),
    BUENO("BUENO"),
    MUY_BUENO("MUY BUENO");

    private String descripcion;

    EscalaSatisfaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
