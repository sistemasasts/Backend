package com.isacore.quality.service.se;

import com.isacore.quality.model.spp.*;
import com.isacore.util.CRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ISolicitudPruebasProcesoService extends CRUD<SolicitudPruebasProceso> {

	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioSolicitante();

	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioEnGestion();

	List<SolicitudPruebasProceso> obtenerSolicitudesPorAsignarResponsable(OrdenFlujoPP orden);

	List<SolicitudPruebasProceso> obtenerSolicitudesPorReasignarResponsable(OrdenFlujoPP orden);

	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioValidador();

	List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioAprobador();

	List<SolicitudPruebasProceso> obtenerSolicitudesPorProcesar(OrdenFlujoPP orden);

	List<SolicitudPruebasProceso> obtenerSolicitudesPorAprobar(OrdenFlujoPP orden);

	SolicitudPruebasProceso buscarPorId(long id);

	boolean enviarSolicitud(SolicitudPruebasProceso solicitud);

	boolean validarSolicitud(SolicitudPruebasProceso solicitud);

	boolean asignarResponsable(SolicitudPruebasProceso dto);

	boolean reasignarResponsable(SolicitudPruebasProceso dto);

	boolean marcarComoPruebaNoRealizada(SolicitudPruebasProceso solicitud);

	boolean marcarComoPruebaNoRealizadaDefinitiva(SolicitudPruebasProceso solicitud);

	void procesar(SolicitudPruebasProceso solicitud);

	void procesarAprobacion(AprobarSolicitudDTO dto);

	boolean regresarSolicitudNovedadForma(SolicitudPruebasProceso solicitud);

	boolean anularSolicitud(SolicitudPruebasProceso solicitud);

	boolean rechazarSolicitud(SolicitudPruebasProceso solicitud);

	Page<SolicitudPPDTO> consultar(Pageable pageabe, ConsultaSolicitudPPDTO dto);

	byte[] generateReporte(long id);

	SolicitudPruebasProceso crearSolicitudParaRepetirPrueba(long solicitudId);

	SolicitudPruebasProceso agregarMaterialFormula(long solicitudId, MaterialFormula materialFormula);

	SolicitudPruebasProceso editarMaterialFormula(long solicitudId, MaterialFormula materialFormula);

	SolicitudPruebasProceso eliminarMaterialFormula(long solicitudId, long materialFormulaId);
}
