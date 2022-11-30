package com.isacore.quality.repository.configuracionFlujo;

import com.isacore.quality.model.configuracionFlujo.ConfiguracionAdjuntoRequerido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConfiguracionAdjuntoRequeridoRepo extends JpaRepository<ConfiguracionAdjuntoRequerido, Long>{

}
