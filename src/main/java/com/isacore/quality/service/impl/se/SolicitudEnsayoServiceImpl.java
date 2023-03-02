package com.isacore.quality.service.impl.se;

import com.isacore.notificacion.servicio.ServicioNotificacionSolicitudEnsayo;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.exception.SolicitudPruebaProcesoErrorException;
import com.isacore.quality.model.Area;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.se.*;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionAdjuntoRequeridoRepo;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.repository.se.IConfiguracionTiempoSolicitudRepo;
import com.isacore.quality.repository.se.IConfiguracionUsuarioRolEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudEnsayoRepo;
import com.isacore.quality.repository.se.ISolicitudHistorialRepo;
import com.isacore.quality.service.se.ISolicitudDocumentoService;
import com.isacore.quality.service.se.ISolicitudEnsayoService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.esNuloOBlanco;
import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;
import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@Service
public class SolicitudEnsayoServiceImpl implements ISolicitudEnsayoService {

    private static final Log LOG = LogFactory.getLog(SolicitudEnsayoServiceImpl.class);

    private ISolicitudEnsayoRepo repo;
    private IConfiguracionUsuarioRolEnsayoRepo repoConfiguracion;
    private ISolicitudHistorialRepo repoHistorial;
    private IUserImptekRepo repoUsuario;
    private ISolicitudDocumentoService documentoServicio;
    private SecuencialServiceImpl secuencialService;
    private IConfiguracionTiempoSolicitudRepo repoConfiguracionTiempo;
    private EntityManager entityManager;
    private IConfiguracionAdjuntoRequeridoRepo configuracionAdjuntoRequeridoRepo;
    private ISolicitudPruebasProcesoService pruebasProcesoService;
    private ServicioNotificacionSolicitudEnsayo servicioNotificacionSolicitudEnsayo;
    private IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;

