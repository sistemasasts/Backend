package com.isacore.quality.service.se;

import java.util.List;

import com.isacore.quality.model.se.SolicitudPruebasProceso;
import com.isacore.util.CRUD;

public interface ISolicitudPruebasProcesoService extends CRUD<SolicitudPruebasProceso> {

	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioSolicitante();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioEnGestion();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioValidador();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioAprobador();
	
	SolicitudPruebasProceso buscarPorId(long id);
	
	boolean enviarSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean validarSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean responderSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean regresarSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean anularSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean rechazarSolicitud(SolicitudPruebasProceso solicitud);
}
