package com.isacore.quality.model.se;

public enum OrdenFlujo {

	INGRESO_SOLICITUD("INGRESO SOLICITUD"),
	VALIDAR_SOLICITUD("VALIDAR SOLICITUD"),
    REVISION_INFORME("REVISIÓN INFORME"),
	RESPONDER_SOLICITUD("RESPONDER SOLICITUD"),
	APROBAR_INFORME("APROBACIÓN SOLICITUD"),
    SOLICITANTE_PLANES_ACCION("SOLICITANTE PLANES ACCIÓN"),
    SOLICITUD_PRUEBAS_PROCESO("SOLICITUD PRUEBAS PROCESO"),
    REVISION_PLANES_ACCION("REVISIÓN PLANES DE ACCIÓN");

	private String descripcion;

    OrdenFlujo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
