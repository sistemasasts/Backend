package com.isacore.quality.model.pnc;

public enum TipoDestino {
    DESPERDICIO("DAR DE BAJA", false ,1),
    DONACION("DONACIÓN",false, 1),
    RETRABAJO("RETRABAJO",true, 1),
    REPROCESO("REPROCESO",true, 1),
    SALIDA_CONCESION("SALIDA DE CONCESIÓN",false, 3),
    TRASPASO_PRODUCTO_TERMINADO("TRASPASO PRODUCTO TERMINADO",true, 2),
    TRASPASO_PRODUCTO_PROCESO("TRASPASO PRODUCTO PROCESO",true, 2),
    RECLASIFICACION("RECLASIFICACIÓN",true, 2),
    ;

    private String descripcion;
    private int grupoInfoAdicional;
    private boolean planesAccion;

    TipoDestino(String descripcion, boolean planesAccion , int grupoInfoAdicional) {
        this.descripcion = descripcion;
        this.planesAccion = planesAccion;
        this.grupoInfoAdicional = grupoInfoAdicional;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isPlanesAccion() {
        return planesAccion;
    }

    public int getGrupoInfoAdicional() {
        return grupoInfoAdicional;
    }
}
