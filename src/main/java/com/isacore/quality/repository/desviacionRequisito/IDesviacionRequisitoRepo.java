package com.isacore.quality.repository.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IDesviacionRequisitoRepo extends JpaRepository<DesviacionRequisito, Long> {
    Optional<DesviacionRequisito> findById(Long id);

    @Query(value = "SELECT NEXT VALUE FOR desviacion_requisito_sequence", nativeQuery = true)
    Long generarSecuencial();
}
