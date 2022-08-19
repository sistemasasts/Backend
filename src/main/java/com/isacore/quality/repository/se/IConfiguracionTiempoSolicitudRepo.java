package com.isacore.quality.repository.se;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.ConfiguracionTiempoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.TiempoEntrega;
import com.isacore.quality.model.se.TipoSolicitud;

@Repository
public interface IConfiguracionTiempoSolicitudRepo extends JpaRepository<ConfiguracionTiempoSolicitud, Integer>{
	
	Optional<ConfiguracionTiempoSolicitud> findByOrdenAndTipoSolicitudAndTipoEntrega(OrdenFlujo orden, TipoSolicitud tipoSolicitud, TiempoEntrega tipoEntrega);
}
