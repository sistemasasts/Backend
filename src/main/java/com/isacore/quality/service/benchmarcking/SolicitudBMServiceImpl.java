package com.isacore.quality.service.benchmarcking;

import com.isacore.notificacion.servicio.ServicioNotificacionSolicitudEnsayo;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.exception.SolicitudPruebaProcesoErrorException;
import com.isacore.quality.mapper.disenoPavimento.SolicitudEnsayoMinaAgregadosMapper;
import com.isacore.quality.mapper.disenoPavimento.SolicitudEnsayoMinaMapper;
import com.isacore.quality.mapper.SolicitudBM.SolicitudEnsayoMapper;
import com.isacore.quality.model.Area;
import com.isacore.quality.model.benchmarking.SolicitudBM;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.disenoPavimento.Mina;
import com.isacore.quality.model.se.*;
import com.isacore.quality.model.spp.SolicitudPruebasProceso;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionAdjuntoRequeridoRepo;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.repository.disenoPavimento.MinaRepo;
import com.isacore.quality.repository.disenoPavimento.SolicitudEnsayoMinaAgregadoRepo;
import com.isacore.quality.repository.se.*;
import com.isacore.quality.service.impl.se.SecuencialServiceImpl;
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
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.esNuloOBlanco;
import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;
import static com.isacore.util.UtilidadesSeguridad.nombreUsuarioEnSesion;

@Service
public class SolicitudBMServiceImpl {

    private static final Log LOG = LogFactory.getLog(SolicitudBMServiceImpl.class);

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
    private final ISolicitudDocumentoRepo repoDocumento;
    private final MinaRepo minaRepo;
    private final SolicitudEnsayoMinaMapper solicitudEnsayoMinaMapper;
    private final SolicitudEnsayoMinaAgregadosMapper solicitudEnsayoMinaAgregadosMapper;
    private final SolicitudEnsayoMinaAgregadoRepo ensayoMinaAgregadoRepo;
    private final SolicitudEnsayoMapper ensayoMapper;

    @Autowired
    public SolicitudBMServiceImpl(
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
            IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo,
            ISolicitudDocumentoRepo repoDocumento,
            MinaRepo minaRepo,
            SolicitudEnsayoMinaMapper solicitudEnsayoMinaMapper,
            SolicitudEnsayoMinaAgregadosMapper solicitudEnsayoMinaAgregadosMapper,
            SolicitudEnsayoMinaAgregadoRepo solicitudEnsayoMinaAgregadoRepo,
            SolicitudEnsayoMapper ensayoMapper
    ) {
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
        this.repoDocumento = repoDocumento;
        this.minaRepo = minaRepo;
        this.solicitudEnsayoMinaMapper = solicitudEnsayoMinaMapper;
        this.solicitudEnsayoMinaAgregadosMapper = solicitudEnsayoMinaAgregadosMapper;
        this.ensayoMinaAgregadoRepo = solicitudEnsayoMinaAgregadoRepo;
        this.ensayoMapper = ensayoMapper;
    }
    
    public List<SolicitudBM> findAll() {
        return repo.findAll();
    }

    public SolicitudBM create(SolicitudBM obj) {
        Secuencial secuencial = secuencialService.ObtenerSecuencialPorTipoSolicitud(TipoSolicitud.SOLICITUD_BENCHMARKING);

        SolicitudBM nuevo = new SolicitudBM(
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
                obj.getTipoDiseno(),
                obj.getTipoDisenoOtro(),
                this.crearAdjuntosRequeridos(),
                obj.getDisenios());

        nuevo.marcarAdjuntoRespaldoComoObligatorio();
        LOG.info(String.format("Solicitud Ensayo a guardar %s", nuevo));
        return repo.save(nuevo);
    }

    
    public SolicitudBM findById(SolicitudBM id) {
        // TODO Auto-generated method stub
        return repo.findById(id.getId()).orElse(null);
    }

    @Transactional
    
