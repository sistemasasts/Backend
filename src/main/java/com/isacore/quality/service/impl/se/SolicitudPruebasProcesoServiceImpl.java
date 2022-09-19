package com.isacore.quality.service.impl.se;

import com.isacore.notificacion.servicio.ServicioNotificacionSolicitudPP;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.se.*;
import com.isacore.quality.model.spp.*;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.repository.spp.IConfiguracionFlujoPuebaProcesoRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoHistorialRepo;
import com.isacore.quality.repository.spp.ISolicitudPruebaProcesoResponsableRepo;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.esNuloOBlanco;
import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;
import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@Service
public class SolicitudPruebasProcesoServiceImpl implements ISolicitudPruebasProcesoService {

    private static final Log LOG = LogFactory.getLog(SolicitudPruebasProcesoServiceImpl.class);

    @Autowired
    private ISolicitudPruebasProcesoRepo repo;

    @Autowired
    private IConfiguracionFlujoPuebaProcesoRepo repoConfiguracion;

    @Autowired
    private ISolicitudPruebaProcesoHistorialRepo repoHistorial;

    @Autowired
    private IUserImptekRepo repoUsuario;

    @Autowired
    private SecuencialServiceImpl secuencialService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ISolicitudPruebaProcesoResponsableRepo responsableRepo;
    @Autowired
    private ServicioNotificacionSolicitudPP servicioNotificacion;
    @Autowired
    private IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;

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
                nombreUsuarioEnSesion(),
                obj.getArea(),
                obj.getOrigen(),
                obj.isRequiereInforme());

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
        return repo.findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(Arrays.asList(EstadoSolicitudPP.EN_PROCESO),
                nombreUsuarioEnSesion());
    }

    @Transactional(readOnly = true)
    @Override
    public List<SolicitudPruebasProceso> obtenerSolicitudesPorAsignarResponsable(OrdenFlujoPP orden) {
        String usuario = nombreUsuarioEnSesion();
        switch (orden) {
            case PRODUCCION:
            case MANTENIMIENTO:
            case CALIDAD:
                return this.responsableRepo.findByUsuarioResponsableAndActivoTrueAndOrdenAndEstadoIn(usuario, orden,
                        Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE))
                        .stream()
                        .filter(x -> esNuloOBlanco(x.getUsuarioAsignado()))
                        .map(SolicitudPruebaProcesoResponsable::getSolicitudPruebasProceso)
                        .collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    @Override
    public List<SolicitudPruebasProceso> obtenerSolicitudesPorUsuarioAprobador() {
        return repo.findByEstadoAndUsuarioValidadorOrderByFechaCreacionDesc(EstadoSolicitudPP.ENVIADO_REVISION, nombreUsuarioEnSesion());
    }

    @Transactional(readOnly = true)
    @Override
    public List<SolicitudPruebasProceso> obtenerSolicitudesPorProcesar(OrdenFlujoPP orden) {
        String usuario = nombreUsuarioEnSesion();
        switch (orden) {
            case PRODUCCION:
            case MANTENIMIENTO:
            case CALIDAD:
                return this.responsableRepo.findByUsuarioAsignadoAndActivoTrueAndOrdenAndEstadoIn(usuario, orden,
                        Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE))
                        .stream()
                        .map(SolicitudPruebaProcesoResponsable::getSolicitudPruebasProceso)
                        .collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    @Override
    public List<SolicitudPruebasProceso> obtenerSolicitudesPorAprobar(OrdenFlujoPP orden) {
        String usuario = nombreUsuarioEnSesion();
        if (orden.equals(OrdenFlujoPP.APROBAR_SOLICITUD)) {
            return this.repo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitudPP.PENDIENTE_APROBACION, usuario);
        } else {
            return this.responsableRepo.findByUsuarioResponsableAndActivoTrueAndOrdenAndEstadoIn(usuario, orden,
                    Arrays.asList(EstadoSolicitudPPResponsable.POR_APROBAR))
                    .stream()
                    .map(SolicitudPruebaProcesoResponsable::getSolicitudPruebasProceso)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public SolicitudPruebasProceso buscarPorId(long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public boolean enviarSolicitud(SolicitudPruebasProceso solicitud) {

        Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
        Optional<ConfiguracionFlujoPruebaProceso> configuracionOP = repoConfiguracion.findByOrden(OrdenFlujoPP.VALIDAR_SOLICITUD);
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
        return repo.findByEstadoAndUsuarioValidadorOrderByFechaCreacionDesc(EstadoSolicitudPP.ENVIADO_REVISION, nombreUsuarioEnSesion());
    }

    @Override
    @Transactional
    public boolean validarSolicitud(SolicitudPruebasProceso solicitud) {
        Optional<SolicitudPruebasProceso> solicitudOP = repo.findById(solicitud.getId());
        Optional<ConfiguracionFlujoPruebaProceso> configuracionOP = repoConfiguracion.findByOrden(OrdenFlujoPP.ASIGNAR_RESPONSABLE);
        if (!configuracionOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujoPP.ASIGNAR_RESPONSABLE));
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudPruebasProceso solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, OrdenFlujoPP.VALIDAR_SOLICITUD, solicitud.getObservacion());
        this.crearResponsable(configuracionOP.get().getUsuarioId(), OrdenFlujoPP.PRODUCCION, solicitudRecargada);
        solicitudRecargada.marcarSolicitudComoValidada(configuracionOP.get().getUsuarioId());

        LOG.info(String.format("Solicitud %s marcada como validada..", solicitudRecargada.getCodigo()));
        return true;
    }

    @Transactional
    @Override
    public boolean asignarResponsable(SolicitudPruebasProceso dto) {
        switch (dto.getOrden()) {
            case PRODUCCION:
                this.asignarResponsablePlanta(dto);
                break;
            case CALIDAD:
                this.asignarResponsableCalidad(dto);
                break;
            case MANTENIMIENTO:
                this.asignarResponsableMantenimiento(dto);
                break;
            default:
                LOG.info(String.format("Asignacion resposable para %s, no está programado..", dto.getOrden()));
                break;
        }
        return true;
    }

    @Transactional
    @Override
    public boolean marcarComoPruebaNoRealizada(SolicitudPruebasProceso solicitud) {
        SolicitudPruebasProceso solicitudRecargada = this.obtenerSolicitud(solicitud.getId());
        this.agregarHistorial(solicitudRecargada, solicitud.getOrden(), solicitud.getObservacion());
        solicitudRecargada.marcarComoPruebaNoEjecutada();
        LOG.info(String.format("Solicitud %s, marcada como prueba no ejecutada", solicitudRecargada.getCodigo()));
        try {
            servicioNotificacion.notificarPruebaNoEjecutada(solicitudRecargada);
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar PRUEBA NO EJECUTADA: %s", e));
        }
        return true;
    }

    @Transactional
    @Override
    public boolean marcarComoPruebaRealizada(SolicitudPruebasProceso solicitud) {
        SolicitudPruebasProceso solicitudRecargada = this.obtenerSolicitud(solicitud.getId());
        ConfiguracionGeneralFlujo config = this.obtenerConfiguracion(TipoSolicitud.SOLICITUD_PRUEBAS_EN_PROCESO,
                NombreConfiguracionFlujo.TIEMPO_ENTREGA_INFORME);
        solicitudRecargada.marcarComoPruebaEjecutada(Integer.parseInt(config.getValorConfiguracion()));
        this.CrearMatrizResponsables(solicitudRecargada);
        LOG.info(String.format("Solicitud %s, marcada como prueba ejecutada", solicitudRecargada.getCodigo()));
        try {
            servicioNotificacion.notificarPruebaEjecutada(solicitudRecargada);
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar PRUEBA EJECUTADA: %s", e));
        }
        return true;
    }

    @Transactional
    @Override
    public void procesar(SolicitudPruebasProceso solicitud) {
        SolicitudPruebasProceso solicitudRecargada = obtenerSolicitud(solicitud.getId());
        switch (solicitud.getOrden()) {
            case PRODUCCION:
                this.responderSolicitudPlanta(solicitudRecargada, solicitud.getObserviacionFlujo());
                break;
            case CALIDAD:
                this.responderSolicitudCalidad(solicitudRecargada, solicitud.getObserviacionFlujo());
                break;
            case MANTENIMIENTO:
                this.responderSolicitudMantenimiento(solicitudRecargada, solicitud.getObserviacionFlujo());
                break;
            default:
                break;
        }
    }

    @Transactional
    @Override
    synchronized public void procesarAprobacion(AprobarSolicitudDTO dto) {
        SolicitudPruebasProceso solicitudRecargada = obtenerSolicitud(dto.getSolicitudId());
        if (dto.getOrden().equals(OrdenFlujoPP.APROBAR_SOLICITUD)) {
            String observacion = esNuloOBlanco(dto.getObservacion()) ? "SOLICITUD APROBADA" : dto.getObservacion();
            this.agregarHistorial(solicitudRecargada, dto.getOrden(), observacion);
            this.aprobarSolcitud(solicitudRecargada, dto.getTipoAprobacion(), observacion);
            LOG.info(String.format("La solicitud %s fue aprobada %s con el tipoo %s", solicitudRecargada.getCodigo(),
                    dto.getTipoAprobacion().isAprobado(), dto.getTipoAprobacion()));
        } else {
            SolicitudPruebaProcesoResponsable responsable = obtenerResponsable(dto.getOrden(),
                    Arrays.asList(EstadoSolicitudPPResponsable.POR_APROBAR), dto.getSolicitudId());
            this.agregarHistorial(solicitudRecargada, dto.getOrden(), dto.getObservacion());
            responsable.marcarAprobacion(dto.isAprobar());
            LOG.info(String.format("Solicitud %s aprobada %s en el paso %s", solicitudRecargada.getCodigo(), dto.isAprobar(), dto.getOrden()));
            this.verificarFinProceso(solicitudRecargada);
        }
    }

    private void aprobarSolcitud(SolicitudPruebasProceso solicitud, TipoAprobacionPP tipoAprobacion, String observacion) {
        solicitud.marcarComoAprobada(tipoAprobacion);
        switch (tipoAprobacion) {
            case ENVIAR_SOLUCIONES_TECNICAS:
            case CREACION_MATERIA_PRIMA:
            case GESTIONAR_IMPLEMENTAR_CAMBIOS:
            case REPETIR_PRUEBA:
            case MATERIAL_NO_VALIDO:
                solicitud.setEstado(EstadoSolicitudPP.FINALIZADO);
                try {
                    servicioNotificacion.notificarSolicitudAprobada(solicitud, observacion);
                } catch (Exception e) {
                    LOG.error(String.format("Error al notificar APROBACION SOLICITUD DDP04: %s", e));
                }
                break;
            case AJUSTE_MAQUINARIA:
                solicitud.setEstado(EstadoSolicitudPP.PENDIENTE_AJUSTE_MAQUINARIA);
                try {
                    servicioNotificacion.notificarAjusteMaquinaria(solicitud, observacion);
                } catch (Exception e) {
                    LOG.error(String.format("Error al notificar AJUSTE DE MAQUINARIA SOLICITUD DDP04: %s", e));
                }
                break;
            default:
                break;
        }
    }

    private void verificarFinProceso(SolicitudPruebasProceso solicitud) {
        List<SolicitudPruebaProcesoResponsable> responsables = this.responsableRepo.findBySolicitudPruebasProceso_Id(solicitud.getId());
        if (responsables.stream().allMatch(x -> x.getEstado().equals(EstadoSolicitudPPResponsable.PROCESADO))) {
            ConfiguracionFlujoPruebaProceso configuracion = this.obtenerConfiguracion(OrdenFlujoPP.APROBAR_SOLICITUD);
            solicitud.marcarComoProcesoFinalizado(configuracion.getUsuarioId());
            LOG.info(String.format("Solicitud %s asignada a usuario aprobador %s", solicitud.getCodigo(), configuracion.getUsuarioId()));
        }
    }

    private void CrearMatrizResponsables(SolicitudPruebasProceso solicitud) {
        ConfiguracionFlujoPruebaProceso configMantenimiento = this.obtenerConfiguracion(OrdenFlujoPP.MANTENIMIENTO);
        solicitud.setUsuarioGestionMantenimientoJefe(configMantenimiento.getUsuarioId());
        ConfiguracionFlujoPruebaProceso configCalidad = this.obtenerConfiguracion(OrdenFlujoPP.CALIDAD);
        solicitud.setUsuarioGestionCalidadJefe(configCalidad.getUsuarioId());
        this.crearResponsable(configMantenimiento.getUsuarioId(), OrdenFlujoPP.MANTENIMIENTO, solicitud);
        this.crearResponsable(configCalidad.getUsuarioId(), OrdenFlujoPP.CALIDAD, solicitud);
    }

    private void asignarResponsablePlanta(SolicitudPruebasProceso dto) {
        SolicitudPruebasProceso solicitud = obtenerSolicitud(dto.getId());
        //agregarHistorial(solicitud, dto.getOrden(), dto.getObservacion());
        SolicitudPruebaProcesoResponsable responsable = this.obtenerResponsable(dto.getOrden(),
                Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE), solicitud.getId());
        responsable.asignarUsuario(dto.getUsuarioAsignado());
        solicitud.marcarSolicitudComoAsignadaPlanta(dto.getUsuarioAsignado(), dto.getFechaPrueba());
        LOG.info(String.format("Solicitud %s asignada a responsable planta %s", solicitud.getCodigo(), solicitud.getUsuarioGestionPlanta()));
    }

    private void asignarResponsableCalidad(SolicitudPruebasProceso dto) {
        SolicitudPruebasProceso solicitud = obtenerSolicitud(dto.getId());
        SolicitudPruebaProcesoResponsable responsable = this.obtenerResponsable(dto.getOrden(),
                Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE), solicitud.getId());
        responsable.asignarUsuario(dto.getUsuarioAsignado());
        solicitud.marcarSolicitudComoAsignadaCalidad(dto.getUsuarioAsignado());
        LOG.info(String.format("Solicitud %s asignada a responsable calidad %s", solicitud.getCodigo(), solicitud.getUsuarioGestionCalidad()));
    }

    private void asignarResponsableMantenimiento(SolicitudPruebasProceso dto) {
        SolicitudPruebasProceso solicitud = obtenerSolicitud(dto.getId());
        SolicitudPruebaProcesoResponsable responsable = this.obtenerResponsable(dto.getOrden(),
                Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE), solicitud.getId());
        responsable.asignarUsuario(dto.getUsuarioAsignado());
        solicitud.marcarSolicitudComoAsignadaMantenimiento(dto.getUsuarioAsignado());
        LOG.info(String.format("Solicitud %s asignada a responsable mantenimiento %s", solicitud.getCodigo(),
                solicitud.getUsuarioGestionMantenimiento()));
    }

    private SolicitudPruebasProceso obtenerSolicitud(long id) {
        SolicitudPruebasProceso solicitud = this.repo.findById(id).orElse(null);
        if (solicitud == null) {
            throw new SolicitudEnsayoErrorException(String.format("Solicitud no encontrada"));
        }
        return solicitud;
    }

    private SolicitudPruebaProcesoResponsable obtenerResponsable(OrdenFlujoPP orden, Collection<EstadoSolicitudPPResponsable> estados,
                                                                 long idSolicitud) {
        Optional<SolicitudPruebaProcesoResponsable> responsable = this.responsableRepo.findByOrdenAndEstadoInAndSolicitudPruebasProceso_Id(orden,
                estados, idSolicitud).stream().findFirst();
        if (!responsable.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Responsable %s no encontrado para la solicitud", orden));
        return responsable.get();
    }

    private void responderSolicitudPlanta(SolicitudPruebasProceso solicitud, String observacion) {
        SolicitudPruebaProcesoResponsable responsable = obtenerResponsable(OrdenFlujoPP.PRODUCCION,
                Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE), solicitud.getId());
        responsable.marcarComoPendientePorAprobar();
        //agregarHistorial(solicitud, OrdenFlujoPP.MANTENIMIENTO, observacion);
        LOG.info(String.format("Solicitud %s respondida planta..", solicitud.getCodigo()));
    }

    private void responderSolicitudMantenimiento(SolicitudPruebasProceso solicitud, String observacion) {
        SolicitudPruebaProcesoResponsable responsable = obtenerResponsable(OrdenFlujoPP.MANTENIMIENTO,
                Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE), solicitud.getId());
        responsable.marcarComoPendientePorAprobar();
        //agregarHistorial(solicitud, OrdenFlujoPP.MANTENIMIENTO, observacion);
        LOG.info(String.format("Solicitud %s respondida mantenimiento..", solicitud.getCodigo()));
    }

    private void responderSolicitudCalidad(SolicitudPruebasProceso solicitud, String observacion) {
        SolicitudPruebaProcesoResponsable responsable = obtenerResponsable(OrdenFlujoPP.CALIDAD,
                Arrays.asList(EstadoSolicitudPPResponsable.PENDIENTE), solicitud.getId());
        responsable.marcarComoPendientePorAprobar();
        //agregarHistorial(solicitud, OrdenFlujoPP.CALIDAD, observacion);
        LOG.info(String.format("Solicitud %s respondida calidad..", solicitud.getCodigo()));
    }

    private ConfiguracionFlujoPruebaProceso obtenerConfiguracion(OrdenFlujoPP orden) {
        Optional<ConfiguracionFlujoPruebaProceso> configuracionOP = repoConfiguracion.findByOrden(orden);
        if (!configuracionOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", orden));
        return configuracionOP.get();
    }

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

    private SolicitudPruebaProcesoResponsable crearResponsable(String usuarioResponsable, OrdenFlujoPP orden,
                                                               SolicitudPruebasProceso solicitudPruebasProceso) {
        SolicitudPruebaProcesoResponsable responsable = new SolicitudPruebaProcesoResponsable(usuarioResponsable, orden, solicitudPruebasProceso);
        this.responsableRepo.save(responsable);
        LOG.info(String.format("Resposanble guardado de la Solicitud %s : %s", solicitudPruebasProceso.getCodigo(), responsable));
        return responsable;
    }

    private ConfiguracionGeneralFlujo obtenerConfiguracion(TipoSolicitud tipo, NombreConfiguracionFlujo nombreConfiguracionFlujo) {
        Optional<ConfiguracionGeneralFlujo> configuracion = this.configuracionGeneralFlujoRepo.findByTipoSolicitudAndNombreConfiguracionFlujo(tipo,
                nombreConfiguracionFlujo);
        if (!configuracion.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración %s no encontrada para %s", nombreConfiguracionFlujo, tipo));
        return configuracion.get();
    }
}
