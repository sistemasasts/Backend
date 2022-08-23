package com.isacore.quality.repository.spp;

import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudPruebaProcesoHistorialRepo extends JpaRepository<SolicitudPruebaProcesoHistorial, Long> {
	
	List<SolicitudPruebaProcesoHistorial> findBySolicitudPruebasProceso_IdOrderByFechaRegistroAsc(long solicitudId);
}