    @Autowired
    public SolicitudEnsayoServiceImpl(
            ISolicitudEnsayoRepo repo,
            IConfiguracionUsuarioRolEnsayoRepo repoConfiguracion,
            ISolicitudHistorialRepo repoHistorial,
            IUserImptekRepo repoUsuario,
            ISolicitudDocumentoService documentoServicio,
            SecuencialServiceImpl secuencialService,
            IConfiguracionTiempoSolicitudRepo repoConfiguracionTiempo,
            EntityManager entityManager,
            IConfiguracionAdjuntoRequeridoRepo configuracionAdjuntoRequeridoRepo,
            ISolicitudPruebasProcesoService pruebasProcesoService,
            ServicioNotificacionSolicitudEnsayo servicioNotificacionSolicitudEnsayo,
            IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo) {
        this.repo = repo;
        this.repoConfiguracion = repoConfiguracion;
        this.repoHistorial = repoHistorial;
        this.repoUsuario = repoUsuario;
        this.documentoServicio = documentoServicio;
        this.secuencialService = secuencialService;
        this.repoConfiguracionTiempo = repoConfiguracionTiempo;
        this.entityManager = entityManager;
        this.configuracionAdjuntoRequeridoRepo = configuracionAdjuntoRequeridoRepo;
        this.pruebasProcesoService = pruebasProcesoService;
        this.servicioNotificacionSolicitudEnsayo = servicioNotificacionSolicitudEnsayo;
        this.configuracionGeneralFlujoRepo = configuracionGeneralFlujoRepo;
    }

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
                obj.getCantidad(),
                obj.getUnidad(),
                nombreUsuarioEnSesion(),
                obj.getMuestraEntrega(),
                obj.getMuestraUbicacion(),
                obj.getNombreComercial(),
                this.crearAdjuntosRequeridos());
        nuevo.marcarAdjuntoRespaldoComoObligatorio(obj.getPrioridad().equals(PrioridadNivel.ALTO));
        LOG.info(String.format("Solicitud Ensayo a guardar %s", nuevo));
        return repo.save(nuevo);
    }

    @Override
    public SolicitudEnsayo findById(SolicitudEnsayo id) {
        // TODO Auto-generated method stub
        return repo.findById(id.getId()).orElse(null);
    }

    @Transactional
    @Override
    public SolicitudEnsayo update(SolicitudEnsayo obj) {
        Optional<SolicitudEnsayo> solicitudOP = repo.findById(obj.getId());
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", obj.getId()));
        SolicitudEnsayo solicitud = solicitudOP.get();
        solicitud.setFechaEntrega(obj.getFechaEntrega());
        solicitud.setPrioridad(obj.getPrioridad());
        solicitud.marcarAdjuntoRespaldoComoObligatorio(obj.getPrioridad().equals(PrioridadNivel.ALTO));
        solicitud.setProveedorId(obj.getProveedorId());
        solicitud.setProveedorNombre(obj.getProveedorNombre());
        solicitud.setObjetivo(obj.getObjetivo());
        solicitud.setTiempoEntrega(obj.getTiempoEntrega());
        solicitud.setDetalleMaterial(obj.getDetalleMaterial());
        solicitud.setCantidad(obj.getCantidad());
        solicitud.setUnidad(obj.getUnidad());
        solicitud.setLineaAplicacion(obj.getLineaAplicacion());
        solicitud.setMuestraEntrega(obj.getMuestraEntrega());
        solicitud.setMuestraUbicacion(obj.getMuestraUbicacion());
        solicitud.setNombreComercial(obj.getNombreComercial());
        LOG.info(String.format("Solicitud ensayo actualizada %s", solicitud));
        return solicitud;
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
        List<SolicitudEnsayo> solicitudesPendientes = new ArrayList<>(repo.findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(Arrays.asList(EstadoSolicitud.EN_PROCESO,
                EstadoSolicitud.REGRESADO_NOVEDAD_INFORME, EstadoSolicitud.PENDIENTE_PRUEBAS_PROCESO), nombreUsuarioEnSesion()));
        solicitudesPendientes.addAll(this.repo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_PRUEBAS_PROCESO, nombreUsuarioEnSesion()));
        return solicitudesPendientes;
    }

    @Override
    public List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioAprobador() {
        return repo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_APROBACION, nombreUsuarioEnSesion());
    }

    @Override
    public SolicitudEnsayo buscarPorId(long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public boolean enviarSolicitud(SolicitudEnsayo solicitud) {

        Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.VALIDAR_SOLICITUD,
                TipoSolicitud.SOLICITUD_ENSAYOS);
        if (!configuracionOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.VALIDAR_SOLICITUD));
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudEnsayo solicitudRecargada = solicitudOP.get();
        if (!solicitudRecargada.adjuntosRequeridosCompletos())
            throw new SolicitudEnsayoErrorException(String.format("Debe cargar todos los adjuntos requeridos."));

        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "SOLICITUD ENVIADA" : solicitud.getObservacion();
        agregarHistorial(solicitudRecargada, OrdenFlujo.INGRESO_SOLICITUD, observacion);

        solicitudRecargada.marcarSolicitudComoEnviada(configuracionOP.get().getUsuarioId());

        LOG.info(String.format("Solicitud id=%s enviada..", solicitudRecargada.getId()));
        return true;
    }


    private void agregarHistorial(SolicitudEnsayo solicitud, OrdenFlujo orden, String observacion) {
        String usuario = nombreUsuarioEnSesion();
        Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
        SolicitudHistorial historial = new SolicitudHistorial(solicitud, orden, usuarioOp.get(), observacion);
        repoHistorial.save(historial);
        LOG.info(String.format("Historial guardado %s", historial));
    }

    @Override
    public List<SolicitudEnsayo> obtenerSolicitudesPorUsuarioValidador() {
        return repo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.ENVIADO_REVISION, nombreUsuarioEnSesion());
    }

    @Override
    @Transactional
    public boolean validarSolicitud(SolicitudEnsayo solicitud) {
        Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());
