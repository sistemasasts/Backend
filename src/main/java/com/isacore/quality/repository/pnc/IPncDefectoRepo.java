package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.PncDefecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPncDefectoRepo extends JpaRepository<PncDefecto, Long> {


}
