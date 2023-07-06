package com.isacore.quality.repository.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ILoteRepo extends JpaRepository<Lote, Long> {
    Optional<Lote> findById(Long id);

    List<Lote> findByDesviacionRequisito(DesviacionRequisito desviacionRequisito);
}
