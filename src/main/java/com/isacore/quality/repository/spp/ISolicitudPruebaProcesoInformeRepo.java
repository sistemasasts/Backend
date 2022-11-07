package com.isacore.quality.repository.spp;

import com.isacore.quality.model.spp.SolicitudPruebaProcesoInforme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ISolicitudPruebaProcesoInformeRepo extends JpaRepository<SolicitudPruebaProcesoInforme, Long> {

    Optional<SolicitudPruebaProcesoInforme> findBySolicitudPruebasProceso_Id(Long solicitudId);
}
