package com.isacore.quality.service.impl.spp;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.spp.ConfiguracionFlujoPruebaProceso;
import com.isacore.quality.repository.spp.IConfiguracionFlujoPuebaProcesoRepo;
import com.isacore.quality.service.spp.IConfiguracionFlujoPruebaProceso;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracionFlujoPruebaProcesoServiceImpl implements IConfiguracionFlujoPruebaProceso {

	private static final Log LOG = LogFactory.getLog(ConfiguracionFlujoPruebaProcesoServiceImpl.class);
	
	@Autowired
	private IConfiguracionFlujoPuebaProcesoRepo repo;
	
	@Override
	public List<ConfiguracionFlujoPruebaProceso> findAll() {
		return (List<ConfiguracionFlujoPruebaProceso>) repo.findAll();
	}

	@Override
	public ConfiguracionFlujoPruebaProceso create(ConfiguracionFlujoPruebaProceso obj) {
		validarDuplicidad(obj);
		validarOrdenYTipoSolicitudUnico(obj);
		LOG.info(String.format("Configuración solicitud de prueba en proceso a guardar %s", obj));
		return repo.save(obj);
	}

	@Override
	public ConfiguracionFlujoPruebaProceso findById(ConfiguracionFlujoPruebaProceso id) {
		return repo.findById(id.getId()).orElse(null);
	}

	@Override
	public ConfiguracionFlujoPruebaProceso update(ConfiguracionFlujoPruebaProceso obj) {
		return null;
	}

	@Override
	public boolean delete(String id) {
		repo.deleteById(Integer.valueOf(id));
		return true;
	}
	
	private void validarDuplicidad(ConfiguracionFlujoPruebaProceso obj) {

		Optional<ConfiguracionFlujoPruebaProceso> optional = repo.findByOrdenAndUsuario_IdUser(
				obj.getOrden(), obj.getUsuario().getIdUser());
		if (optional.isPresent())
			throw new SolicitudEnsayoErrorException(
					String.format("Usuario %s con Rol %s ya está configurado.",
							obj.getUsuario().getIdUser(), obj.getOrden()));

	}
	
	private void validarOrdenYTipoSolicitudUnico(ConfiguracionFlujoPruebaProceso obj) {

		Optional<ConfiguracionFlujoPruebaProceso> optional = repo.findByOrden(obj.getOrden());
		if (optional.isPresent())
			throw new SolicitudEnsayoErrorException(
					String.format("Para el Rol %s ya está configurado.",
							obj.getOrden()));

	}

//	@Override
//	public ConfiguracionUsuarioRolEnsayo buscarConfiguracionValidarSolicitud() {
//		return repo.findByOrdenAndTipoSolicitud(OrdenFlujo.VALIDAR_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS).orElse(null);
//	}
//
//	@Override
//	public ConfiguracionUsuarioRolEnsayo buscarConfiguracionResponderSolicitud() {
//		return repo.findByOrdenAndTipoSolicitud(OrdenFlujo.RESPONDER_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS).orElse(null);
//	}
//
//	@Override
//	public ConfiguracionUsuarioRolEnsayo buscarConfiguracionAprobarInforme() {
//		return repo.findByOrdenAndTipoSolicitud(OrdenFlujo.APROBAR_INFORME, TipoSolicitud.SOLICITUD_ENSAYOS).orElse(null);
//	}

}
