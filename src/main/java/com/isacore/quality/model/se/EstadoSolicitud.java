package com.isacore.quality.model.se;

public enum EstadoSolicitud {

	NUEVO,
	ENVIADO_REVISION,
	EN_PROCESO,
	REVISION_INFORME,
	FINALIZADO,
	RECHAZADO,
	REGRESADO_NOVEDAD_FORMA,
	REGRESADO_NOVEDAD_INFORME,
	ANULADO,
    PENDIENTE_APROBACION,
    PROCESO_FINALIZADO,
    PENDIENTE_PLANES_ACCION,
	PLANES_ACCION_REVISADOS,
    PENDIENTE_PRUEBAS_PROCESO,
    GESTION_PRUEBAS_PROCESO,
//TODO: Estados que puede tomar la solicitud despues de finalizar la prueba en proceso.
	ENVIAR_SOLUCIONES_TECNICAS,
	CREACION_MATERIA_PRIMA,
	GESTIONAR_IMPLEMENTAR_CAMBIOS,
	LIBRE_USO;
}
