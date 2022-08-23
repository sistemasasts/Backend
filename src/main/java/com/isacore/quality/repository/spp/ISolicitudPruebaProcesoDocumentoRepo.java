package com.isacore.quality.repository.spp;

import com.isacore.quality.model.spp.EstadoSolicitudPP;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudPruebaProcesoDocumentoRepo extends JpaRepository<SolicitudPruebaProcesoDocumento, Long> {
	
	List<SolicitudPruebaProcesoDocumento> findByEstadoInAndOrdenFlujoInAndSolicitudPruebasProceso_Id(List<EstadoSolicitudPP> estado, List<OrdenFlujoPP> orden, long solicitudId);
	
	boolean existsByEstadoAndOrdenFlujoAndSolicitudPruebasProceso_Id(EstadoSolicitudPP estado, OrdenFlujoPP orden, long solicitudId);
	
	boolean existsByOrdenFlujoAndSolicitudPruebasProceso_Id(OrdenFlujoPP orden, long solicitudId);
}
