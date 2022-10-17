package com.isacore.quality.model.spp;

public enum EstadoSolicitudPP {
    NUEVO("NUEVO"),
    ENVIADO_REVISION("ENVIADO REVISIÓN"),
    EN_PROCESO("EN PROCESO"),
//    EN_PROCESO_PRODUCCION("EN PROCESO PRODUCCIÓN"),
    EN_PROCESO_MANTENIMIENTO("EN PROCESO MANTENIMIENTO"),
//    EN_PROCESO_CALIDAD,
    PRUEBA_NO_EJECUTADA("PRUEBA NO EJECUTADA"),
    PENDIENTE_APROBACION("PENDIENTE APROBACIÓN"),
    ANULADO("ANULADO"),
    RECHAZADO("RECHAZADO"),
    FINALIZADO("FINALIZADO"),
    //PRUEBA_NO_REALIZADA("PRUEBA NO REALIZADA"),
    PENDIENTE_AJUSTE_MAQUINARIA("PENDIENTE AJUSTE MAQUINARIA");

    private String descripcion;

    EstadoSolicitudPP(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
