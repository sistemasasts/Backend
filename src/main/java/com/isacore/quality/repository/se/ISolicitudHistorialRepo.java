package com.isacore.quality.repository.se;

import java.util.Collection;
import java.util.List;

import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.SolicitudHistorial;

@Repository
public interface ISolicitudHistorialRepo extends JpaRepository<SolicitudHistorial, Long> {

	List<SolicitudHistorial> findBySolicitudEnsayo_IdOrderByFechaRegistroAsc(long solicitudId);
    List<SolicitudHistorial> findBySolicitudEnsayo_IdInOrderBySolicitudEnsayoId(Collection<Long> solicitudIds);

}
