package com.isacore.quality.model.pnc;

public enum LineaAfecta {

    LAMINACION_LINEA_1("LAMINACIÓN - LÍNEA 1"),
    LAMINACION_LINEA_2("LAMINACIÓN - LÍNEA 2"),
    REVESTIMIENTOS_LIQUIDOS_ASFALTICO("REVESTIMIENTOS LÍQUIDOS ASFÁLTICO"),
    REVESTIMIENTOS_LIQUIDOS_ACRILICOS("REVESTIMIENTOS LÍQUIDOS ACRÍLICOS"),
    CORTES_BANDAS("CORTES BANDAS"),
    CORTE_TEJAS("CORTE TEJAS"),
    METALES("METALES"),
    VIALES_EMULSIONES("VIALES - EMULSIONES"),
    VIALES_ASFALTO_MODIFICADO("VIALES - ASFALTO MODIFICADO"),
    VIALES_MEZCLA_EN_FRIO("VIALES - MEZCLA EN FRÍO")
    ;

    private String descripcion;

    LineaAfecta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
