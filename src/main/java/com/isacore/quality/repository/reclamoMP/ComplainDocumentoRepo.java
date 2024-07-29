package com.isacore.quality.repository.reclamoMP;

import com.isacore.RepositorioBase;
import com.isacore.quality.model.reclamoMP.ComplaintDocumento;
import com.isacore.quality.model.reclamoMP.ComplaintOrdenFlujo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplainDocumentoRepo extends RepositorioBase<ComplaintDocumento> {

    boolean existsBySolicitudIdAndOrden(long solicitudid, String orden);

    List<ComplaintDocumento> findByOrdenAndSolicitudId(String orden, long solicitudId);

    List<ComplaintDocumento> findByOrdenAndSolicitudIdAndPlanAccionId(String orden, long solicitudId, long planAccionId);

}
