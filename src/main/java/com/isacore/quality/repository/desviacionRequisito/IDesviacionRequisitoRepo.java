package com.isacore.quality.repository.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.EstadoDesviacion;
import com.isacore.quality.model.pnc.EstadoSalidaMaterial;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IDesviacionRequisitoRepo extends JpaRepository<DesviacionRequisito, Long> {
    Optional<DesviacionRequisito> findById(Long id);

    @Query(value = "SELECT NEXT VALUE FOR desviacion_requisito_sequence", nativeQuery = true)
    Long generarSecuencial();

    List<DesviacionRequisito> findByEstadoIn(Collection<EstadoDesviacion> estados);

}
