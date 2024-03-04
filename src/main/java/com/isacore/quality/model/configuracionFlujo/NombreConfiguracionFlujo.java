package com.isacore.quality.model.configuracionFlujo;

public enum NombreConfiguracionFlujo {
    TIEMPO_ENTREGA_INFORME("TIEMPO ENTREGA INFORME"),
    TIEMPO_VALIDAR_SOLICITUD_PP("TIEMPO VALIDAR SOLICITUD PRUEBAS EN PROCESO"),
    DIA_MAX_PERMITIDO_ENTREGAR_MUESTRAS("DIA MÁXIMO PERMITIDO ENTREGA DE MUESTRAS"),
    APROBADOR_SALIDA_MATERIAL("APROBADOR SALIDA MATERIAL"),
    NOTIFICAR_COMPRA_PRODUCTOS("NOTIFICAR COMPRA PRODUCTOS"),
    APROBADOR_DESVIACION_REQUISITO("APROBADOR DESVIACIÓN REQUISITO")
    ;

    private String descripcion;

    private NombreConfiguracionFlujo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
