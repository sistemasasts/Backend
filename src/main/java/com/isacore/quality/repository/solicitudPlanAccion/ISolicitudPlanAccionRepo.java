package com.isacore.quality.repository.solicitudPlanAccion;

import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.solicitudPlanAccion.SolicitudPlanAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudPlanAccionRepo extends JpaRepository<SolicitudPlanAccion, Long>{

    List<SolicitudPlanAccion> findByTipoSolicitudAndSolicitudIdOrderByFechaRegistro(TipoSolicitud tipo, long solicitudId);

}
