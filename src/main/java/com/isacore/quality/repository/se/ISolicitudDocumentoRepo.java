package com.isacore.quality.repository.se;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudDocumento;

@Repository
public interface ISolicitudDocumentoRepo extends JpaRepository<SolicitudDocumento, Long> {

	List<SolicitudDocumento> findByEstadoInAndOrdenFlujoInAndSolicitudEnsayo_Id(List<EstadoSolicitud> estado, List<OrdenFlujo> orden, long solicitudId);

	List<SolicitudDocumento> findByOrdenFlujoInAndSolicitudEnsayo_Id(List<OrdenFlujo> orden, long solicitudId);
	
	boolean existsByEstadoAndOrdenFlujoAndSolicitudEnsayo_Id(EstadoSolicitud estado, OrdenFlujo orden, long solicitudId);
	
	boolean existsByOrdenFlujoAndSolicitudEnsayo_Id(OrdenFlujo orden, long solicitudId);

}
