package com.isacore.quality.service.se;

import java.util.List;

import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.isacore.quality.model.se.ConsultaSolicitudDTO;
import com.isacore.quality.model.se.SolicitudDTO;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.util.CRUD;

public interface ISolicitudEnsayoService extends CRUD<SolicitudEnsayo> {

	List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioSolicitante();

	List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioEnGestion();

	List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioValidador();

	List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioAprobador();

	SolicitudEnsayo buscarPorId(long id);

	boolean enviarSolicitud(SolicitudEnsayo solicitud);

	boolean validarSolicitud(SolicitudEnsayo solicitud);

	boolean responderSolicitud(SolicitudEnsayo solicitud);

    boolean aprobarInforme(SolicitudEnsayo solicitud);

    boolean rechazarInforme(SolicitudEnsayo solicitud);

	boolean aprobarSolicitud(SolicitudEnsayo solicitud);

	boolean regresarSolicitud(SolicitudEnsayo solicitud);

	boolean anularSolicitud(SolicitudEnsayo solicitud);

	boolean rechazarSolicitud(SolicitudEnsayo solicitud);

	Page<SolicitudDTO> consultar(Pageable pageabe, ConsultaSolicitudDTO dto);

    SolicitudEnsayo crearSolicitudAPartirDeOtra(long solicitudId);

    boolean finalizarProceso(SolicitudEnsayo solicitud);

    boolean confirmarPlanesAccion(SolicitudEnsayo solicitud);

    SolicitudPruebasProceso iniciarPruebaEnProceso(SolicitudEnsayo solicitud);

    List<SolicitudEnsayo> obtenerSolicitudesPendientesPlanesAccion();
}
