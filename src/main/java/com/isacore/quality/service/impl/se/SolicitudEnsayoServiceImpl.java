package com.isacore.quality.service.impl.se;

import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;
import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.ConfiguracionTiempoSolicitud;
import com.isacore.quality.model.se.ConfiguracionUsuarioRolEnsayo;
import com.isacore.quality.model.se.ConsultaSolicitudDTO;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.Secuencial;
import com.isacore.quality.model.se.SolicitudDTO;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.model.se.SolicitudPruebasProceso;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.se.IConfiguracionTiempoSolicitudRepo;
import com.isacore.quality.repository.se.IConfiguracionUsuarioRolEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.service.se.ISolicitudDocumentoService;
import com.isacore.quality.service.se.ISolicitudEnsayoService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;

@Service
public class SolicitudEnsayoServiceImpl implements ISolicitudEnsayoService {

	private static final Log LOG = LogFactory.getLog(SolicitudEnsayoServiceImpl.class);
	
	@Autowired
	private ISolicitudEnsayoRepo repo;
	
	@Autowired
	private IConfiguracionUsuarioRolEnsayoRepo repoConfiguracion;
	
	@Autowired
	private ISolicitudHistorialRepo repoHistorial;
	
	@Autowired
	private IUserImptekRepo repoUsuario;
	
	@Autowired
	private ISolicitudDocumentoService documentoServicio;

	@Autowired
	private SecuencialServiceImpl secuencialService;
	
	@Autowired
	private IConfiguracionTiempoSolicitudRepo repoConfiguracionTiempo;
	
	@Autowired
    private EntityManager entityManager;
	
	@Override
	public List<SolicitudEnsayo> findAll() {
		return repo.findAll();
	}

	@Override
	public SolicitudEnsayo create(SolicitudEnsayo obj) {
		
		Secuencial secuencial = secuencialService.ObtenerSecuencialPorTipoSolicitud(TipoSolicitud.SOLICITUD_ENSAYOS);
		SolicitudEnsayo nuevo = new SolicitudEnsayo(
				secuencial.getNumeroSecuencial(),
				obj.getProveedorNombre(), 
				obj.getProveedorId(), 
				obj.getFechaEntrega(), 
				obj.getObjetivo(), 
				obj.getPrioridad(), 
				obj.getTiempoEntrega(),
				obj.getDetalleMaterial(), 
				obj.getLineaAplicacion(), 
				obj.getUso(),
				obj.getCantidad(),
				obj.getUnidad(), 
				nombreUsuarioEnSesion());
		
		LOG.info(String.format("Solicitud Ensayo a guardar %s", nuevo));
		return repo.save(nuevo);
	}

	@Override
	public SolicitudEnsayo findById(SolicitudEnsayo id) {
		// TODO Auto-generated method stub
		return repo.findById(id.getId()).orElse(null);
	}

	@Override
	public SolicitudEnsayo update(SolicitudEnsayo obj) {
		return repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioSolicitante() {
		return repo.findByNombreSolicitanteOrderByFechaCreacionDesc(nombreUsuarioEnSesion());
	}

	@Override
	public List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioEnGestion() {
		return repo.findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(Arrays.asList(EstadoSolicitud.EN_PROCESO, EstadoSolicitud.REGRESADO_NOVEDAD_INFORME), nombreUsuarioEnSesion());
	}
	
	@Override
	public List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioAprobador() {
		return repo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud.REVISION_INFORME, nombreUsuarioEnSesion());
	}

	@Override
	public SolicitudEnsayo buscarPorId(long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public boolean enviarSolicitud(SolicitudEnsayo solicitud) {
		
		Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
		Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.VALIDAR_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS);
		if(!configuracionOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuraci??n para el rol %s no existe.", OrdenFlujo.VALIDAR_SOLICITUD));
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudEnsayo solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.INGRESO_SOLICITUD, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoEnviada(configuracionOP.get().getUsuarioId());
		
		LOG.info(String.format("Solicitud id=%s enviada..", solicitudRecargada.getId()));
		return true;
	}
	
	
	private void agregarHistorial(SolicitudEnsayo solicitud, OrdenFlujo orden, String observacion) {
		String usuario = nombreUsuarioEnSesion();
		Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
		SolicitudHistorial historial = new SolicitudHistorial(solicitud,orden, usuarioOp.get(), observacion);
		repoHistorial.save(historial);
		LOG.info(String.format("Historial guardado %s", historial));		
	}

	@Override
	public List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioValidador() {
		return repo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.ENVIADO_REVISION ,nombreUsuarioEnSesion());
	}

	@Override
	@Transactional
	public boolean validarSolicitud(SolicitudEnsayo solicitud) {
		Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
		Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.RESPONDER_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS);
		
