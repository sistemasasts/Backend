package com.isacore.quality.repository.reclamoMP;

import com.isacore.quality.model.reclamoMP.ComplaintHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintHistorialRepo extends JpaRepository<ComplaintHistorial, Long> {
    List<ComplaintHistorial> findBySolicitudIdOrderByFechaRegistroAsc(long solicitudId);
}
