package com.isacore.quality.repository.spp;

import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ISolicitudPruebaProcesoHistorialRepo extends JpaRepository<SolicitudPruebaProcesoHistorial, Long> {
	
	List<SolicitudPruebaProcesoHistorial> findBySolicitudPruebasProceso_IdOrderByFechaRegistroAsc(long solicitudId);
	List<SolicitudPruebaProcesoHistorial> findBySolicitudPruebasProceso_IdInOrderBySolicitudPruebasProcesoId(Collection<Long> solicitudIds);
}