    public SolicitudBM update(SolicitudBM obj) {
        Optional<SolicitudBM> solicitudOP = repo.findById(obj.getId());
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", obj.getId()));
        SolicitudBM solicitud = solicitudOP.get();
        solicitud.setFechaEntrega(obj.getFechaEntrega());
        solicitud.setPrioridad(obj.getPrioridad());
        solicitud.marcarAdjuntoRespaldoComoObligatorio();
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
        solicitud.setTipoDiseno(obj.getTipoDiseno());
        solicitud.setTipoDisenoOtro(obj.getTipoDisenoOtro());
        //Seccion proyecto
        solicitud.setProyectoNombre(obj.getProyectoNombre());
        solicitud.setProyectoUbicacion(obj.getProyectoUbicacion());
        solicitud.setProyectoCanton(obj.getProyectoCanton());
        solicitud.setProyectoProvincia(obj.getProyectoProvincia());
        solicitud.setProyectoPais(obj.getProyectoPais());
        solicitud.setProyectoContratista(obj.getProyectoContratista());
        solicitud.setProyectoFiscalizador(obj.getProyectoFiscalizador());
        solicitud.setProyectoPropietario(obj.getProyectoPropietario());
        solicitud.setProyectoIniciado(obj.isProyectoIniciado());
        solicitud.setProyectoLatInicial(obj.getProyectoLatInicial());
        solicitud.setProyectoLatFinal(obj.getProyectoLatFinal());
        solicitud.setProyectoLngInicial(obj.getProyectoLngInicial());
        solicitud.setProyectoLngFinal(obj.getProyectoLngFinal());
        solicitud.setProyectoNumeroCarriles(obj.getProyectoNumeroCarriles());
        solicitud.setProyectoDimension(obj.getProyectoDimension());
        solicitud.getDisenios().clear();
        solicitud.getDisenios().addAll(obj.getDisenios());
        solicitud.setProyectoRedVial(obj.getProyectoRedVial());
        solicitud.setEjesEquivalentes(obj.getEjesEquivalentes());
        solicitud.setProyectoPorcentajeVehiculosPesados(obj.getProyectoPorcentajeVehiculosPesados());
        solicitud.setProyectoCategoriaVial(obj.getProyectoCategoriaVial());
        solicitud.setTipoLigante(obj.getTipoLigante());

        LOG.info(String.format("Solicitud ensayo actualizada %s", solicitud));
        return solicitud;
    }

    
    public boolean delete(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    
    public List<SolicitudBM> obtenerSolicitudesPorUsuarioSolicitante() {
        return repo.findByNombreSolicitanteOrderByFechaCreacionDesc(nombreUsuarioEnSesion());
    }

    
    public List<SolicitudBM> obtenerSolicitudesPorUsuarioEnGestion() {
        List<SolicitudBM> solicitudesPendientes = new ArrayList<>(repo.findByEstadoInAndUsuarioGestionOrderByFechaCreacionDesc(Arrays.asList(EstadoSolicitud.EN_PROCESO,
                EstadoSolicitud.REGRESADO_NOVEDAD_INFORME, EstadoSolicitud.PENDIENTE_PRUEBAS_PROCESO), nombreUsuarioEnSesion()));
        solicitudesPendientes.addAll(this.repo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_PRUEBAS_PROCESO, nombreUsuarioEnSesion()));
        return solicitudesPendientes;
    }

    
    public List<SolicitudBM> obtenerSolicitudesPorUsuarioAprobador() {
        return repo.findByEstadoAndUsuarioAprobadorOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_APROBACION, nombreUsuarioEnSesion());
    }

    
    public SolicitudBM buscarPorId(long id) {
        return repo.findById(id).orElse(null);
    }

    
    @Transactional
    public boolean enviarSolicitud(SolicitudBM solicitud) {

        Optional<SolicitudBM> solicitudOP = repo.findById(solicitud.getId());
        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.VALIDAR_SOLICITUD,
                TipoSolicitud.SOLICITUD_ENSAYOS);
        if (!configuracionOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.VALIDAR_SOLICITUD));
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudBM solicitudRecargada = solicitudOP.get();
        if (!solicitudRecargada.adjuntosRequeridosCompletos())
            throw new SolicitudEnsayoErrorException(String.format("Debe cargar todos los adjuntos requeridos."));

        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "SOLICITUD ENVIADA" : solicitud.getObservacion();
        agregarHistorial(solicitudRecargada, OrdenFlujo.INGRESO_SOLICITUD, observacion);

        solicitudRecargada.marcarSolicitudComoEnviada(configuracionOP.get().getUsuarioId());

        LOG.info(String.format("Solicitud id=%s enviada..", solicitudRecargada.getId()));
        return true;
    }


    private void agregarHistorial(SolicitudBM solicitud, OrdenFlujo orden, String observacion) {
        String usuario = nombreUsuarioEnSesion();
        Optional<UserImptek> usuarioOp = repoUsuario.findById(usuario);
        SolicitudHistorial historial = new SolicitudHistorial(solicitud, orden, usuarioOp.get(), observacion);
        repoHistorial.save(historial);
        LOG.info(String.format("Historial guardado %s", historial));
    }

    
    public List<SolicitudBM> obtenerSolicitudesPorUsuarioValidador() {
        return repo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.ENVIADO_REVISION, nombreUsuarioEnSesion());
    }

    
    @Transactional
    public boolean validarSolicitud(SolicitudBM solicitud) {
        Optional<SolicitudBM> solicitudOP = repo.findById(solicitud.getId());
//        Optional<ConfiguracionUsuarioRolEnsayo> configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.RESPONDER_SOLICITUD,
//            TipoSolicitud.SOLICITUD_ENSAYOS);

        if (esNuloOBlanco(solicitud.getUsuarioGestion()))
            throw new SolicitudEnsayoErrorException("Usuario responsable obligatorio.");
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudBM solicitudRecargada = solicitudOP.get();

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
        int vigenciaDias = solicitudRecargada.esDisenioPavimentos() ? configuracionTiempoOP.get().getVigenciaDiasDisenioPavimentos() : configuracionTiempoOP.get().getVigenciaDias();
        solicitudRecargada.marcarSolicitudComoValidada(solicitud.getUsuarioGestion(), vigenciaDias, fechaInicioEntregaInforme);
        if (solicitud.getExtensionFecha() != null) {
            if (solicitudRecargada.getFechaEntregaInforme().isAfter(solicitud.getExtensionFecha())) {
                throw new SolicitudEnsayoErrorException("La extensión de fecha debe ser mayor que la fecha de entrega informe: " + solicitudRecargada.getFechaEntregaInforme());
            }
            solicitudRecargada.setExtensionFecha(solicitud.getExtensionFecha());
            solicitudRecargada.setFechaEntregaInforme(solicitud.getExtensionFecha());

        } else if (solicitudRecargada.getObjetivo().contains("Diseño Vial") && solicitud.getFechaEntregaInforme() != null) {
            solicitudRecargada.setFechaEntregaInforme(solicitud.getFechaEntregaInforme());
            solicitudRecargada.setTiempoEntrega(TiempoEntrega.DEFINIDO_POR_USUARIO);
        }

        LOG.info(String.format("Solicitud id=%s validada..", solicitudRecargada.getId()));
        try {
            this.servicioNotificacionSolicitudEnsayo.notificarIngresoMuestra(solicitudRecargada, solicitud.getObservacion());
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Ingreso de muestra %s", e));
        }
        return true;
    }

    
    @Transactional
    public boolean responderSolicitud(SolicitudBM solicitud) {
        SolicitudBM solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
        documentoServicio.validarInformeSubido(solicitudRecargada.getId(), solicitudRecargada.getEstado());
        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "INFORME ENVIADO" : solicitud.getObservacion();
        agregarHistorial(solicitudRecargada, OrdenFlujo.RESPONDER_SOLICITUD, observacion);

        solicitudRecargada.setProyectoTpda(solicitud.getProyectoTpda());
        solicitudRecargada.setProyectoPorcentajeVehiculosPesados(solicitud.getProyectoPorcentajeVehiculosPesados());
        solicitudRecargada.setProyectoCategoriaVial(solicitud.getProyectoCategoriaVial());
        solicitudRecargada.setTipoLigante(solicitud.getTipoLigante());
        solicitudRecargada.setEjesEquivalentes(solicitud.getEjesEquivalentes());

        solicitudRecargada.marcarSolicitudComoRespondida();

        LOG.info(String.format("Solicitud id=%s respondida..", solicitudRecargada.getId()));
        return true;
    }

    @Transactional
    
    public boolean aprobarInforme(SolicitudBM solicitud) {
        SolicitudBM solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
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
        int vigenciaDias = solicitudRecargada.esDisenioPavimentos() ? configuracionTiempoOP.get().getVigenciaDiasDisenioPavimentos() : configuracionTiempoOP.get().getVigenciaDias();
        solicitudRecargada.marcarSolicitudComoInformeAprobado(configuracionOP.get().getUsuarioId(), vigenciaDias);
        LOG.info(String.format("Solicitud %s, marcada como informe aprobado", solicitudRecargada.getCodigo()));
        return true;
    }

    @Transactional
    
    public boolean rechazarInforme(SolicitudBM solicitud) {
        SolicitudBM solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
        this.agregarHistorial(solicitudRecargada, OrdenFlujo.REVISION_INFORME, solicitud.getObservacion());
        solicitudRecargada.setEstado(EstadoSolicitud.EN_PROCESO);
        LOG.info(String.format("Solicitud %s, marcada como informe rechazado", solicitudRecargada.getCodigo()));
        return true;
    }

    
    @Transactional
    public boolean aprobarSolicitud(SolicitudBM solicitud) {
        Optional<SolicitudBM> solicitudOP = repo.findById(solicitud.getId());

        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudBM solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());
        copiarInforme(solicitudRecargada.getId());

        solicitudRecargada.marcarSolicitudComoAprobada(solicitud.getTipoAprobacion(), solicitud.isRequiereMateriaPrima());
        List<TipoAprobacionSolicitud> estadosRequierePruebasProceso = Arrays.asList(TipoAprobacionSolicitud.SOLICITUD_PRUEBA_PROCESO, TipoAprobacionSolicitud.REQUIERE_PRUEBA_PROCESO);
        if (estadosRequierePruebasProceso.contains(solicitud.getTipoAprobacion()) && !solicitud.isRequiereMateriaPrima()) {
            solicitudRecargada.setEstado(EstadoSolicitud.PENDIENTE_PRUEBAS_PROCESO);
        }
        if (TipoAprobacionSolicitud.APROBADO_DISENIO_VIAL.equals(solicitud.getTipoAprobacion())) {
            solicitudRecargada.setEstado(EstadoSolicitud.PENDIENTE_RECEPCION_INFORME);
        }
        LOG.info(String.format("Solicitud id=%s aprobada, tipo aprobacion %s..", solicitudRecargada.getId(), solicitudRecargada.getTipoAprobacion()));
        try {
            this.servicioNotificacionSolicitudEnsayo.notificarSolicitudFinalizada(solicitudRecargada, solicitud.getObservacion());
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Solicitud Finalizada %s", e));
        }
        return true;
    }

    
    @Transactional
    public boolean confirmarRecepcionInformeSolicitud(SolicitudBM solicitud) {
        Optional<SolicitudBM> solicitudOP = repo.findById(solicitud.getId());
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));
        SolicitudBM solicitudRecargada = solicitudOP.get();
        agregarHistorial(solicitudRecargada, OrdenFlujo.CONFIRMAR_RECEPCION_INFORME, solicitud.getObservacion());
        solicitudRecargada.setEstado(solicitud.getEstado());

        LOG.info(String.format("Solicitud id=%s recepcion informe con estado  %s..", solicitudRecargada.getId(), solicitudRecargada.getEstado()));
        try {
            if (EstadoSolicitud.REGRESADO_NOVEDAD_INFORME.equals(solicitud.getEstado())) {
                this.servicioNotificacionSolicitudEnsayo.notificarSolicitudRecepcionInforme(solicitudRecargada, solicitud.getObservacion());
            }
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Solicitud Finalizada %s", e));
        }
        return true;
    }

    private void copiarInforme(long solicitudId) {
        boolean tieneAdjuntos = this.repoDocumento.existsByOrdenFlujoAndSolicitudEnsayo_Id(OrdenFlujo.APROBAR_INFORME, solicitudId);
        if (!tieneAdjuntos) {
            List<SolicitudDocumento> documentos = this.repoDocumento.findByOrdenFlujoInAndSolicitudEnsayo_Id(Collections.singletonList(OrdenFlujo.REVISION_INFORME), solicitudId);
            List<SolicitudDocumento> documentosNuevos = new ArrayList<>();
            if (!documentos.isEmpty()) {
                documentos.forEach(x -> {
                    documentosNuevos.add(new SolicitudDocumento(x.getSolicitudEnsayo(), x.getPath(), x.getNombreArchivo(), OrdenFlujo.APROBAR_INFORME, x.getTipo()));
                });
                this.repoDocumento.saveAll(documentosNuevos);
            }
        }
    }

    
    @Transactional
    public boolean regresarSolicitud(SolicitudBM solicitud) {
        Optional<SolicitudBM> solicitudOP = repo.findById(solicitud.getId());

        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudBM solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, OrdenFlujo.APROBAR_INFORME, solicitud.getObservacion());

        solicitudRecargada.marcarSolicitudComoRegresada();

        LOG.info(String.format("Solicitud id=%s regresada..", solicitudRecargada.getId()));
        return true;
    }

    @Transactional
    
    public boolean regresarSolicitudForma(SolicitudBM solicitud) {
        SolicitudBM solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
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

    
    @Transactional
    public boolean anularSolicitud(SolicitudBM solicitud) {
        Optional<SolicitudBM> solicitudOP = repo.findById(solicitud.getId());

        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudBM solicitudRecargada = solicitudOP.get();

        agregarHistorial(solicitudRecargada, solicitud.getOrden(), solicitud.getObservacion());

        solicitudRecargada.anular();

        LOG.info(String.format("Solicitud id=%s anulada..", solicitudRecargada.getId()));
        return true;
    }

    
    @Transactional
    public boolean rechazarSolicitud(SolicitudBM solicitud) {
        Optional<SolicitudBM> solicitudOP = repo.findById(solicitud.getId());
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", solicitud.getId()));

        SolicitudBM solicitudRecargada = solicitudOP.get();
        agregarHistorial(solicitudRecargada, solicitud.getOrden(), solicitud.getObservacion());
        solicitudRecargada.rechazar();

        try {
            this.servicioNotificacionSolicitudEnsayo.notificarSolicitudEstado(solicitudRecargada, solicitud.getObservacion());
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Solicitud rechazada %s", e));
        }
        LOG.info(String.format("Solicitud id=%s rechazada..", solicitudRecargada.getId()));
        return true;
    }

    
    @Transactional(readOnly = true)
    public Page<SolicitudDTO> consultar(Pageable pageable, ConsultaSolicitudDTO dto) {
        try {
            List<SolicitudBM> entidades = obtenerSolicitudesEnsayo(dto, pageable);
            long total = contarTotales(dto);
            List<SolicitudDTO> dtos = this.ensayoMapper.fromListToDto(entidades);
            return new PageImpl<>(dtos, pageable, total);
        } catch (Exception e) {
            final Page<SolicitudDTO> pageResult = new PageImpl<SolicitudDTO>(new ArrayList<SolicitudDTO>(), pageable, 0);
            return pageResult;
        }
    }

    @Transactional
    
    public SolicitudBM crearSolicitudAPartirDeOtra(long solicitudId) {
        SolicitudBM solicitud = this.obtenerSolicitudPorId(solicitudId);
        this.validarUnicaPruebaEnviadaNuevamenteEnCurso(solicitudId);
        SolicitudBM solicitudNueva = this.create(solicitud);
        solicitudNueva.setSolicitudPadreId(solicitud.getId());
        LOG.info(String.format("Solicitud creada para reenviar -> solicitud origen %s :: solicitud nueva %s", solicitud.getCodigo(), solicitudNueva));
        return solicitudNueva;
    }

    @Transactional
    
    public boolean finalizarProceso(SolicitudBM solicitud) {
        SolicitudBM SolicitudBM = this.obtenerSolicitudPorId(solicitud.getId());
        String observacion = noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "PROCESO FINALIZADO";
        this.agregarHistorial(SolicitudBM, OrdenFlujo.SOLICITANTE_PLANES_ACCION, observacion);
        SolicitudBM.setEstado(EstadoSolicitud.PROCESO_FINALIZADO);
        LOG.info(String.format("Solicitud %s marcada como proceso finalizado", SolicitudBM.getCodigo()));
        return true;
    }

    @Transactional
    
    public boolean confirmarPlanesAccion(SolicitudBM solicitud) {
        SolicitudBM SolicitudBM = this.obtenerSolicitudPorId(solicitud.getId());
        String observacion = noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "PLANES DE ACCIÓN INGRESADOS";
        this.agregarHistorial(SolicitudBM, OrdenFlujo.SOLICITANTE_PLANES_ACCION, observacion);
        SolicitudBM.setEstado(EstadoSolicitud.PENDIENTE_PLANES_ACCION);
        LOG.info(String.format("Solicitud %s planes de accion confirmados y enviados a revision", SolicitudBM.getCodigo()));
        return true;
    }

    @Transactional
    
    public boolean finalizarRevisionPlanesAccion(SolicitudBM solicitud) {
        SolicitudBM SolicitudBM = this.obtenerSolicitudPorId(solicitud.getId());
        String observacion = noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "REVISIÓN FINALIZADA";
        this.agregarHistorial(SolicitudBM, OrdenFlujo.REVISION_PLANES_ACCION, observacion);
        List<TipoAprobacionSolicitud> estadosRequierePruebasProceso = Arrays.asList(TipoAprobacionSolicitud.SOLICITUD_PRUEBA_PROCESO, TipoAprobacionSolicitud.REQUIERE_PRUEBA_PROCESO);
        if (estadosRequierePruebasProceso.contains(SolicitudBM.getTipoAprobacion())) {
            SolicitudBM.setEstado(EstadoSolicitud.PENDIENTE_PRUEBAS_PROCESO);
        } else {
            SolicitudBM.setEstado(EstadoSolicitud.PLANES_ACCION_REVISADOS);
        }
        LOG.info(String.format("Solicitud %s planes de accion revisados, tipo aprobacion: %s", SolicitudBM.getCodigo(), SolicitudBM.getTipoAprobacion()));
        return true;
    }

    @Transactional
    
    public SolicitudPruebasProceso iniciarPruebaEnProceso(SolicitudBM solicitud) {
        SolicitudBM SolicitudBM = this.obtenerSolicitudPorId(solicitud.getId());
        SolicitudPruebasProceso nuevo = this.pruebasProcesoService.create(new SolicitudPruebasProceso(
                "",
                nombreUsuarioEnSesion(),
                SolicitudBM.getFechaEntrega(),
                this.obtenerAreaUsuarioEnSesion()
        ));
        String observacion = nuevo.getCodigo().concat(" :::").concat(noEsNuloNiBlanco(solicitud.getObservacion()) ? solicitud.getObservacion() : "PROCESO INICIADO");
        this.agregarHistorial(SolicitudBM, OrdenFlujo.SOLICITUD_PRUEBAS_PROCESO, observacion);
        SolicitudBM.setEstado(EstadoSolicitud.GESTION_PRUEBAS_PROCESO);
        SolicitudBM.setSolicitudPruebaProcesoId(nuevo.getId());
        return nuevo;
    }

    @Transactional(readOnly = true)
    
    public List<SolicitudBM> obtenerSolicitudesPendientesPlanesAccion() {
        String usuario = nombreUsuarioEnSesion();
        return this.repo.findByEstadoAndValidadorOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_PLANES_ACCION, usuario);
    }

    @Transactional
    
    public boolean solicitudExtensionPlazo(SolicitudBM solicitud) {
        SolicitudBM solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
//        documentoServicio.validarInformeSubido(solicitudRecargada.getId(), solicitudRecargada.getEstado());
        ConfiguracionUsuarioRolEnsayo configuracionOP = repoConfiguracion.findByOrdenAndTipoSolicitud(OrdenFlujo.APROBACION_EXTENSION_PLAZO,
                        TipoSolicitud.SOLICITUD_ENSAYOS)
                .orElseThrow(() -> new SolicitudEnsayoErrorException(String.format("Configuración para el rol %s no existe.", OrdenFlujo.APROBACION_EXTENSION_PLAZO)));

        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "SOLICITUD DE EXTENSIÓN DE PLAZO ENVIADA" : solicitud.getObservacion();
        agregarHistorial(solicitudRecargada, OrdenFlujo.RESPONDER_SOLICITUD, observacion);
        solicitudRecargada.setEstado(EstadoSolicitud.PENDIENTE_APROBACION_EXTENSION_PLAZO);
        solicitudRecargada.setUsuarioAprobadorExtensionPlazo(configuracionOP.getUsuarioId());
        SolicitudExtensionPlazo extensionPlazo = new SolicitudExtensionPlazo(configuracionOP.getUsuarioId(), observacion, solicitudRecargada.getFechaEntregaInforme());
        solicitudRecargada.agregarExtensionPlazo(extensionPlazo);
        LOG.info(String.format("Solicitud id=%s enviada a aprobacion de extension plazo..", solicitudRecargada.getId()));
        try {
            this.servicioNotificacionSolicitudEnsayo.notificarSolicitudExtensionIngreso(solicitudRecargada, solicitud.getObservacion());
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar Solicitud Extension Plazo %s", e));
        }
        return true;
    }

    @Transactional(readOnly = true)
    
    public List<SolicitudBM> obtenerSolicitudesPendienteExtensionPlazo() {
        return this.repo.findByEstadoAndUsuarioAprobadorExtensionPlazoOrderByFechaCreacionDesc(EstadoSolicitud.PENDIENTE_APROBACION_EXTENSION_PLAZO, nombreUsuarioEnSesion());
    }

    @Transactional
    
    public void ejecutarAccionExtensionPlazo(SolicitudBM solicitud) {
        SolicitudBM solicitudRecargada = this.obtenerSolicitudPorId(solicitud.getId());
        EstadoExtensionPlazo estadoFinal = EstadoExtensionPlazo.RECHAZADA;
        if (solicitud.getExtensionFecha() != null) {
            solicitudRecargada.setFechaEntregaInforme(solicitud.getExtensionFecha());
            solicitudRecargada.setExtensionFecha(solicitud.getExtensionFecha());
            estadoFinal = EstadoExtensionPlazo.APROBADA;
            this.actualizarSolicitudExtension(solicitudRecargada, estadoFinal);
        }
        String observacion = esNuloOBlanco(solicitud.getObservacion()) ? "SOLICITUD EXTENSIÓN DE PLAZO " + estadoFinal : solicitud.getObservacion();
        agregarHistorial(solicitudRecargada, OrdenFlujo.APROBACION_EXTENSION_PLAZO, observacion);
        solicitudRecargada.setEstado(EstadoSolicitud.EN_PROCESO);
        try {
            this.servicioNotificacionSolicitudEnsayo.notificarSolicitudExtensionEstado(solicitudRecargada, observacion, estadoFinal);
        } catch (Exception e) {
            LOG.error(String.format("Error al notificar estado de Solicitud Extension Plazo %s", e));
        }
    }

    @Transactional
    
    public List<SolicitudEnsayoMinaDto> agregarMina(long solicitudId, SolicitudEnsayoMinaDto dto) {
        SolicitudBM solicitud = this.obtenerSolicitudPorId(solicitudId);
        Mina mina = this.minaRepo.findById(dto.getMina().getId()).orElseThrow(() ->
                new SolicitudEnsayoErrorException(String.format("Mina con id %s no existe.", dto.getMina().getId())));
        this.validarUnicaMina(solicitud, mina);

        List<SolicitudEnsayoMinaAgregados> agregados = dto.getAgregados().stream()
                .map(this.solicitudEnsayoMinaAgregadosMapper::fromDtoToEntity).collect(Collectors.toList());

        SolicitudEnsayoMina minaTmp = new SolicitudEnsayoMina(mina, agregados);
        solicitud.agregarMina(minaTmp);
        repo.save(solicitud);
        LOG.info(String.format("Solicitud ensayo %s, Mina agregada %s", solicitud.getCodigo(), minaTmp));
        return solicitudEnsayoMinaMapper.fromListToDto(solicitud.getMinas());
    }

    @Transactional
    
    public List<SolicitudEnsayoMinaDto> eliminarMina(long solicitudId, long minaId) {
        SolicitudBM solicitud = this.obtenerSolicitudPorId(solicitudId);
        solicitud.getMinas().removeIf(x -> x.getId() == minaId);
        repo.save(solicitud);
        LOG.info(String.format("Solicitud ensayo %s, Mina eliminada %s", solicitud.getCodigo(), minaId));
        return solicitudEnsayoMinaMapper.fromListToDto(solicitud.getMinas());
    }

    @Transactional
    
    public List<SolicitudEnsayoMinaDto> modificarMinaAgregados(long solicitudId, SolicitudEnsayoMinaDto dto) {
        SolicitudBM solicitud = this.obtenerSolicitudPorId(solicitudId);
        SolicitudEnsayoMina mina = solicitud.getMinas().stream().filter(x -> x.getId() == dto.getId()).findFirst()
                .orElseThrow(() -> new SolicitudEnsayoErrorException(String.format("Mina con id %s no existe.", dto.getId())));
        List<SolicitudEnsayoMinaAgregados> agregados = dto.getAgregados().stream()
                .map(this.solicitudEnsayoMinaAgregadosMapper::fromDtoToEntity).collect(Collectors.toList());
        mina.getAgregados().clear();
        mina.getAgregados().addAll(agregados);
        repo.save(solicitud);
        LOG.info(String.format("Solicitud ensayo %s, Mina actualizada %s", solicitud.getCodigo(), mina));
        return solicitudEnsayoMinaMapper.fromListToDto(solicitud.getMinas());
    }

    private void actualizarSolicitudExtension(SolicitudBM SolicitudBM, EstadoExtensionPlazo estadoExtensionPlazo) {
        SolicitudExtensionPlazo solicitudExtensionPlazo = SolicitudBM.getExtensionesPlazo().stream()
                .filter(x -> x.getEstado().equals(EstadoExtensionPlazo.PENDIENTE))
                .filter(x -> x.getUsuarioAprobador().equals(nombreUsuarioEnSesion()))
                .findFirst().orElseThrow(() -> new SolicitudEnsayoErrorException("No se encontro solicitud de extensión de plazo"));
        solicitudExtensionPlazo.marcarAprobacion(estadoExtensionPlazo, SolicitudBM.getFechaEntregaInforme());
    }

    private List<SolicitudBM> obtenerSolicitudesEnsayo(ConsultaSolicitudDTO consulta, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SolicitudBM> query = cb.createQuery(SolicitudBM.class);
        Root<SolicitudBM> root = query.from(SolicitudBM.class);
        // Aplicamos los predicados compartidos
        List<Predicate> predicados = construirPredicados(cb, root, consulta);
        query.where(cb.and(predicados.toArray(new Predicate[0])));
        // Opcional: Agregar ordenamiento por defecto
        query.orderBy(cb.desc(root.get("fechaCreacion")));

        TypedQuery<SolicitudBM> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    private List<Predicate> construirPredicados(CriteriaBuilder criteriaBuilder, Root<SolicitudBM> root, ConsultaSolicitudDTO consulta) {
        List<Predicate> predicadosConsulta = new ArrayList<>();
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
//        Filtros Proyecto
        if (noEsNuloNiBlanco(consulta.getProyectoNombre())) {
            predicadosConsulta.add(criteriaBuilder.like(root.get("proyectoNombre"), "%" + consulta.getProyectoNombre() + "%"));
        }
        if (noEsNuloNiBlanco(consulta.getProyectoPropietario())) {
            predicadosConsulta.add(criteriaBuilder.like(root.get("proyectoPropietario"), "%" + consulta.getProyectoPropietario() + "%"));
        }
        if (consulta.getProyectoIniciado() != null) {
            predicadosConsulta.add(criteriaBuilder.equal(root.get("proyectoIniciado"), consulta.getProyectoIniciado()));
        }

//        JOIN con la tabla mina filtros Mina
        if (consulta.getMinaId() > 0 || noEsNuloNiBlanco(consulta.getCanton()) || noEsNuloNiBlanco(consulta.getProvincia())) {
            Join<SolicitudBM, SolicitudEnsayoMina> solicitudEnsayoMinaJoin = root.join("minas", JoinType.INNER);
            Join<SolicitudEnsayoMina, Mina> minaJoin = solicitudEnsayoMinaJoin.join("mina", JoinType.INNER);
            if (consulta.getMinaId() > 0) {
                predicadosConsulta.add(criteriaBuilder.equal(minaJoin.get("id"), consulta.getMinaId()));
            }
            if (noEsNuloNiBlanco(consulta.getCanton())) {
                predicadosConsulta.add(criteriaBuilder.equal(minaJoin.get("canton"), consulta.getCanton()));
            }
            if (noEsNuloNiBlanco(consulta.getProvincia())) {
                predicadosConsulta.add(criteriaBuilder.equal(minaJoin.get("provincia"), consulta.getProvincia()));
            }
        }

        return predicadosConsulta;
    }

    private long contarTotales(ConsultaSolicitudDTO consulta) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<SolicitudBM> root = countQuery.from(SolicitudBM.class);

        // Usamos EXACTAMENTE el mismo método de predicados
        List<Predicate> predicados = construirPredicados(cb, root, consulta);

        countQuery.select(cb.count(root)).where(cb.and(predicados.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<SolicitudEnsayoAdjuntoRequerido> crearAdjuntosRequeridos() {
        return this.configuracionAdjuntoRequeridoRepo.findAll()
                .stream()
                .map(x -> new SolicitudEnsayoAdjuntoRequerido(x.getNombre(), x.getSecuencia(), x.isObligatorio()))
                .collect(Collectors.toList());
    }

    private SolicitudBM obtenerSolicitudPorId(long id) {
        Optional<SolicitudBM> solicitudOP = repo.findById(id);
        if (!solicitudOP.isPresent())
            throw new SolicitudEnsayoErrorException(String.format("Solicitud con id %s no existe.", id));
        return solicitudOP.get();
    }

    private void validarUnicaPruebaEnviadaNuevamenteEnCurso(long solicitudPadreId) {
        List<EstadoSolicitud> estados = Arrays.asList(EstadoSolicitud.ANULADO, EstadoSolicitud.FINALIZADO);
//        List<EstadoSolicitud> estadosFinalizados = Arrays.asList(EstadoSolicitud.LIBRE_USO, EstadoSolicitud.CREACION_MATERIA_PRIMA,
//                EstadoSolicitud.GESTIONAR_IMPLEMENTAR_CAMBIOS, EstadoSolicitud.ENVIAR_SOLUCIONES_TECNICAS);
        List<SolicitudBM> solicitudesEnProceso = this.repo.findBySolicitudPadreId(solicitudPadreId);
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
            throw new SolicitudEnsayoErrorException("Configuración día máximo entrega muestras no existe");
        return Integer.parseInt(diaMaxEntregaInforme.get().getValorConfiguracion());
    }

    private void validarUnicaMina(SolicitudBM SolicitudBM, Mina mina) {
        boolean minaEncontrada = SolicitudBM.getMinas()
                .stream()
                .anyMatch(x -> x.getMina().getId() == mina.getId());
        if (minaEncontrada) {
            throw new SolicitudEnsayoErrorException(String.format("Mina %s ya se encuentra agregada", mina.getNombre()));
        }
    }
}
