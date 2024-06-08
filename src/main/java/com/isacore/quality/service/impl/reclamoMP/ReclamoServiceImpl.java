package com.isacore.quality.service.impl.reclamoMP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.exception.reporte.JasperReportsException;
import com.isacore.exception.reporte.ReporteExeption;
import com.isacore.notificacion.dominio.Adjunto;
import com.isacore.notificacion.servicio.ServicioNotificacionReclamoMP;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.mapper.reclamoMP.ProblemaMapper;
import com.isacore.quality.mapper.reclamoMP.ReclamoAccionEjecutadasMapper;
import com.isacore.quality.mapper.reclamoMP.ReclamoMapper;
import com.isacore.quality.mapper.reclamoMP.ReclamoPlanesAccionMapper;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.Provider;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.reclamoMP.*;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoResponsable;
import com.isacore.quality.repository.IProductRepo;
import com.isacore.quality.repository.IProviderRepo;
import com.isacore.quality.repository.IUnidadMedidadRepo;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.repository.reclamoMP.IComplaintRepo;
import com.isacore.quality.service.reclamoMP.IComplaintHistorialService;
import com.isacore.quality.service.reclamoMP.IComplaintService;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.servicio.reporte.IGeneradorJasperReports;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.repository.IUserImptekRepo;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.UtilidadesArchivo;
import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesSeguridad;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesCadena.esNuloOBlanco;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReclamoServiceImpl implements IComplaintService {

    private final IComplaintRepo reclamoRepo;
    private final IProductRepo productoRepo;
    private final IProviderRepo providerRepo;
    private final ReclamoMapper reclamoMapper;
    private final EntityManager entityManager;
    private final IUserImptekRepo usuarioRepo;
    private final IUnidadMedidadRepo unidadMedidadRepo;
    private final ConfiguracionSolicitud configuracion;
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private final ProblemaMapper problemaMapper;
    private final IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;
    private final IComplaintHistorialService historialService;
    private final IGeneradorJasperReports reporteServicio;
    private final ServicioNotificacionReclamoMP servicioNotificacion;
    private final ReclamoPlanesAccionMapper reclamoPlanesAccionMapper;
    private final ReclamoAccionEjecutadasMapper reclamoAccionEjecutadasMapper;

    @Override
    @Transactional
    public void close(ComplaintDto dto) {
        Complaint complaint = this.obtenerporId(dto.getId());
        ComplaintEstado estadoOriginal = complaint.getState();
        complaint.setState(ComplaintEstado.CERRADO);
        complaint.setCloseDate(LocalDateTime.now());
        historialService.agregar(complaint, estadoOriginal, ComplaintOrdenFlujo.GESTION_CALIDAD,
                UtilidadesCadena.esNuloOBlanco(dto.getObservacion()) ? "RECLAMO CERRADO" : dto.getObservacion());
        log.info(String.format("Reclamo de MP id=%s ha sido CERRADO", complaint.getId()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ComplaintDto> listarPorCriterio(Pageable pageable, ConsultaReclamoDto dto) {
        try {

            List<Complaint> respuesta = new ArrayList<>();
            respuesta.addAll(obtenerDesviacionesPorCriterios(dto));
            final int sizeTotal = respuesta.size();

            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > respuesta.size() ? respuesta.size()
                    : (start + pageable.getPageSize());

            respuesta = respuesta.subList(start, end);

            List<ComplaintDto> listaMapeada = this.reclamoMapper.fromListReclamoToListDto(respuesta);
            final Page<ComplaintDto> pageResut = new PageImpl<>(listaMapeada, pageable, sizeTotal);
            return pageResut;

        } catch (Exception e) {
            final Page<ComplaintDto> pageResult = new PageImpl<ComplaintDto>(new ArrayList<ComplaintDto>(), pageable, 0);
            return pageResult;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ComplaintDto buscarPorId(long id) {
        Complaint reclamo = obtenerporId(id);
        return this.reclamoMapper.fromReclamoToDto(reclamo);
    }

    @Transactional
    @Override
    public List<ProblemDto> agregarProblema(String jsonDTO, byte[] file, String nombreArchivo, String tipo) {
        try {
            ProblemDto dto = JSON_MAPPER.readValue(jsonDTO, ProblemDto.class);
            if (dto != null) {
                Complaint reclamo = obtenerporId(dto.getReclamoId());
                String path = "";
                if (file.length > 0) {
                    path = UtilidadesArchivo.crearPathArchivo(reclamo.getNumber(), nombreArchivo, configuracion.getRutaBase(), "RECLAMO_MP");
                    PassFileToRepository.saveLocalFile(path, file);
                }

                if (dto.getId() > 0) {
                    this.actualizarProblema(reclamo, dto, path, nombreArchivo, tipo);
                } else {
                    this.crearProblema(reclamo, dto, path, nombreArchivo, tipo);
                }
                this.reclamoRepo.save(reclamo);
                return this.problemaMapper.fromListProblemToListDto(reclamo.getListProblems());
            }
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        } catch (IOException e) {
            throw new PncErrorException("Error al cargar el documento");
        }
    }

    private void crearProblema(Complaint reclamo, ProblemDto dto, String path, String nombreArchivo, String tipo) {
        Problem problema = new Problem(
                dto.getDefectoId(),
                dto.getDescription(),
                path,
                nombreArchivo,
                tipo
        );
        reclamo.agregarProblema(problema);
        log.info(String.format("Reclamo %s : Problema guardado %s", reclamo.getNumber(), problema));
    }

    private void actualizarProblema(Complaint reclamo, ProblemDto dto, String path, String nombreArchivo, String tipo) {
        Optional<Problem> problema = reclamo.getListProblems().stream().filter(x -> x.getId() == dto.getId()).findFirst();
        if (problema.isPresent()) {
            problema.get().setDescription(dto.getDescription());
            problema.get().setDefectoId(dto.getDefectoId());
            if (UtilidadesCadena.noEsNuloNiBlanco(path)) {
                PassFileToRepository.eliminarArchivoFisico(problema.get().getPictureStringB64());
                problema.get().setPictureStringB64(path);
                problema.get().setExtensionFileP(tipo);
                problema.get().setNameFileP(nombreArchivo);
            }
            log.info(String.format("Reclamo %s : Problema actualizar %s", reclamo.getNumber(), problema));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProblemDto> listarProblemas(long complaint) {
        Complaint reclamo = obtenerporId(complaint);
        return this.problemaMapper.fromListProblemToListDto(reclamo.getListProblems());
    }

    @Transactional
    @Override
    public List<ProblemDto> eliminarProblema(long reclamoId, long problemaId) {
        Complaint reclamo = this.obtenerporId(reclamoId);
        Problem problema = reclamo.getListProblems().stream().filter(x -> x.getId() == problemaId)
                .findFirst().orElseThrow(() -> new PncErrorException("Problema no encontrado"));

        log.info(String.format("Reclamo %s : Problema eliminado %s", reclamo.getNumber(), problema));
        PassFileToRepository.eliminarArchivoFisico(problema.getPictureStringB64());
        reclamo.eliminarProblema(problema.getId());
        this.reclamoRepo.save(reclamo);
        return this.problemaMapper.fromListProblemToListDto(reclamo.getListProblems());
    }

    @Transactional
    @Override
    public void enviarReclamo(ComplaintDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getId());
        ComplaintEstado estadoOriginal = reclamo.getState();
        String aprobadorCalidad = obtenerAprobador(NombreConfiguracionFlujo.APROBADOR_CALIDAD_RECLAMO_MP);
        String observacion = UtilidadesCadena.esNuloOBlanco(dto.getObservacion()) ? "RECLAMO ENVIADO" : dto.getObservacion();
        reclamo.setAprobadorCalidad(aprobadorCalidad);
        reclamo.setState(ComplaintEstado.PENDIENTE_APROBACION_CALIDAD);
        historialService.agregar(reclamo, estadoOriginal, ComplaintOrdenFlujo.INGRESO, observacion);

        try {
            this.servicioNotificacion.notificarPendienteAprobacion(reclamo, observacion, ComplaintOrdenFlujo.APROBACION_CALIDAD);
        } catch (Exception e) {
            log.error(String.format("Error al notificar envio de RECLAMO DE MATERIA PRIMA: %s", e));
        }
    }

    @Transactional
    @Override
    public void procesarReclamoCalidad(ComplaintDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getId());
        String obs = dto.getObservacion();
        ComplaintEstado estadoOriginal = reclamo.getState();
        reclamo.setState(dto.getAccion());
        reclamo.setFechaAprobadorCalidad(LocalDateTime.now());
        if (UtilidadesCadena.esNuloOBlanco(dto.getObservacion()))
            obs = "SOLICITUD APROBADA";

        this.historialService.agregar(reclamo, estadoOriginal, ComplaintOrdenFlujo.APROBACION_CALIDAD, obs);
        try {
            this.servicioNotificacion.notificarCambioEstado(reclamo, obs, UtilidadesSeguridad.nombreUsuarioEnSesion());
        } catch (Exception e) {
            log.error(String.format("Error al notificar envio de RECLAMO DE MATERIA PRIMA: %s", e));
        }
    }

    @Transactional
    @Override
    public void procesarReclamoCompras(ComplaintDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getId());
        String obs = dto.getObservacion();
        ComplaintEstado estadoOriginal = reclamo.getState();
        reclamo.setState(dto.getAccion());
        reclamo.setFechaAprobadorCompras(LocalDateTime.now());
        if (dto.getAccion().equals(ComplaintEstado.APROBADO)) {
            if (UtilidadesCadena.esNuloOBlanco(dto.getObservacion()))
                obs = "SOLICITUD APROBADA";
        }
        this.historialService.agregar(reclamo, estadoOriginal, ComplaintOrdenFlujo.APROBACION_COMPRAS, obs);

        if (dto.getAccion().equals(ComplaintEstado.RECHAZADO)) {
            try {
                this.servicioNotificacion.notificarCambioEstado(reclamo, obs, UtilidadesSeguridad.nombreUsuarioEnSesion());
            } catch (Exception e) {
                log.error(String.format("Error al notificar envio de RECLAMO DE MATERIA PRIMA: %s", e));
            }
        }

    }

    @Transactional
    @Override
    public void anular(ComplaintDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getId());
        reclamo.setState(ComplaintEstado.ANULADO);
        ComplaintEstado estadoOriginal = reclamo.getState();
        this.historialService.agregar(reclamo, estadoOriginal, ComplaintOrdenFlujo.INGRESO, dto.getObservacion());
        log.info("Reclamo MP {} anulado", dto.getNumber());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ComplaintDto> listarAsignadasPorEstado(ComplaintEstado estado) {
        List<Complaint> reclamos = new ArrayList<>();
        String usuarioSesion = UtilidadesSeguridad.nombreUsuarioEnSesion();
        if (estado.equals(ComplaintEstado.PENDIENTE_APROBACION_CALIDAD))
            reclamos = reclamoRepo.findByStateAndAprobadorCalidad(estado, usuarioSesion);
        else if (estado.equals(ComplaintEstado.PENDIENTE_APROBACION_COMPRAS))
            reclamos = reclamoRepo.findByStateAndAprobadorCompras(estado, usuarioSesion);

        return this.reclamoMapper.fromListReclamoToListDto(reclamos);
    }

    @Transactional(readOnly = true)
    @Override
    public byte[] generateReporte(long id) {
        Complaint reclamo = this.obtenerporId(id);
        try {
            return reporteServicio.generarReporte("Complaint", Collections.singleton(this.reclamoMapper.fromReclamoToDto(reclamo)), new HashMap<>());
        } catch (JasperReportsException e) {
            log.error(String.format("Error Reclamo MP Reporte: %s", e));
            throw new ReporteExeption("Reclamo MP");
        }
    }

    @Transactional
    @Override
    public List<ProviderActionPlanDto> agregarPLanAccion(ProviderActionPlanDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getIdReclamo());
        reclamo.agregarPlanAccion(new ProviderActionPlan(dto.getDescription(),
                dto.getDateLimit(), dto.getDateStart(), dto.getResponsable()));
        reclamoRepo.save(reclamo);
        return this.reclamoPlanesAccionMapper.fromListToListDto(reclamo.getListActionsPlanProvider());
    }

    @Transactional
    @Override
    public List<ProviderActionPlanDto> actualizarPLanAccion(ProviderActionPlanDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getIdReclamo());
        ProviderActionPlan plan = reclamo.getListActionsPlanProvider()
                .stream().filter(x -> x.getId() == dto.getId())
                .findFirst()
                .orElseThrow(() -> new PncErrorException("Plan de acción no encontrado"));
        if (!plan.getEstado().equals(ComplaintPlanAccionEstado.CREADA))
            throw new PncErrorException("El estado del Plan de acción no permite modifcaciones");

        plan.setDescription(dto.getDescription());
        plan.setDateStart(dto.getDateStart());
        plan.setDateLimit(dto.getDateLimit());
        plan.setResponsable(dto.getResponsable());
        log.info("Reclamo MP {} modificando el plan de accion {}", reclamo.getNumber(), plan);
        this.reclamoRepo.save(reclamo);

        return this.reclamoPlanesAccionMapper.fromListToListDto(reclamo.getListActionsPlanProvider());
    }

    @Transactional
    @Override
    public List<ProviderActionPlanDto> eliminarPlanAccion(long reclamoId, long planAccionId) {
        Complaint reclamo = this.obtenerporId(reclamoId);
        ProviderActionPlan problema = reclamo.getListActionsPlanProvider().stream().filter(x -> x.getId() == planAccionId)
                .findFirst().orElseThrow(() -> new PncErrorException("Problema no encontrado"));

        log.info(String.format("Reclamo %s : Plan de accion eliminado %s", reclamo.getNumber(), problema));
        reclamo.eliminarPlanAccion(problema.getId());
        this.reclamoRepo.save(reclamo);
        return this.reclamoPlanesAccionMapper.fromListToListDto(reclamo.getListActionsPlanProvider());
    }

    @Transactional
    @Override
    public List<ExecutedActionDto> agregarAccionEjecutada(ExecutedActionDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getIdReclamo());
        reclamo.agregarAccionEjecutada(new ExecutedAction(dto.getDescription()));
        reclamoRepo.save(reclamo);
        return this.reclamoAccionEjecutadasMapper.fromListToListDto(reclamo.getListExecutedActons());
    }

    @Transactional
    @Override
    public List<ExecutedActionDto> actualizarAccionEjecutada(ExecutedActionDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getIdReclamo());
        ExecutedAction plan = reclamo.getListExecutedActons()
                .stream().filter(x -> x.getId() == dto.getId())
                .findFirst()
                .orElseThrow(() -> new PncErrorException("Acción no encontrada"));

        plan.setDescription(dto.getDescription());
        log.info("Reclamo MP {} modificando el accion ejecutada {}", reclamo.getNumber(), plan);
        this.reclamoRepo.save(reclamo);
        return this.reclamoAccionEjecutadasMapper.fromListToListDto(reclamo.getListExecutedActons());
    }

    @Transactional
    @Override
    public List<ExecutedActionDto> eliminarAccionEjecutada(long reclamoId, long planAccionId) {
        Complaint reclamo = this.obtenerporId(reclamoId);
        ExecutedAction problema = reclamo.getListExecutedActons().stream().filter(x -> x.getId() == planAccionId)
                .findFirst().orElseThrow(() -> new PncErrorException("Accioón ejecutada no encontrado"));

        log.info(String.format("Reclamo %s : Accion ejecutada eliminado %s", reclamo.getNumber(), problema));
        reclamo.eliminarAccionEjecutada(problema.getId());
        this.reclamoRepo.save(reclamo);
        return this.reclamoAccionEjecutadasMapper.fromListToListDto(reclamo.getListExecutedActons());
    }

    //@Async
    @Override
    public void notificarReclamo(ComplaintDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getId());
        try {
            byte[] reporteBytes = reporteServicio.generarReporte("Complaint",
                    Collections.singleton(this.reclamoMapper.fromReclamoToDto(reclamo)), new HashMap<>());
            String nombreReporte = "Reclamo".concat(String.valueOf(reclamo.getNumber())).concat(".pdf");
            List<Adjunto> adjuntos = Collections.singletonList(new Adjunto(nombreReporte, reporteBytes));
            this.servicioNotificacion.notificarReclamoAprobado(reclamo, dto.getMensaje(), dto.getDestinatarios(), adjuntos, dto.getAsunto());
        } catch (Exception e) {
            log.error(String.format("Error al notificar envio de RECLAMO DE MATERIA PRIMA: %s", e));
        }

    }

    @Transactional
    @Override
    public void enviarPlanesAccion(ComplaintDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getId());
        reclamo.getListActionsPlanProvider().stream().filter(x -> x.getEstado().equals(ComplaintPlanAccionEstado.CREADA))
                .forEach(ProviderActionPlan::marcarComoEnviadas);
        log.info("Reclamo MP {} Se envian los planes de accion para procesar.", reclamo.getNumber());
        this.reclamoRepo.save(reclamo);
        this.historialService.agregar(reclamo, reclamo.getState(), ComplaintOrdenFlujo.GESTION_PLANES_ACCION, dto.getObservacion());
        //TODO: Hacer notificacion

        Map<String, List<ProviderActionPlan>> planes = reclamo.getListActionsPlanProvider()
                .stream()
                .filter(x -> x.getEstado().equals(ComplaintPlanAccionEstado.ASIGNADA))
                .collect(Collectors.groupingBy(ProviderActionPlan::getResponsable));
        planes.forEach((usuario, planesAcciones) -> {
            try {
                this.servicioNotificacion.notificarPlanAccionAsignado(reclamo, planesAcciones);
            } catch (Exception ex) {
                log.error(String.format("Error al notificar planes de asigancion de RECLAMO DE MATERIA PRIMA: %s", ex));
            }
        });
    }

    @Transactional(readOnly = true)
    @Override
    public List<ComplaintDto> listarPorPlanerAccionPorUsuarioSesion() {
        String usuarioSesion = UtilidadesSeguridad.nombreUsuarioEnSesion();
        List<Complaint> reclamos = this.reclamoRepo.findByPlanesAccionPorUsuarioSesion(usuarioSesion);
        List<ComplaintDto> reclamosDto = this.reclamoMapper.fromListReclamoToListDto(reclamos);
        for (ComplaintDto dto : reclamosDto) {
            dto.getListActionsPlanProvider().removeIf(x -> !x.getResponsable().equals(usuarioSesion));
        }
        return reclamosDto;
    }

    @Transactional
    @Override
    public List<ProviderActionPlanDto> procesarPlanAccion(ProviderActionPlanDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getIdReclamo());
        ProviderActionPlan planAccion = reclamo.getListActionsPlanProvider().stream()
                .filter(x -> x.getId() == dto.getId()).findFirst()
                .orElseThrow(() -> new PncErrorException("Plan de acción no encontrado"));
        planAccion.setEstado(ComplaintPlanAccionEstado.PENDIENTE_APROBACION);
        reclamoRepo.save(reclamo);
        String observacion = UtilidadesCadena.noEsNuloNiBlanco(dto.getObservacion()) ? dto.getObservacion() : "Plan de acción finalizado";
        this.historialService.agregar(reclamo, reclamo.getState(), ComplaintOrdenFlujo.PROCESAR_PLANES_ACCION, observacion, planAccion);
        try {
            this.servicioNotificacion.notificarPlanAccionEstado(reclamo, Collections.singletonList(planAccion),ComplaintPlanAccionEstado.PENDIENTE_APROBACION, observacion, UtilidadesSeguridad.nombreUsuarioEnSesion());
        } catch (Exception ex) {
            log.error(String.format("Error al notificar planes de asigancion de RECLAMO DE MATERIA PRIMA: %s", ex));
        }
        return this.reclamoPlanesAccionMapper.fromListToListDto(reclamo.getListActionsPlanProvider());
    }

    @Transactional
    @Override
    public List<ProviderActionPlanDto> validarPlanAccion(ProviderActionPlanDto dto) {
        Complaint reclamo = this.obtenerporId(dto.getIdReclamo());
        ProviderActionPlan planAccion = reclamo.getListActionsPlanProvider().stream()
                .filter(x -> x.getId() == dto.getId()).findFirst()
                .orElseThrow(() -> new PncErrorException("Plan de acción no encontrado"));
        planAccion.setEstado(dto.getEstado());
        if(dto.equals(ComplaintPlanAccionEstado.FINALIZADO))
            planAccion.setFechaCierre(LocalDateTime.now());
        String observacion = UtilidadesCadena.noEsNuloNiBlanco(dto.getObservacion()) ? dto.getObservacion() : "Plan de acción aprobado";
        this.historialService.agregar(reclamo, reclamo.getState(), ComplaintOrdenFlujo.VALIDAR_PLANES_ACCION, observacion, planAccion);
        if (dto.getEstado().equals(ComplaintPlanAccionEstado.REGRESADO)) {
            try {
                this.servicioNotificacion.notificarPlanAccionEstado(reclamo, Collections.singletonList(planAccion),ComplaintPlanAccionEstado.REGRESADO, observacion, UtilidadesSeguridad.nombreUsuarioEnSesion());
            } catch (Exception ex) {
                log.error(String.format("Error al notificar planes de asigancion de RECLAMO DE MATERIA PRIMA: %s", ex));
            }
        }
        return this.reclamoPlanesAccionMapper.fromListToListDto(reclamo.getListActionsPlanProvider());
    }

    @Override
    public List<ComplaintDto> findAll() {
        return null;
    }

    @Transactional
    @Override
    public ComplaintDto create(ComplaintDto obj) {
        Product producto = this.obtenerArticuloPorId(obj.getIdProduct());
        Provider proveedor = null;
        if (obj.getIdProvider() != null)
            proveedor = this.obtenerProveedorPorId(obj.getIdProvider());
        UnidadMedida unidadMedida = this.obtenerUnidadMedida(obj.getUnidadMedidaId());

        Complaint reclamo = new Complaint(producto.getIdProduct(), proveedor != null ? proveedor.getIdProvider() : null, obj.getBatchProvider(), obj.getPalletNumber(), obj.getAffectedProduct(),
                obj.getAffectedAmount(), obj.getTotalAmount(), obj.getPlace(), obj.getDateComplaint(), obj.isApplyReturn(), obj.getPorcentComplaint(), obj.getDetailNCP(), unidadMedida,
                obj.getOtherProvider(), this.consultarUsuario(UtilidadesSeguridad.nombreUsuarioEnSesion()), obj.getOrdenCompra());

        reclamo.setNumber(this.reclamoRepo.secuencialSiguiente());
        log.info("Reclamo guardado {0}", reclamo);
        this.reclamoRepo.save(reclamo);
        return this.reclamoMapper.fromReclamoToDto(reclamo);
    }

    @Override
    public ComplaintDto findById(ComplaintDto id) {
        return null;
    }

    @Transactional
    @Override
    public ComplaintDto update(ComplaintDto obj) {
        Complaint reclamo = this.obtenerporId(obj.getId());
        reclamo.setDetailNCP(obj.getDetailNCP());
        reclamo.setTotalAmount(obj.getTotalAmount());
        reclamo.setAffectedAmount(obj.getAffectedAmount());
        reclamo.setPlace(obj.getPlace());
        reclamo.setBatchProvider(obj.getBatchProvider());
        reclamo.setApplyReturn(obj.isApplyReturn());
        reclamo.setDateComplaint(obj.getDateComplaint());
        reclamo.setOtherProvider(obj.getOtherProvider());
        reclamo.setPalletNumber(obj.getPalletNumber());
        reclamo.setOrdenCompra(obj.getOrdenCompra());
        if (obj.getUnidadMedidaId() != reclamo.getUnit().getId()) {
            UnidadMedida unidadMedida = this.obtenerUnidadMedida(obj.getUnidadMedidaId());
            reclamo.setUnit(unidadMedida);
        }
        reclamo.calcularTotal();
        this.reclamoRepo.save(reclamo);
        return this.reclamoMapper.fromReclamoToDto(reclamo);
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    private Complaint obtenerporId(long id) {
        return reclamoRepo.findById(id).orElseThrow(() -> new PncErrorException("Reclamo no encontrado"));
    }

    private Product obtenerArticuloPorId(Integer id) {
        return productoRepo.findById(id).orElseThrow(() -> new PncErrorException("Producto no encontrado"));
    }

    private Provider obtenerProveedorPorId(Integer id) {
        return providerRepo.findById(id).orElseThrow(() -> new PncErrorException("Proveedor no encontrado"));
    }

    private UserImptek consultarUsuario(String usuario) {
        return usuarioRepo.findOneByNickName(usuario);
    }

    private UnidadMedida obtenerUnidadMedida(long id) {
        return unidadMedidadRepo.findById(id).orElseThrow(() -> new PncErrorException("Unidad de medida no encontrada"));
    }


    private List<Complaint> obtenerDesviacionesPorCriterios(ConsultaReclamoDto consulta) {
        try {
            final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<Complaint> query = criteriaBuilder.createQuery(Complaint.class);
            final Root<Complaint> root = query.from(Complaint.class);
            final List<Predicate> predicadosConsulta = new ArrayList<>();

            if (consulta.getFechaInicio() != null && consulta.getFechaFin() != null) {
                predicadosConsulta.add(criteriaBuilder.between(root.get("creadoFecha"),
                        consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
                        consulta.getFechaFin().withHour(23).withMinute(59).withSecond(59)));
            }

            if (consulta.getFechaInicio() != null && consulta.getFechaFin() == null) {
                predicadosConsulta.add(criteriaBuilder.between(root.get("creadoFecha"),
                        consulta.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
                        consulta.getFechaInicio().withHour(23).withMinute(59).withSecond(59)));
            }

            if (consulta.getProductoId() != null)
                predicadosConsulta.add(criteriaBuilder.equal(root.get("idProduct"), consulta.getProductoId()));

            if (consulta.getProveedorId() != null)
                predicadosConsulta.add(criteriaBuilder.equal(root.get("idProvider"), consulta.getProveedorId()));

            if (consulta.getNumero() != null)
                predicadosConsulta.add(criteriaBuilder.equal(root.get("number"), consulta.getNumero()));

            if(!consulta.getEstados().isEmpty()){
                predicadosConsulta.add(criteriaBuilder.in(root.get("state")).value(consulta.getEstados()));
            }

            query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
                    .orderBy(criteriaBuilder.desc(root.get("number")));

            final TypedQuery<Complaint> statement = this.entityManager.createQuery(query);

            final List<Complaint> desviacionesResultado = statement.getResultList();
            return desviacionesResultado;
        } catch (Exception e) {
            log.error(String.format("Error al consultar Reclamos de MP %s", e.getMessage()));
            return new ArrayList<>();
        }
    }

    private String obtenerAprobador(NombreConfiguracionFlujo nombreConfiguracionFlujo) {
        ConfiguracionGeneralFlujo aprobador = configuracionGeneralFlujoRepo.findByTipoSolicitudAndNombreConfiguracionFlujo(TipoSolicitud.RECLAMO_MP, nombreConfiguracionFlujo)
                .orElseThrow(() -> new PncErrorException("Aprobador no configurado"));
        return aprobador.getValorConfiguracion();
    }
}
