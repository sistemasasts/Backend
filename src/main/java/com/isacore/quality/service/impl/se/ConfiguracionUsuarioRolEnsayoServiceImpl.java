package com.isacore.quality.service.impl.se;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.ConfiguracionUsuarioRolEnsayo;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.se.IConfiguracionUsuarioRolEnsayoRepo;
import com.isacore.quality.service.se.IConfiguracionUsuarioRolEnsayo;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ConfiguracionUsuarioRolEnsayoServiceImpl implements IConfiguracionUsuarioRolEnsayo {

	private static final Log LOG = LogFactory.getLog(ConfiguracionUsuarioRolEnsayoServiceImpl.class);
	
	@Autowired
	private IConfiguracionUsuarioRolEnsayoRepo repo;
	
	@Override
	public List<ConfiguracionUsuarioRolEnsayo> findAll() {
		return (List<ConfiguracionUsuarioRolEnsayo>) repo.findAll();
	}

	@Override
	public ConfiguracionUsuarioRolEnsayo create(ConfiguracionUsuarioRolEnsayo obj) {
		validarDuplicidad(obj);
		validarOrdenYTipoSolicitudUnico(obj);
		LOG.info(String.format("Configuración solicitud de ensayo a guardar %s", obj));
		return repo.save(obj);
	}

	@Override
	public ConfiguracionUsuarioRolEnsayo findById(ConfiguracionUsuarioRolEnsayo id) { 
		return repo.findById(id.getId()).orElse(null);
	}

	@Transactional
	@Override
	public ConfiguracionUsuarioRolEnsayo update(ConfiguracionUsuarioRolEnsayo obj) {
		ConfiguracionUsuarioRolEnsayo configuracionUsuarioRolEnsayo = this.repo.findById(obj.getId()).orElseThrow(() -> new SolicitudEnsayoErrorException("configuración no encontrada"));
		configuracionUsuarioRolEnsayo.setUsuario(obj.getUsuario());
		log.info("Actualizando la condifiuracion de solicitud de ensayos.");
		return configuracionUsuarioRolEnsayo;
	}

	@Override
	public boolean delete(String id) {
		repo.deleteById(Integer.valueOf(id));
		return true;
	}
	
	private void validarDuplicidad(ConfiguracionUsuarioRolEnsayo obj) {

		Optional<ConfiguracionUsuarioRolEnsayo> optional = repo.findByOrdenAndTipoSolicitudAndUsuario_NombreUsuario(
				obj.getOrden(), obj.getTipoSolicitud(), obj.getUsuario().getNombreUsuario());
		if (optional.isPresent())
			throw new SolicitudEnsayoErrorException(
					String.format("Usuario %s con Rol %s para %s ya está configurado.",
							obj.getUsuario().getNombreUsuario(), obj.getOrden(), obj.getTipoSolicitud()));

	}
	
	private void validarOrdenYTipoSolicitudUnico(ConfiguracionUsuarioRolEnsayo obj) {

		Optional<ConfiguracionUsuarioRolEnsayo> optional = repo.findByOrdenAndTipoSolicitud(
				obj.getOrden(), obj.getTipoSolicitud());
		if (optional.isPresent())
			throw new SolicitudEnsayoErrorException(
					String.format("Para el Rol %s en %s ya está configurado.",
							obj.getOrden(), obj.getTipoSolicitud()));

	}

	@Override
	public ConfiguracionUsuarioRolEnsayo buscarConfiguracionValidarSolicitud() {
		return repo.findByOrdenAndTipoSolicitud(OrdenFlujo.VALIDAR_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS).orElse(null);
	}

	@Override
	public ConfiguracionUsuarioRolEnsayo buscarConfiguracionResponderSolicitud() {
		return repo.findByOrdenAndTipoSolicitud(OrdenFlujo.RESPONDER_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS).orElse(null);
	}

	@Override
	public ConfiguracionUsuarioRolEnsayo buscarConfiguracionAprobarInforme() {
		return repo.findByOrdenAndTipoSolicitud(OrdenFlujo.APROBAR_INFORME, TipoSolicitud.SOLICITUD_ENSAYOS).orElse(null);
	}

}
