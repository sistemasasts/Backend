package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.EstadoSalidaMaterial;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IPncSalidaMaterialRepo extends JpaRepository<PncSalidaMaterial, Long> {

    List<PncSalidaMaterial> findByProductoNoConforme_Id(long pncId);

    List<PncSalidaMaterial> findByPncDefecto_Id(long pncDefectoId);

    List<PncSalidaMaterial> findByProductoNoConforme_IdAndEstadoIn(long pncId, Collection<EstadoSalidaMaterial> estados);

    List<PncSalidaMaterial> findByEstadoIn(Collection<EstadoSalidaMaterial> estados);
}
