package com.isacore.quality.repository.se;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.SolicitudEnsayo;

@Repository
public interface ISolicitudEnsayoRepo extends JpaRepository<SolicitudEnsayo, Long> {

	List<SolicitudEnsayo> findByNombreSolicitanteOrderByFechaCreacionDesc(String nombreSolicitante);
	
	List<SolicitudEnsayo> findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(List<EstadoSolicitud> estados, String usuarioGestion);
	
	List<SolicitudEnsayo> findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud estado, String usuarioValidador);
	
	List<SolicitudEnsayo> findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud estado, String usuarioAprobador);
}
