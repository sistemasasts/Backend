package com.isacore.quality.repository.disenoPavimento;

import com.isacore.quality.model.se.SolicitudEnsayoMinaAgregados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudEnsayoMinaAgregadoRepo extends JpaRepository<SolicitudEnsayoMinaAgregados, Long> {

}
