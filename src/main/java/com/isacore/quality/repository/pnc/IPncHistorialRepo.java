package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.PncHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPncHistorialRepo extends JpaRepository<PncHistorial, Long> {

    List<PncHistorial> findBySalidaMaterialIdOrderByFechaRegistroAsc(long salidaMaterialId);

    List<PncHistorial> findByPncIdOrderByFechaRegistroAsc(long pncId);
}
