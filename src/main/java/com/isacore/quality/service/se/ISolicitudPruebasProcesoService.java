package com.isacore.quality.service.se;

import java.util.List;

import com.isacore.quality.model.se.ConsultaSolicitudDTO;
import com.isacore.quality.model.se.SolicitudDTO;
import com.isacore.quality.model.spp.*;
import com.isacore.util.CRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISolicitudPruebasProcesoService extends CRUD<SolicitudPruebasProceso> {

	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioSolicitante();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioEnGestion();

	List<SolicitudPruebasProceso> obtenerSolicitudesPorAsignarResponsable(OrdenFlujoPP orden);
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioValidador();
	
	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioAprobador();

	List<SolicitudPruebasProceso> obtenerSolicitudesPorProcesar(OrdenFlujoPP orden);

	List<SolicitudPruebasProceso> obtenerSolicitudesPorAprobar(OrdenFlujoPP orden);
	
	SolicitudPruebasProceso buscarPorId(long id);
	
	boolean enviarSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean validarSolicitud(SolicitudPruebasProceso solicitud);

	boolean asignarResponsable(SolicitudPruebasProceso dto);
	
	boolean marcarComoPruebaNoRealizada(SolicitudPruebasProceso solicitud);

	boolean marcarComoPruebaRealizada(SolicitudPruebasProceso solicitud);

	void procesar(SolicitudPruebasProceso solicitud);

	void procesarAprobacion(AprobarSolicitudDTO dto);
	
//	boolean regresarSolicitud(SolicitudPruebasProceso solicitud);
	
	boolean anularSolicitud(SolicitudPruebasProceso solicitud);
	
//	boolean rechazarSolicitud(SolicitudPruebasProceso solicitud);

	Page<SolicitudPPDTO> consultar(Pageable pageabe, ConsultaSolicitudDTO dto);
}
