package com.isacore.quality.service.se;

import com.isacore.quality.model.se.ConfiguracionUsuarioRolEnsayo;
import com.isacore.util.CRUD;

public interface IConfiguracionUsuarioRolEnsayo extends CRUD<ConfiguracionUsuarioRolEnsayo> {

	ConfiguracionUsuarioRolEnsayo buscarConfiguracionValidarSolicitud();
	
	ConfiguracionUsuarioRolEnsayo buscarConfiguracionResponderSolicitud();
	
	ConfiguracionUsuarioRolEnsayo buscarConfiguracionAprobarInforme();
}
