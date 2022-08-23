package com.isacore.quality.service.impl.se;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.*;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import com.isacore.quality.model.spp.SolicitudPPDTO;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import com.isacore.quality.repository.se.IConfiguracionUsuarioRolEnsayoRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoHistorialRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebasProcesoRepo;
import com.isacore.quality.service.se.ISolicitudPruebasProcesoService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;
import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@Service
public class SolicitudPruebasProcesoServiceImpl implements ISolicitudPruebasProcesoService {

    private static final Log LOG = LogFactory.getLog(SolicitudPruebasProcesoServiceImpl.class);

    @Autowired
    private ISolicitudPruebasProcesoRepo repo;

    @Autowired
    private IConfiguracionUsuarioRolEnsayoRepo repoConfiguracion;

    @Autowired
    private ISolicitudPruebaProcesoHistorialRepo repoHistorial;

    @Autowired
    private IUserImptekRepo repoUsuario;

    @Autowired
    private SecuencialServiceImpl secuencialService;

    @Autowired
    private EntityManager entityManager;

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
        return repo.findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(Arrays.asList(EstadoSolicitud.EN_PROCESO,
                EstadoSolicitud.REGRESADO_NOVEDAD_INFORME), nombreUsuarioEnSesion());
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
        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.VALIDAR_SOLICITUD,
                TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO);
        if (!configuracionOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.VALIDAR_SOLICITUD));
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, OrdenFlujoPP.INGRESO_SOLICITUD, solicitud.getObservacion());

        solicitudRecargada.marcarSolicitudComoEnviada(configuracionOP.get().getUsuarioId());

        LOG.info(String.format("Solicitud id=%s enviada..", solicitudRecargada.getId()));
        return true;
    }


    private void agregarHistorial(SolicitudPruebasProceso solicitud, OrdenFlujoPP orden, String observacion) {
        String usuario = nombreUsuarioEnSesion();
        Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
        SolicitudPruebaProcesoHistorial historial = new SolicitudPruebaProcesoHistorial(solicitud, orden, usuarioOp.get(), observacion);
        repoHistorial.save(historial);
        LOG.info(String.format("Historial guardado %s", historial));
    }

    @Override
    public List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioValidador() {
        return repo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud.ENVIADO_REVISION, nombreUsuarioEnSesion());
    }

    @Override
    @Transactional
    public boolean validarSolicitud(SolicitudPruebasProceso solicitud) {
        Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.RESPONDER_SOLICITUD,
                TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO);
        if (!configuracionOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.RESPONDER_SOLICITUD));
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, OrdenFlujoPP.VALIDAR_SOLICITUD, solicitud.getObservacion());

        solicitudRecargada.marcarSolicitudComoValidada(configuracionOP.get().getUsuarioId());

        LOG.info(String.format("Solicitud id=%s validada..", solicitudRecargada.getId()));
        return true;
    }

//    @Override
//    @Transactional
//    public boolean responderSolicitud(SolicitudPruebasProceso solicitud) {
//        Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
//        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.APROBAR_INFORME,
//                TipoSolicitud.SOLICITUD_ENSAYOS);
//        if (!configuracionOP.isPresent())
//            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.APROBAR_INFORME));
//        if (!solicitudOP.isPresent())
//            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
//
//        SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
//
//        agregarHistorial(solicitudRecargada, OrdenFlujoPP.RESPONDER_SOLICITUD, solicitud.getObservacion());
//
//        //solicitudRecargada.setEstado(EstadoSolicitud.FINALIZADO);
//        solicitudRecargada.finalizarSolicitud();
//
//        LOG.info(String.format("Solicitud id=%s respondida..", solicitudRecargada.getId()));
//        return true;
//    }


//	@Override
//	@Transactional
//	public boolean regresarSolicitud(SolicitudPruebasProceso solicitud) {
//		Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
//
//		if(!solicitudOP.isPresent())
//			throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
//
//		SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
//
//		agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());
//
//		solicitudRecargada.marcarSolicitudComoRegresada();
//
//		LOG.info(String.format("Solicitud id=%s regresada..", solicitudRecargada.getId()));
//		return true;
//	}

    @Override
    @Transactional
    public boolean anularSolicitud(SolicitudPruebasProceso solicitud) {
        Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());

        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, solicitudRecargada.getOrden(), solicitud.getObservacion());

        solicitudRecargada.anular();

        LOG.info(String.format("Solicitud id=%s anulada..", solicitudRecargada.getId()));
        return true;
    }

//    @Override
//    @Transactional
//    public boolean rechazarSolicitud(SolicitudPruebasProceso solicitud) {
//        Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
//
//        if (!solicitudOP.isPresent())
//            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
//
//        SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();
//
//        agregarHistorial(solicitudRecargada, solicitudRecargada.getOrden(), solicitud.getObservacion());
//
//        //solicitudRecargada.rechazar();
//
//        LOG.info(String.format("Solicitud id=%s rechazada..", solicitudRecargada.getId()));
//        return true;
//    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudPPDTO> consultar(Pageable pageable, ConsultaSolicitudDTO dto) {
        try {

            List<SolicitudPPDTO> respuesta = new ArrayList<>();
            final List<SolicitudPPDTO> pruebasProceso = obtenerSolicitudesPruebasProceso(dto);
            respuesta.addAll(pruebasProceso);
            final int sizeTotal = respuesta.size();

            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > respuesta.size() ? respuesta.size()
                    : (start + pageable.getPageSize());

            respuesta = respuesta.subList(start, end);

            final Page<SolicitudPPDTO> pageResut = new PageImpl<>(respuesta, pageable, sizeTotal);

            return pageResut;

        } catch (Exception e) {
            final Page<SolicitudPPDTO> pageResult = new PageImpl<SolicitudPPDTO>(new ArrayList<SolicitudPPDTO>(), pageable, 0);
            return pageResult;
        }
    }

    private List<SolicitudPPDTO> obtenerSolicitudesPruebasProceso(ConsultaSolicitudDTO consulta) {
        try {
            final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<SolicitudPruebasProceso> query = criteriaBuilder.createQuery(SolicitudPruebasProceso.class);

            final Root<SolicitudPruebasProceso> root = query.from(SolicitudPruebasProceso.class);
            final List<Predicate> predicadosConsulta = new ArrayList<>();

            if (consulta.getEstado() != null) {
                predicadosConsulta.add(criteriaBuilder.equal(root.get("estado"), EstadoSolicitud.valueOf(consulta.getEstado().toString())));
            }


            if (noEsNuloNiBlanco(consulta.getCodigo())) {
                predicadosConsulta.add(criteriaBuilder.like(root.get("codigo"), "%" + consulta.getCodigo() + "%"));
            }

            if (noEsNuloNiBlanco(consulta.getNombreSolicitante())) {
                predicadosConsulta.add(criteriaBuilder.equal(root.get("nombreSolicitante"), consulta.getNombreSolicitante()));
            }

            if (noEsNuloNiBlanco(consulta.getUsuarioGestion())) {
                predicadosConsulta.add(criteriaBuilder.equal(root.get("usuarioGestion"), consulta.getUsuarioGestion()));
            }

            if (noEsNuloNiBlanco(consulta.getUsuarioAprobador())) {
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
                return new SolicitudPPDTO(
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
                        null
                );
            }).collect(Collectors.toList());

        } catch (Exception e) {
            LOG.error(String.format("Error al consultar solicitudes %s", e.getMessage()));
            return new ArrayList<>();
        }
    }


}
