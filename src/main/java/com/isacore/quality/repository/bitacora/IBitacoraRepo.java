package com.isacore.quality.repository.bitacora;

import com.isacore.quality.model.bitacora.Bitacora;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBitacoraRepo extends JpaRepository<Bitacora, Long> {

}
