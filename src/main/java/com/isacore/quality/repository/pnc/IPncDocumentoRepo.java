package com.isacore.quality.repository.pnc;

import com.isacore.quality.model.pnc.PncDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPncDocumentoRepo extends JpaRepository<PncDocumento, Long> {

}
