package com.isacore.quality.repository.se;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isacore.quality.model.se.ConfiguracionUsuarioRolEnsayo;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.TipoSolicitud;

@Repository
public interface IConfiguracionUsuarioRolEnsayoRepo extends JpaRepository<ConfiguracionUsuarioRolEnsayo, Integer>{

	Optional<ConfiguracionUsuarioRolEnsayo> findByOrdenAndTipoSolicitudAndUsuario_IdUser(OrdenFlujo orden, TipoSolicitud tipoSolicitud, String idUsuario);
	
	Optional<ConfiguracionUsuarioRolEnsayo> findByOrdenAndTipoSolicitud(OrdenFlujo orden, TipoSolicitud tipoSolicitud);
}
