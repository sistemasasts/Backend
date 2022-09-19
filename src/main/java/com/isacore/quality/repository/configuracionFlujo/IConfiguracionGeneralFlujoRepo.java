package com.isacore.quality.repository.configuracionFlujo;

import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.se.TipoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IConfiguracionGeneralFlujoRepo extends JpaRepository<ConfiguracionGeneralFlujo, Long>{
	
	List<ConfiguracionGeneralFlujo> findByTipoSolicitud(TipoSolicitud tipoSolicitud);
	Optional<ConfiguracionGeneralFlujo> findByTipoSolicitudAndNombreConfiguracionFlujo(TipoSolicitud tipoSolicitud, NombreConfiguracionFlujo nombre);
}
