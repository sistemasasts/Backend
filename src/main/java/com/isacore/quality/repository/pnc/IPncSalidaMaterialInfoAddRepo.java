package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.PncSalidaMaterialInfoAdd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPncSalidaMaterialInfoAddRepo extends JpaRepository<PncSalidaMaterialInfoAdd, Long> {

}
