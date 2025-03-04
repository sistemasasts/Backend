package com.isacore.quality.model.se;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoAprobacionSolicitud {

    NIVEL_LABORATORIO("NIVEL LABORATORIO", false),
    NIVEL_PLANTA("NIVEL PLANTA", false),
    GESTION_COMPRA("GESTIÓN COMPRA", false),
	NO_APROBADO("NO APROBADO", false),
    SOLICITUD_PRUEBA_PROCESO("SOLICITUD PRUEBA PROCESO", false),
    LIBRE_USO_GESTION_COMPRA("LIBRE USO GESTIÓN COMPRA", true),
    VALIDACION_NO_APROBADA("VALIDACIÓN NO APROBADA", true),
    REQUIERE_PRUEBA_PROCESO("REQUIERE PRUEBA EN PROCESO", true),
    ;

    private String descripcion;
    private boolean activoParaSeleccion;

    TipoAprobacionSolicitud(String descripcion, boolean activo) {
        this.descripcion = descripcion;
        this.activoParaSeleccion = activo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isActivoParaSeleccion() {
        return activoParaSeleccion;
    }

    public static List<TipoAprobacionSolicitud> soloActivosParaSeleccion(){
        return Arrays.stream(values())
                .filter(TipoAprobacionSolicitud::isActivoParaSeleccion)
                .collect(Collectors.toList());
    }
        
}
