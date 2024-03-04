package com.isacore.quality.repository.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDesviacionRequisitoHistorialRepo extends JpaRepository<DesviacionRequisitoHistorial, Long> {

    List<DesviacionRequisitoHistorial> findByDesviacionRequisito_IdOrderByFechaRegistroAsc(long desviacionRequisitoId);

}