//        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.RESPONDER_SOLICITUD,
//            TipoSolicitud.SOLICITUD_ENSAYOS);

        if (esNuloOBlanco(solicitud.getUsuarioGestion()))
            throw new SolicitudEnsayoErrorException("Usuario responsable obligatorio.");
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudEnsayo solicitudRecargada = solicitudOP.get();

        Optional<ConfiguracionTiempoSolicitud> configuracionTiempoOP = repoConfiguracionTiempo.
                findByOrdenAndTipoSolicitudAndTipoEntrega(OrdenFlujo.RESPONDER_SOLICITUD, TipoSolicitud.SOLICITUD_ENSAYOS,
                        solicitudRecargada.getTiempoEntrega());

        if (!configuracionTiempoOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el tipo de entrega %s no existe.",
                    solicitudRecargada.getTiempoEntrega()));

        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "SOLICITUD VALIDADA" : solicitud.getObservacion();
        agregarHistorial(solicitudRecargada, OrdenFlujo.VALIDAR_SOLICITUD, observacion);
        int diaMaxEntregaInforme = this.obtenerDiaMaxEntregaInforme();
        LocalDate fechaInicioEntregaInforme = this.obtenerFechaInicioEntregaInforme(diaMaxEntregaInforme);
        solicitudRecargada.marcarSolicitudComoValidada(solicitud.getUsuarioGestion(), configuracionTiempoOP.get().getVigenciaDias(), fechaInicioEntregaInforme);

        LOG.info(String.format("Solicitud id=%s validada..", solicitudRecargada.getId()));
        try {
            this.servicioNotificacionSolicitudEnsayo.notificarIngresoMuestra(solicitudRecargada, solicitud.getObservacion());
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Ingreso de muestra %s", e));
        }
        return true;
    }

    @Override
    @Transactional
    public boolean responderSolicitud(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
        documentoServicio.validarInformeSubido(solicitudRecargada.getId(), solicitudRecargada.getEstado());
        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "INFORME ENVIADO" : solicitud.getObservacion();
        agregarHistorial(solicitudRecargada, OrdenFlujo.RESPONDER_SOLICITUD, observacion);
        solicitudRecargada.marcarSolicitudComoRespondida();

        LOG.info(String.format("Solicitud id=%s respondida..", solicitudRecargada.getId()));
        return true;
    }

    @Transactional
    @Override
    public boolean aprobarInforme(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.APROBAR_INFORME,
                TipoSolicitud.SOLICITUD_ENSAYOS);
        if (!configuracionOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.APROBAR_INFORME));
        Optional<ConfiguracionTiempoSolicitud> configuracionTiempoOP = repoConfiguracionTiempo.
                findByOrdenAndTipoSolicitudAndTipoEntrega(OrdenFlujo.APROBAR_INFORME, TipoSolicitud.SOLICITUD_ENSAYOS,
                        solicitudRecargada.getTiempoEntrega());

        if (!configuracionTiempoOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el tipo de entrega %s no existe.",
                    solicitudRecargada.getTiempoEntrega()));
        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "INFORME APROBADO" : solicitud.getObservacion();
        this.agregarHistorial(solicitudRecargada, OrdenFlujo.REVISION_INFORME, observacion);
        solicitudRecargada.marcarSolicitudComoInformeAprobado(configuracionOP.get().getUsuarioId(), configuracionTiempoOP.get().getVigenciaDias());
        LOG.info(String.format("Solicitud %s, marcada como informe aprobado", solicitudRecargada.getCodigo()));
        return true;
    }

    @Transactional
    @Override
    public boolean rechazarInforme(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
        this.agregarHistorial(solicitudRecargada, OrdenFlujo.REVISION_INFORME, solicitud.getObservacion());
        solicitudRecargada.setEstado(EstadoSolicitud.EN_PROCESO);
        LOG.info(String.format("Solicitud %s, marcada como informe rechazado", solicitudRecargada.getCodigo()));
        return true;
    }

    @Override
    @Transactional
    public boolean aprobarSolicitud(SolicitudEnsayo solicitud) {
        Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());

        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudEnsayo solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());

        solicitudRecargada.marcarSolicitudComoAprobada(solicitud.getTipoAprobacion());
        if (solicitud.getTipoAprobacion().equals(TipoAprobacionSolicitud.SOLICITUD_PRUEBA_PROCESO)) {
            solicitudRecargada.setEstado(EstadoSolicitud.PENDIENTE_PRUEBAS_PROCESO);
        }
        LOG.info(String.format("Solicitud id=%s aprobada, tipo aprobacion %s..", solicitudRecargada.getId(), solicitudRecargada.getTipoAprobacion()));
        //TODO: se debe agregar metodo de notificar al solicitante el estado de la solicitud
        try {
            this.servicioNotificacionSolicitudEnsayo.notificarSolicitudFinalizada(solicitudRecargada, solicitud.getObservacion());
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Solicitud Finalizada %s", e));
        }
        return true;
    }

    @Override
    @Transactional
    public boolean regresarSolicitud(SolicitudEnsayo solicitud) {
        Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());

        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudEnsayo solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());

        solicitudRecargada.marcarSolicitudComoRegresada();

        LOG.info(String.format("Solicitud id=%s regresada..", solicitudRecargada.getId()));
        return true;
    }

    @Transactional
    @Override
    public boolean regresarSolicitudForma(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
        agregarHistorial(solicitudRecargada, OrdenFlujo.VALIDAR_SOLICITUD, solicitud.getObservacion());
        solicitudRecargada.setEstado(EstadoSolicitud.REGRESADO_NOVEDAD_FORMA);
        LOG.info(String.format("Solicitud %s regresada por novedad solicitud..", solicitudRecargada.getCodigo()));
        try {
            this.servicioNotificacionSolicitudEnsayo.notificarSolicitudEstado(solicitudRecargada, solicitud.getObservacion());
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Solicitud Finalizada %s", e));
        }
        return true;
    }

    @Override
    @Transactional
    public boolean anularSolicitud(SolicitudEnsayo solicitud) {
        Optional<SolicitudEnsayo> solicitudOP = repo.findById(solicitud.getId());

        if (!solicitudOP.isPresent())
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
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudEnsayo solicitudRecargada = solicitudOP.get();
        agregarHistorial(solicitudRecargada, solicitud.getOrden(), solicitud.getObservacion());
        solicitudRecargada.rechazar();

        try{
            this.servicioNotificacionSolicitudEnsayo.notificarSolicitudEstado(solicitudRecargada,solicitud.getObservacion());
        }catch (Exception e) {
            LOG.error(String.format("Error al notificar Solicitud rechazada %s", e));
        }
        LOG.info(String.format("Solicitud id=%s rechazada..", solicitudRecargada.getId()));
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudDTO> consultar(Pageable pageable, ConsultaSolicitudDTO dto) {
        try {

            List<SolicitudDTO> respuesta = new ArrayList<>();
            respuesta.addAll(obtenerSolicitudesEnsayo(dto));
            final int sizeTotal = respuesta.size();

            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > respuesta.size() ? respuesta.size()
                    : (start + pageable.getPageSize());

            respuesta = respuesta.subList(start, end);

            final Page<SolicitudDTO> pageResut = new PageImpl<>(respuesta, pageable, sizeTotal);

            return pageResut;

        } catch (Exception e) {
            final Page<SolicitudDTO> pageResult = new PageImpl<SolicitudDTO>(new ArrayList<SolicitudDTO>(), pageable, 0);
            return pageResult;
        }
    }

    @Transactional
    @Override
    public SolicitudEnsayo crearSolicitudAPartirDeOtra(long solicitudId) {
        SolicitudEnsayo solicitud = this.obtenerSolicitudPorId(solicitudId);
        this.validarUnicaPruebaEnviadaNuevamenteEnCurso(solicitudId);
        SolicitudEnsayo solicitudNueva = this.create(solicitud);
        solicitudNueva.setSolicitudPadreId(solicitud.getId());
        LOG.info(String.format("Solicitud creada para reenviar -> solicitud origen %s :: solicitud nueva %s", solicitud.getCodigo(), solicitudNueva));
        return solicitudNueva;
    }

    @Transactional
    @Override
    public boolean finalizarProceso(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudEnsayo = this.obtenerSolicitudPorId(solicitud.getId());
        String observacion = noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "PROCESO FINALIZADO";
        this.agregarHistorial(solicitudEnsayo, OrdenFlujo.SOLICITANTE_PLANES_ACCION, observacion);
        solicitudEnsayo.setEstado(EstadoSolicitud.PROCESO_FINALIZADO);
        LOG.info(String.format("Solicitud %s marcada como proceso finalizado", solicitudEnsayo.getCodigo()));
        return true;
    }

    @Transactional
    @Override
    public boolean confirmarPlanesAccion(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudEnsayo = this.obtenerSolicitudPorId(solicitud.getId());
        String observacion = noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "PLANES DE ACCIÓN INGRESADOS";
        this.agregarHistorial(solicitudEnsayo, OrdenFlujo.SOLICITANTE_PLANES_ACCION, observacion);
        solicitudEnsayo.setEstado(EstadoSolicitud.PENDIENTE_PLANES_ACCION);
        LOG.info(String.format("Solicitud %s planes de accion confirmados y enviados a revision", solicitudEnsayo.getCodigo()));
        return true;
    }

    @Transactional
    @Override
    public boolean finalizarRevisionPlanesAccion(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudEnsayo = this.obtenerSolicitudPorId(solicitud.getId());
        String observacion = noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "REVISIÓN FINALIZADA";
        this.agregarHistorial(solicitudEnsayo, OrdenFlujo.REVISION_PLANES_ACCION, observacion);
        solicitudEnsayo.setEstado(EstadoSolicitud.PLANES_ACCION_REVISADOS);
        LOG.info(String.format("Solicitud %s planes de accion revisados", solicitudEnsayo.getCodigo()));
        return true;
    }

    @Transactional
    @Override
    public SolicitudPruebasProceso iniciarPruebaEnProceso(SolicitudEnsayo solicitud) {
        SolicitudEnsayo solicitudEnsayo = this.obtenerSolicitudPorId(solicitud.getId());
        SolicitudPruebasProceso nuevo = this.pruebasProcesoService.create(new SolicitudPruebasProceso(
                "",
                nombreUsuarioEnSesion(),
                solicitudEnsayo.getFechaEntrega(),
                this.obtenerAreaUsuarioEnSesion()
        ));
        String observacion = nuevo.getCodigo().concat(" :::").concat(noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "PROCESO INICIADO");
        this.agregarHistorial(solicitudEnsayo, OrdenFlujo.SOLICITUD_PRUEBAS_PROCESO, observacion);
        solicitudEnsayo.setEstado(EstadoSolicitud.GESTION_PRUEBAS_PROCESO);
        solicitudEnsayo.setSolicitudPruebaProcesoId(nuevo.getId());
        return nuevo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SolicitudEnsayo> obtenerSolicitudesPendientesPlanesAccion() {
        String usuario = nombreUsuarioEnSesion();
        return this.repo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_PLANES_ACCION, usuario);
    }

    private List<SolicitudDTO> obtenerSolicitudesEnsayo(ConsultaSolicitudDTO consulta) {
        try {
            final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<SolicitudEnsayo> query = criteriaBuilder.createQuery(SolicitudEnsayo.class);

            final Root<SolicitudEnsayo> root = query.from(SolicitudEnsayo.class);
            final List<Predicate> predicadosConsulta = new ArrayList<>();

            if (consulta.getEstado() != null) {
                predicadosConsulta.add(criteriaBuilder.equal(root.get("estado"), EstadoSolicitud.valueOf(consulta.getEstado().toString())));
            }

            if (consulta.getTipoAprobacion() != null) {
                predicadosConsulta.add(criteriaBuilder.equal(root.get("tipoAprobacion"),
                        TipoAprobacionSolicitud.valueOf(consulta.getTipoAprobacion().toString())));
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

            if (noEsNuloNiBlanco(consulta.getUsuarioValidador())) {
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
                        c.getMuestraEntrega(),
                        c.getDetalleMaterial(),
                        TipoSolicitud.SOLICITUD_ENSAYOS,
                        c.getTipoAprobacion() == null ? "":c.getTipoAprobacion().getDescripcion(),
                        c.getPrioridad()
                );
            }).collect(Collectors.toList());

        } catch (Exception e) {
            LOG.error(String.format("Error al consultar solicitudes %s", e.getMessage()));
            return new ArrayList<>();
        }
    }

    private List<SolicitudEnsayoAdjuntoRequerido> crearAdjuntosRequeridos() {
        return this.configuracionAdjuntoRequeridoRepo.findAll()
                .stream()
                .map(x -> new SolicitudEnsayoAdjuntoRequerido(x.getNombre(), x.getSecuencia(), x.isObligatorio()))
                .collect(Collectors.toList());
    }

    private SolicitudEnsayo obtenerSolicitudPorId(long id) {
        Optional<SolicitudEnsayo> solicitudOP = repo.findById(id);
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", id));
        return solicitudOP.get();
    }

    private void validarUnicaPruebaEnviadaNuevamenteEnCurso(long solicitudPadreId) {
        List<EstadoSolicitud> estados = Arrays.asList(EstadoSolicitud.ANULADO, EstadoSolicitud.FINALIZADO);
//        List<EstadoSolicitud> estadosFinalizados = Arrays.asList(EstadoSolicitud.LIBRE_USO, EstadoSolicitud.CREACION_MATERIA_PRIMA,
//                EstadoSolicitud.GESTIONAR_IMPLEMENTAR_CAMBIOS, EstadoSolicitud.ENVIAR_SOLUCIONES_TECNICAS);
        List<SolicitudEnsayo> solicitudesEnProceso = this.repo.findBySolicitudPadreId(solicitudPadreId);
        if (solicitudesEnProceso.stream().anyMatch(x -> !estados.contains(x.getEstado())))
            throw new SolicitudPruebaProcesoErrorException("La solicitud ya fue reenviada en un nuevo proceso.");
//        if(solicitudesEnProceso.stream().anyMatch(x -> estadosFinalizados.contains(x.getEstado())))
//            throw new SolicitudPruebaProcesoErrorException("La solicitud en otros de sus subprocesos ya cuenta con un tipo de aprobación.");
    }

    private Area obtenerAreaUsuarioEnSesion() {
        Optional<UserImptek> usuarioOp = repoUsuario.findById(nombreUsuarioEnSesion());
        if (!usuarioOp.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Usuario en sesión no tiene asignado una área"));
        return usuarioOp.get().getEmployee().getArea();
    }

    private LocalDate obtenerFechaInicioEntregaInforme(int diaMaxEntregaInforme) {
        LocalDate fechaInicio = LocalDate.now();
        int diaActual = LocalDate.now().getDayOfMonth();
        if (diaActual > diaMaxEntregaInforme) {
            fechaInicio = fechaInicio.plusMonths(1);
        }
        int mes = fechaInicio.getMonthValue();
        int anio = fechaInicio.getYear();
        return LocalDate.of(anio, mes, diaMaxEntregaInforme);
    }

    private int obtenerDiaMaxEntregaInforme() {
        Optional<ConfiguracionGeneralFlujo> diaMaxEntregaInforme = this.configuracionGeneralFlujoRepo.findByTipoSolicitudAndNombreConfiguracionFlujo(TipoSolicitud.SOLICITUD_ENSAYOS, NombreConfiguracionFlujo.DIA_MAX_PERMITIDO_ENTREGAR_MUESTRAS);
        if (!diaMaxEntregaInforme.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración día máximo entrega muestras no existe"));
        return Integer.parseInt(diaMaxEntregaInforme.get().getValorConfiguracion());
    }
}
