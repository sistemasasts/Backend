package com.isacore.quality.repository.spp;

import java.util.List;

import com.isacore.quality.model.spp.EstadoSolicitudPP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;

@Repository
public interface ISolicitudPruebasProcesoRepo extends JpaRepository<SolicitudPruebasProceso, Long> {

	List<SolicitudPruebasProceso> findByNombreSolicitanteOrderByFechaCreacionDesc(String nombreSolicitante);
	
	List<SolicitudPruebasProceso> findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(List<EstadoSolicitudPP> estados, String usuarioGestion);
	
	List<SolicitudPruebasProceso> findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitudPP estado, String usuarioAprobador);

	List<SolicitudPruebasProceso> findByEstadoAndUsuarioValidadorOrderByFechaCreacionDesc(EstadoSolicitudPP estado, String usuarioAprobador);

	List<SolicitudPruebasProceso> findByEstadoAndUsuarioGestionPlanta(EstadoSolicitudPP estado, String usuarioPlanta);
	List<SolicitudPruebasProceso> findByEstadoAndUsuarioGestionCalidad(EstadoSolicitudPP estado, String usuarioCalidad);
	List<SolicitudPruebasProceso> findByEstadoAndUsuarioGestionMantenimiento(EstadoSolicitudPP estado, String usuarioMantenimiento);

	List<SolicitudPruebasProceso> findByEstadoAndUsuarioGestionCalidadJefe(EstadoSolicitudPP estado, String usuarioCalidadJefe);
	List<SolicitudPruebasProceso> findByEstadoAndUsuarioGestionMantenimientoJefe(EstadoSolicitudPP estado, String usuarioMantenimientoJefe);
}
