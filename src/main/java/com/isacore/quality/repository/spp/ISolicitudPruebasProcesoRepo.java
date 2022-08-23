package com.isacore.quality.repository.spp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;

@Repository
public interface ISolicitudPruebasProcesoRepo extends JpaRepository<SolicitudPruebasProceso, Long> {

	List<SolicitudPruebasProceso> findByNombreSolicitanteOrderByFechaCreacionDesc(String nombreSolicitante);
	
	List<SolicitudPruebasProceso> findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(List<EstadoSolicitud> estados, String usuarioGestion);
	
	List<SolicitudPruebasProceso> findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud estado, String usuarioAprobador);
}
