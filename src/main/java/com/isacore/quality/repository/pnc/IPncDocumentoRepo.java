package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.EstadoSalidaMaterial;
import com.isacore.quality.model.pnc.PncDocumento;
import com.isacore.quality.model.pnc.PncOrdenFlujo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IPncDocumentoRepo extends JpaRepository<PncDocumento, Long> {

    List<PncDocumento> findByEstadoInAndOrdenFlujoAndSalidaMaterialId(Collection<EstadoSalidaMaterial> estados, PncOrdenFlujo orden,
                                                                      long idSalidaMaterial);

    List<PncDocumento> findByOrdenFlujoAndSalidaMaterialIdAndPncPlanAccionId(PncOrdenFlujo orden, long idSalidaMaterial, long planAccionId);

    boolean existsBySalidaMaterialIdAndEstadoAndOrdenFlujo(long salidaMaterialId, EstadoSalidaMaterial estado, PncOrdenFlujo orden);
}