		if(!configuracionOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuraci??n para el rol %s no existe.", OrdenFlujo.RESPONDER_SOLICITUD));
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudEnsayo solicitudRecargada = solicitudOP.get();
		
		Optional<ConfiguracionTiempoSolicitud> configuracionTiempoOP = repoConfiguracionTiempo.
				findByOrdenAndTipoSolicitudAndTipoEntrega(OrdenFlujo.RESPONDER_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS, solicitudRecargada.getTiempoEntrega());
		
		if(!configuracionTiempoOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuraci??n para el tipo de entrega %s no existe.", solicitudRecargada.getTiempoEntrega()));
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.VALIDAR_SOLICITUD, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoValidada(configuracionOP.get().getUsuarioId(),configuracionTiempoOP.get().getVigenciaDias());		
		
		LOG.info(String.format("Solicitud id=%s validada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean responderSolicitud(SolicitudEnsayo solicitud) {
		Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
		Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.APROBAR_INFORME, TipoSolicitud.SOLICITUD_ENSAYOS);
		if(!configuracionOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuraci??n para el rol %s no existe.", OrdenFlujo.APROBAR_INFORME));
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudEnsayo solicitudRecargada = solicitudOP.get();
		
		documentoServicio.validarInformeSubido(solicitudRecargada.getId(), solicitudRecargada.getEstado());
		
		Optional<ConfiguracionTiempoSolicitud> configuracionTiempoOP = repoConfiguracionTiempo.
				findByOrdenAndTipoSolicitudAndTipoEntrega(OrdenFlujo.APROBAR_INFORME, TipoSolicitud.SOLICITUD_ENSAYOS, solicitudRecargada.getTiempoEntrega());
		
		if(!configuracionTiempoOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Configuraci??n para el tipo de entrega %s no existe.", solicitudRecargada.getTiempoEntrega()));
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.RESPONDER_SOLICITUD, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoRespondida(configuracionOP.get().getUsuarioId(), configuracionTiempoOP.get().getVigenciaDias());		
		
		LOG.info(String.format("Solicitud id=%s respondida..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean aprobarSolicitud(SolicitudEnsayo solicitud) {
		Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
		
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudEnsayo solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoAprobada(solicitud.getTipoAprobacion());		
		
		LOG.info(String.format("Solicitud id=%s validada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean regresarSolicitud(SolicitudEnsayo solicitud) {
		Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
		
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudEnsayo solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());
		
		solicitudRecargada.marcarSolicitudComoRegresada();		
		
		LOG.info(String.format("Solicitud id=%s regresada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean anularSolicitud(SolicitudEnsayo solicitud) {
		Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
		
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudEnsayo solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, solicitud.getOrden(), solicitud.getObservacion());
		
		solicitudRecargada.anular();		
		
		LOG.info(String.format("Solicitud id=%s anulada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional
	public boolean rechazarSolicitud(SolicitudEnsayo solicitud) {
		Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
		
		if(!solicitudOP.isPresent())
			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
		
		SolicitudEnsayo solicitudRecargada = solicitudOP.get();
		
		agregarHistorial(solicitudRecargada, solicitud.getOrden(), solicitud.getObservacion());
		
		solicitudRecargada.rechazar();		
		
		LOG.info(String.format("Solicitud id=%s rechazada..", solicitudRecargada.getId()));
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SolicitudDTO> consultar(Pageable pageable,ConsultaSolicitudDTO dto) {
		try {
			
			List<SolicitudDTO> respuesta = new ArrayList<>();
			
			if(dto.getTipoSolicitud() == null) {
				final List<SolicitudDTO> ensayos = obtenerSolicitudesEnsayo(dto);
				final List<SolicitudDTO> pruebasProceso = obtenerSolicitudesPruebasProceso(dto);
				
				if(!ensayos.isEmpty())
					respuesta.addAll(ensayos);
				
				if(!pruebasProceso.isEmpty())
					respuesta.addAll(pruebasProceso);
			}else {
				if(dto.getTipoSolicitud().equals(TipoSolicitud.SOLICITUD_ENSAYOS))
					respuesta.addAll(obtenerSolicitudesEnsayo(dto));
				if(dto.getTipoSolicitud().equals(TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO))
					respuesta.addAll(obtenerSolicitudesPruebasProceso(dto));
			}
			
			final int sizeTotal = respuesta.size();

			final int start = (int) pageable.getOffset();
			final int end = (start + pageable.getPageSize()) > respuesta.size() ? respuesta.size()
					: (start + pageable.getPageSize());

			respuesta = respuesta.subList(start, end);

			final Page<SolicitudDTO> pageResut = new PageImpl<>(respuesta, pageable, sizeTotal);

			return pageResut;
			
		}catch (Exception e) {
			final Page<SolicitudDTO> pageResult = new PageImpl<SolicitudDTO>(new ArrayList<SolicitudDTO>(), pageable, 0);
			return pageResult;
		}
	}
	
	private List<SolicitudDTO> obtenerSolicitudesEnsayo(ConsultaSolicitudDTO consulta){
		try {
			final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			final CriteriaQuery<SolicitudEnsayo> query = criteriaBuilder.createQuery(SolicitudEnsayo.class);

			final Root<SolicitudEnsayo> root = query.from(SolicitudEnsayo.class);
			final List<Predicate> predicadosConsulta = new ArrayList<>();
			
			if(consulta.getEstado() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("estado"), EstadoSolicitud.valueOf(consulta.getEstado().toString())));
			}
			
			if(consulta.getTipoAprobacion() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("tipoAprobacion"), TipoAprobacionSolicitud.valueOf(consulta.getTipoAprobacion().toString())));
			}
			
			if(noEsNuloNiBlanco(consulta.getCodigo())) {
				predicadosConsulta.add(criteriaBuilder.like(root.get("codigo"), "%" + consulta.getCodigo() + "%"));
			}
			
			if(noEsNuloNiBlanco(consulta.getNombreSolicitante())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("nombreSolicitante"), consulta.getNombreSolicitante()));
			}
			
			if(noEsNuloNiBlanco(consulta.getUsuarioGestion())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("usuarioGestion"), consulta.getUsuarioGestion()));
			}
			
			if(noEsNuloNiBlanco(consulta.getUsuarioAprobador())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("usuarioAprobador"), consulta.getUsuarioAprobador()));
			}
			
			if(noEsNuloNiBlanco(consulta.getUsuarioValidador())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("validador"), consulta.getUsuarioValidador()));
			}
			
			if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaCreacion"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaCreacion"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
			}
			
			query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
			.orderBy(criteriaBuilder.desc(root.get("fechaCreacion")));

			final TypedQuery<SolicitudEnsayo> statement = this.entityManager.createQuery(query);

			final List<SolicitudEnsayo> cotizacionesResult = statement.getResultList();

			return cotizacionesResult.stream().map(c -> {
				return new SolicitudDTO(
						c.getId(),
						c.getCodigo(),
						c.getFechaCreacion(),
						c.getFechaAprobacion(),
						c.getNombreSolicitante(),
						c.getUsuarioGestion(),
						c.getUsuarioAprobador(),
						c.getEstado(),
						c.getProveedorNombre(),
						c.getProveedorId(),
						c.getFechaEntrega(),
						c.getDetalleMaterial(),
						TipoSolicitud.SOLICITUD_ENSAYOS,
						c.getTipoAprobacion()
						);
			}).collect(Collectors.toList());
			
		}catch(Exception e) {
			LOG.error(String.format("Error al consultar solicitudes %s", e.getMessage()));
			return new ArrayList<>();
		}
	}

	private List<SolicitudDTO> obtenerSolicitudesPruebasProceso(ConsultaSolicitudDTO consulta){
		try {
			final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
			final CriteriaQuery<SolicitudPruebasProceso> query = criteriaBuilder.createQuery(SolicitudPruebasProceso.class);

			final Root<SolicitudPruebasProceso> root = query.from(SolicitudPruebasProceso.class);
			final List<Predicate> predicadosConsulta = new ArrayList<>();
			
			if(consulta.getEstado() != null) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("estado"), EstadoSolicitud.valueOf(consulta.getEstado().toString())));
			}
			
			
			if(noEsNuloNiBlanco(consulta.getCodigo())) {
				predicadosConsulta.add(criteriaBuilder.like(root.get("codigo"), "%" + consulta.getCodigo() + "%"));
			}
			
			if(noEsNuloNiBlanco(consulta.getNombreSolicitante())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("nombreSolicitante"), consulta.getNombreSolicitante()));
			}
			
			if(noEsNuloNiBlanco(consulta.getUsuarioGestion())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("usuarioGestion"), consulta.getUsuarioGestion()));
			}
			
			if(noEsNuloNiBlanco(consulta.getUsuarioAprobador())) {
				predicadosConsulta.add(criteriaBuilder.equal(root.get("usuarioAprobador"), consulta.getUsuarioAprobador()));
			}
			
			
			if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaCreacion"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
			}

			if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
				predicadosConsulta.add(criteriaBuilder.between(root.get("fechaCreacion"),
						consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
						consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
			}
			
			query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
			.orderBy(criteriaBuilder.desc(root.get("fechaCreacion")));

			final TypedQuery<SolicitudPruebasProceso> statement = this.entityManager.createQuery(query);

			final List<SolicitudPruebasProceso> cotizacionesResult = statement.getResultList();

			return cotizacionesResult.stream().map(c -> {
				return new SolicitudDTO(
						c.getId(),
						c.getCodigo(),
						c.getFechaCreacion(),
						c.getFechaAprobacion(),
						c.getNombreSolicitante(),
						c.getUsuarioGestion(),
						c.getUsuarioAprobador(),
						c.getEstado(),
						"",
						0,
						c.getFechaEntrega(),
						c.getMaterialLineaProceso(),
						TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO,
						null
						);
			}).collect(Collectors.toList());
			
		}catch(Exception e) {
			LOG.error(String.format("Error al consultar solicitudes %s", e.getMessage()));
			return new ArrayList<>();
		}
	}

}
