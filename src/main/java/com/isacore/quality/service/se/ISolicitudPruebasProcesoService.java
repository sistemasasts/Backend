package com.isacore.quality.service.se;

import java.util.List;

import com.isacore.quality.model.se.ConsultaSolicitudDTO;
import com.isacore.quality.model.se.SolicitudDTO;
import com.isacore.quality.model.spp.SolicitudPPDTO;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import com.isacore.util.CRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISolicitudPruebasProcesoService extends CRUD<SolicitudPruebasProceso> {

	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioSolicitante();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioEnGestion();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioValidador();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioAprobador();
	
	SolicitudPruebasProceso buscarPorId(long id);
	
	boolean enviarSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean validarSolicitud(SolicitudPruebasProceso solicitud);
	
//	boolean responderSolicitud(SolicitudPruebasProceso solicitud);
	
//	boolean regresarSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean anularSolicitud(SolicitudPruebasProceso solicitud);
	
//	boolean rechazarSolicitud(SolicitudPruebasProceso solicitud);

	Page<SolicitudPPDTO> consultar(Pageable pageabe, ConsultaSolicitudDTO dto);
}
