package com.isacore.quality.service.impl.se;

import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.ConfiguracionUsuarioRolEnsayo;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.Secuencial;
import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.model.se.SolicitudPruebasProceso;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.se.IConfiguracionUsuarioRolEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.repository.se.ISolicitudPruebasProcesoRepo;
import com.isacore.quality.service.se.ISolicitudPruebasProcesoService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;

@Service
public class SolicitudPruebasProcesoServiceImpl implements ISolicitudPruebasProcesoService {

	private static final Log LOG = LogFactory.getLog(SolicitudPruebasProcesoServiceImpl.class);
	
	@Autowired
	private ISolicitudPruebasProcesoRepo repo;
	
	@Autowired
	private IConfiguracionUsuarioRolEnsayoRepo repoConfiguracion;
	
	@Autowired
	private ISolicitudHistorialRepo repoHistorial;
	
	@Autowired
	private IUserImptekRepo repoUsuario;
	
	@Autowired
	private SecuencialServiceImpl secuencialService;
	
	@Override
	public List<SolicitudPruebasProceso> findAll() {
		return repo.findAll();
	}

	@Override
	public SolicitudPruebasProceso create(SolicitudPruebasProceso obj) {
		Secuencial secuencial = secuencialService.ObtenerSecuencialPorTipoSolicitud(TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO);
		
		SolicitudPruebasProceso nuevo = new SolicitudPruebasProceso(
				secuencial.getNumeroSecuencial(),
				obj.getFechaEntrega(), 
				obj.getLineaAplicacion(), 
				obj.getMotivo(), 
				obj.getMotivoOtro(), 
				obj.getMaterialLineaProceso(), 
				obj.getMaterialLineaProcesoOtro(),
				obj.getDescripcionProducto(), 
				obj.getVariablesProceso(), 
				obj.getVerificacionAdicional(),
				obj.getObservacion(),				
				nombreUsuarioEnSesion());
		
		LOG.info(String.format("Solicitud Prueba en proceso a guardar %s", nuevo));
		return repo.save(nuevo);
	}

	@Override
	public SolicitudPruebasProceso findById(SolicitudPruebasProceso id) {
		// TODO Auto-generated method stub
		return repo.findById(id.getId()).orElse(null);
	}

	@Override
	public SolicitudPruebasProceso update(SolicitudPruebasProceso obj) {
		return repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioSolicitante() {
		return repo.findByNombreSolicitanteOrderByFechaCreacionDesc(nombreUsuarioEnSesion());
	}

	@Override
	public List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioEnGestion() {
		return repo.findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(Arrays.asList(EstadoSolicitud.EN_PROCESO, EstadoSolicitud.REGRESADO_NOVEDAD_INFORME), nombreUsuarioEnSesion());
	}
	
	@Override
	public List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioAprobador() {
		return repo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud.REVISION_INFORME, nombreUsuarioEnSesion());
	}

	@Override
	public SolicitudPruebasProceso buscarPorId(long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public boolean enviarSolicitud(SolicitudPruebasProceso solicitud) {
		
		Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
		Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.VALIDAR_SOLICITUD, TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO);
		if(!configuracionOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.VALIDAR_SOLICITUD));
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.INGRESO_SOLICITUD, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoEnviada(configuracionOP.get().getUsuarioId());
		
		LOG.info(String.format("Solicitud id=%s enviada..", solicitudRecargada.getId()));
		return true;
	}
	
	
	private void agregarHistorial(SolicitudPruebasProceso solicitud, OrdenFlujo orden, String observacion) {
		String usuario = nombreUsuarioEnSesion();
		Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
		SolicitudHistorial historial = new SolicitudHistorial(solicitud,orden, usuarioOp.get(), observacion);
		repoHistorial.save(historial);
		LOG.info(String.format("Historial guardado %s", historial));		
	}

	@Override
	public List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioValidador() {
		return repo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud.ENVIADO_REVISION ,nombreUsuarioEnSesion());
	}

	@Override
	@Transactional
	public boolean validarSolicitud(SolicitudPruebasProceso solicitud) {
		Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
		Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.RESPONDER_SOLICITUD, TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO);
		if(!configuracionOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.RESPONDER_SOLICITUD));
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.VALIDAR_SOLICITUD, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoValidada(configuracionOP.get().getUsuarioId());		
		
		LOG.info(String.format("Solicitud id=%s validada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean responderSolicitud(SolicitudPruebasProceso solicitud) {
		Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
		Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.APROBAR_INFORME, TipoSolicitud.SOLICITUD_ENSAYOS);
		if(!configuracionOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.APROBAR_INFORME));
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.RESPONDER_SOLICITUD, solicitud.getObservacion());
		
		solicitudRecargada.setEstado(EstadoSolicitud.FINALIZADO);
		solicitudRecargada.finalizarSolicitud();		
		
		LOG.info(String.format("Solicitud id=%s respondida..", solicitudRecargada.getId()));
		return true;
	}


	@Override
	@Transactional
	public boolean regresarSolicitud(SolicitudPruebasProceso solicitud) {
		Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
		
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoRegresada();		
		
		LOG.info(String.format("Solicitud id=%s regresada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean anularSolicitud(SolicitudPruebasProceso solicitud) {
		Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
		
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, solicitudRecargada.getOrden(), solicitud.getObservacion());
		
		solicitudRecargada.anular();		
		
		LOG.info(String.format("Solicitud id=%s anulada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean rechazarSolicitud(SolicitudPruebasProceso solicitud) {
		Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
		
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, solicitudRecargada.getOrden(), solicitud.getObservacion());
		
		solicitudRecargada.rechazar();		
		
		LOG.info(String.format("Solicitud id=%s rechazada..", solicitudRecargada.getId()));
		return true;
	}


	

}
