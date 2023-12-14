package com.isacore.quality.model.pnc;

public enum TipoInfoAdd {
    PRODUCTO_RECUPERADO("PRODUCTO RECUPERADO", 1),
    PRODUCTO_APROBADO("PRODUCTO APROBADO", 1),
    PRODUCTO_NO_CONFORME("PRODUCTO NO CONFORME", 1),
    DESPERDICIO("DESPERDICIO", 1),
    LOTES("LOTES", 2),
    ;

    private String descripcion;
    private int grupo;

    TipoInfoAdd(String descripcion, int grupo) {
        this.descripcion = descripcion;
        this.grupo = grupo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getGrupo() {
        return grupo;
    }
}
