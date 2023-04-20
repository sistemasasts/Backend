package com.isacore.quality.model.spp;

public enum TipoAprobacionPP {
    ENVIAR_SOLUCIONES_TECNICAS(true, "SOLICITUD SOLUCIONES TÉCNICAS"),
    CREACION_MATERIA_PRIMA(true, "CREACIÓN DE MATERIAS PRIMAS"),
    GESTIONAR_COMPRA(true,"GESTIONAR COMPRA"),
    GESTIONAR_IMPLEMENTAR_CAMBIOS(true, "GESTIONAR E IMPLEMENTAR CAMBIOS"),
    LIBRE_USO(true, "LIBRE USO"),
    REPETIR_PRUEBA(false, "REPETIR PRUEBA"),
    MATERIAL_NO_VALIDO(false, "MATERIAL NO VALIDADO"),
    AJUSTE_MAQUINARIA(false, "AJUSTE MAQUINARIA");

    private boolean aprobado;
    private String descripcion;

    TipoAprobacionPP( boolean aprobado, String descripcion){
        this.aprobado = aprobado;
        this.descripcion = descripcion;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
