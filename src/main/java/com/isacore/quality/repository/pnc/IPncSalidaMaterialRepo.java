package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.PncSalidaMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPncSalidaMaterialRepo extends JpaRepository<PncSalidaMaterial, Long> {

}
