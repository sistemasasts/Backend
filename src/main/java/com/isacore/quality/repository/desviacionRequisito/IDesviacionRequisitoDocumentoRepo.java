package com.isacore.quality.repository.desviacionRequisito;

import com.isacore.RepositorioBase;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoDocumento;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoOrdenFlujo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDesviacionRequisitoDocumentoRepo extends RepositorioBase<DesviacionRequisitoDocumento> {

    List<DesviacionRequisitoDocumento> findByOrdenAndDesviacionRequisito_Id(DesviacionRequisitoOrdenFlujo orden, long desviacionRequisitolId);

    boolean existsByDesviacionRequisito_IdAndOrden(long desviacionRequisitolId, DesviacionRequisitoOrdenFlujo orden);
}
