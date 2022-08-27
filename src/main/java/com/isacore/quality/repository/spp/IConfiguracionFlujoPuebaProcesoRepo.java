package com.isacore.quality.repository.spp;

import com.isacore.quality.model.spp.ConfiguracionFlujoPruebaProceso;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IConfiguracionFlujoPuebaProcesoRepo extends JpaRepository<ConfiguracionFlujoPruebaProceso, Integer>{

	Optional<ConfiguracionFlujoPruebaProceso> findByOrdenAndUsuario_IdUser(OrdenFlujoPP orden, String idUsuario);
	
	Optional<ConfiguracionFlujoPruebaProceso> findByOrden(OrdenFlujoPP orden);
}
