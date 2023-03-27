package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.EstadoPncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPncPlanAccionRepo extends JpaRepository<PncPlanAccion, Long> {

    List<PncPlanAccion> findBySalidaMaterial_Id(long salidaMaterialId);

    List<PncPlanAccion> findByEstadoAndEnTurnoOrderByFechaFin(EstadoPncPlanAccion estado, String enTurno);

    int deleteBySalidaMaterial_Id(long salidaMaterialId);

}
