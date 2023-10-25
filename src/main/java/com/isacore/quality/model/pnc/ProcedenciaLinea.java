package com.isacore.quality.model.pnc;

public enum ProcedenciaLinea {
    PRODUCCION("PRODUCCIÓN"),
    BODEGA_INGA("BODEGA INGA"),
    BODEGA_CASHAPAMBA("BODEGA CASHAPAMBA"),
    BODEGA_VIA_LA_COSTA("BODEGA VÍA A LA COSTA"),
    BODEGA_LA_NAVAL("BODEGA LA NAVAL"),
    CANAL_MAYORISTA("CANAL MAYORISTA"),
    CUENTAS_CLAVES_ESPECIFICAR("CUENTAS CLAVES"),
    EXPORTACIONES("EXPORTACIONES "),
    PROVEEDOR_MAQUILA("PROVEEDOR MAQUILA"),
    VIALES("VIALES");


    private String descripcion;

    ProcedenciaLinea(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
