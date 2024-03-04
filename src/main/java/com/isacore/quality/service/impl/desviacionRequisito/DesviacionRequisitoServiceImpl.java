package com.isacore.quality.service.impl.desviacionRequisito;

import com.isacore.exception.reporte.JasperReportsException;
import com.isacore.exception.reporte.ReporteExeption;
import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.mapper.pnc.DesviacionRequisitoMapper;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.configuracionFlujo.NombreConfiguracionFlujo;
import com.isacore.quality.model.desviacionRequisito.*;
import com.isacore.quality.model.pnc.Defecto;
import com.isacore.quality.model.pnc.PncOrdenFlujo;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.model.pnc.PncSalidaMaterialDto;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoHistorialRepo;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoRepo;
import com.isacore.quality.repository.desviacionRequisito.ILoteRepo;
import com.isacore.quality.repository.pnc.IDefectoRepo;
import com.isacore.quality.service.desviacionRequisito.IDesviacionRequisitoHistorialService;
import com.isacore.quality.service.desviacionRequisito.IDesviacionRequisitoService;
import com.isacore.servicio.reporte.IGeneradorJasperReports;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DesviacionRequisitoServiceImpl implements IDesviacionRequisitoService {
    private final IDesviacionRequisitoRepo desviacionRequisitoRepo;
    private final IGeneradorJasperReports reporteServicio;
    private final ILoteRepo loteRepo;
    private final EntityManager entityManager;
    private final IDefectoRepo defectoRepositorio;
    private final IConfiguracionGeneralFlujoRepo configuracionGeneralFlujoRepo;
    private final IDesviacionRequisitoHistorialService historialService;
    private final DesviacionRequisitoMapper mapper;

    @Override
    public List<DesviacionRequisito> findAll() {
        return this.desviacionRequisitoRepo.findAll();
    }

    @Override
    public DesviacionRequisito create(DesviacionRequisito desviacionRequisito) {
        if( desviacionRequisito.getProduct() == null || desviacionRequisito.getProduct().getIdProduct() == null)
            throw new ConfiguracionErrorException("Material no encontrado, seleccione uno");

        DesviacionRequisito nuevaDesviacionRequisito = new DesviacionRequisito(
                this.desviacionRequisitoRepo.generarSecuencial(),
                desviacionRequisito.getProduct(),
                desviacionRequisito.getSeguimiento(),
                desviacionRequisito.getAfectacion(),
                desviacionRequisito.getMotivo(),
                desviacionRequisito.getDescripcion(),
                desviacionRequisito.getControl(),
                desviacionRequisito.getAlcance(),
                desviacionRequisito.getResponsable(),
                desviacionRequisito.isReplanificacion(),
                desviacionRequisito.getCausa()
        );

        this.desviacionRequisitoRepo.save(nuevaDesviacionRequisito);
        log.info(String.format("Desviacion deRequisito registrado %s", nuevaDesviacionRequisito));

        return nuevaDesviacionRequisito;
    }

    @Override
    public DesviacionRequisito findById(DesviacionRequisito id) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public DesviacionRequisito listarDesviacionRequisitosPorId(Long desviacionId) {
        Optional<DesviacionRequisito> desviacionRequisito = this.desviacionRequisitoRepo.findById(desviacionId);

        if(!desviacionRequisito.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        return desviacionRequisito.get();
    }

    @Transactional
    @Override
    public DesviacionRequisito update(DesviacionRequisito obj) {
        Optional<DesviacionRequisito> desviacionRequisito = this.desviacionRequisitoRepo.findById(obj.getId());
        if (!desviacionRequisito.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        desviacionRequisito.get().setProduct(obj.getProduct());
        desviacionRequisito.get().setSeguimiento(obj.getSeguimiento());
        desviacionRequisito.get().setAfectacion(obj.getAfectacion());
        desviacionRequisito.get().setMotivo(obj.getMotivo());
        desviacionRequisito.get().setDescripcion(obj.getDescripcion());
        desviacionRequisito.get().setControl(obj.getControl());
        desviacionRequisito.get().setAlcance(obj.getAlcance());
        desviacionRequisito.get().setResponsable(obj.getResponsable());
        desviacionRequisito.get().setReplanificacion(obj.isReplanificacion());
        desviacionRequisito.get().setCantidadAfectada(obj.getCantidadAfectada());
        desviacionRequisito.get().setCantidadRecuperada(obj.getCantidadRecuperada());
        desviacionRequisito.get().setDesperdicioGenerado(obj.getDesperdicioGenerado());
        desviacionRequisito.get().setProductoAfectado(obj.getProductoAfectado());
        desviacionRequisito.get().setProductoReplanificado(obj.getProductoReplanificado());
        desviacionRequisito.get().setUnidadAfectada(obj.getUnidadAfectada());
        desviacionRequisito.get().setUnidadDesperdicio(obj.getUnidadDesperdicio());
        desviacionRequisito.get().setUnidadRecuperada(obj.getUnidadRecuperada());
        desviacionRequisito.get().setCausa(obj.getCausa());

        log.info(String.format("Desviacion de requisito actualizado %s", desviacionRequisito));

        return desviacionRequisito.get();
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<DesviacionRequisito> listarDesviacionRequistoActivos() {
        return null;
    }

    @Override
    public byte[] generarReporte(Long id) {
        Optional<DesviacionRequisito> desviacion = desviacionRequisitoRepo.findById(id);

        if (!desviacion.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        try {
            return reporteServicio.generarReporte("DesviacionRequisito", Collections.singleton(this.crearReporteDTO(desviacion.get())), new HashMap<>());
        } catch (JasperReportsException e) {
            log.error(String.format("Error Desviacion de Requisito Reporete %s", e));

            throw  new ReporteExeption("Desviación de requisitos");
        }
    }

    @Override
    public Page<DesviacionRequisito> listar(Pageable pageable, ConsultaDesviacionRequisitoDTO dto) {
        try {

            List<DesviacionRequisito> respuesta = new ArrayList<>();
            respuesta.addAll(obtenerDesviacionesPorCriterios(dto));
            final int sizeTotal = respuesta.size();

            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > respuesta.size() ? respuesta.size()
                    : (start + pageable.getPageSize());

            respuesta = respuesta.subList(start, end);
            List<DesviacionRequisito> listaMapeada = respuesta.stream().map(c -> {
                DesviacionRequisito desviacion = new DesviacionRequisito(
                        c.getSecuencial(),
                        c.getProduct(),
                        c.getSeguimiento(),
                        c.getAfectacion(),
                        c.getMotivo(),
                        c.getDescripcion(),
                        c.getControl(),
                        c.getAlcance(),
                        c.getResponsable(),
                        c.isReplanificacion(),
                        c.getCausa()
                );
                desviacion.setId(c.getId());
                desviacion.setAfectacionText(c.getAfectacion().getDescripcion());
                desviacion.setProductTypeText(c.getProduct().getTypeProduct().getDescripcion());
                desviacion.setFechaCreacion(c.getFechaCreacion());
                desviacion.setEstado(c.getEstado());

                return  desviacion;
            }).collect(Collectors.toList());

            final Page<DesviacionRequisito> pageResut = new PageImpl<>(listaMapeada, pageable, sizeTotal);

            return pageResut;

        } catch (Exception e) {
            final Page<DesviacionRequisito> pageResult = new PageImpl<DesviacionRequisito>(new ArrayList<DesviacionRequisito>(), pageable, 0);
            return pageResult;
        }
    }

    @Transactional
    @Override
    public List<DesviacionRequisitoDefecto> agregarDefecto(long id, long defectoId) {
        DesviacionRequisito desviacion = this.obtenerPorId(id);
        Defecto defecto = defectoRepositorio.findById(defectoId).orElseThrow(() -> new ConfiguracionErrorException("Defecto no encontrado"));
        desviacion.agregarDefecto(new DesviacionRequisitoDefecto(defecto));
        desviacionRequisitoRepo.save(desviacion);
        log.info("Desviacion {} Defecto agregado {}", desviacion.getSecuencial() ,defecto);
        return desviacion.getDefectos();
    }

    @Transactional
    @Override
    public List<DesviacionRequisitoDefecto> eliminarDefecto(long id, long defectoId) {
        DesviacionRequisito desviacion = this.obtenerPorId(id);
        desviacion.eliminarDefecto(defectoId);
        log.info("Desviacion {} Defecto eliminado {}", desviacion.getSecuencial() ,defectoId);
        return desviacion.getDefectos();
    }

    @Transactional
    @Override
    public void enviarAprobacion(DesviacionRequisitoDto dto) {
        DesviacionRequisito salidaMaterial = this.obtenerPorId(dto.getId());
        Optional<ConfiguracionGeneralFlujo> configuracion = this.configuracionGeneralFlujoRepo
                .findByTipoSolicitudAndNombreConfiguracionFlujo(TipoSolicitud.DESVIACION_REQUISITO, NombreConfiguracionFlujo.APROBADOR_DESVIACION_REQUISITO);
        if (!configuracion.isPresent())
            throw new PncErrorException(String.format("Configuración %s no encontrada", NombreConfiguracionFlujo.APROBADOR_DESVIACION_REQUISITO.getDescripcion()));

        String observacion = UtilidadesCadena.esNuloOBlanco(dto.getObservacion()) ? "DESVIACIÓN REQUISITOS ENVIADA" :
                dto.getObservacion();
        this.historialService.agregar(salidaMaterial, DesviacionRequisitoOrdenFlujo.INGRESO, observacion);
        salidaMaterial.marcarComoEnviada(configuracion.get().getValorConfiguracion());

        log.info(String.format("DESVIACION REQUISITO %s ->  enviada a aprobar", salidaMaterial.getSecuencial()));
        try {
           // this.notificacionPnc.notificarIngresoSalidaMaterial(salidaMaterial, observacion, this.planAccionService.listarPorSalidaMaterialId(salidaMaterial.getId()));
        } catch (Exception e) {
            log.error(String.format("Error al notificar INGRESO DESVIACION REQUISITO: %s", e));
        }
    }

    @Transactional
    @Override
    public void procesar(DesviacionRequisitoDto dto) {
        DesviacionRequisito desviacion = this.obtenerPorId(dto.getId());
        if(dto.getAccion() == null)
            throw new PncErrorException("Acción no procesada");

        String observacion = "";
        switch (dto.getAccion()){
            case APROBADO:
                observacion= "DESVIACIÓN REQUISITOS APROBADA";
                break;
            case RECHAZADO:
                observacion= "DESVIACIÓN REQUISITOS RECHAZADA";
                break;
            default:
                break;
        }

        String observacionFinal= UtilidadesCadena.esNuloOBlanco(dto.getObservacion()) ? observacion :
                dto.getObservacion();

        this.historialService.agregar(desviacion, DesviacionRequisitoOrdenFlujo.APROBACION_GERENCIA_CALIDAD, observacionFinal);
        desviacion.setEstado(dto.getAccion());
        desviacion.setFechaAprobacion(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    @Override
    public List<DesviacionRequisitoDto> listarPorEstado(EstadoDesviacion estado) {
        String usuarioSesion = UtilidadesSeguridad.nombreUsuarioEnSesion();
        List<DesviacionRequisito> salidaMateriales = this.desviacionRequisitoRepo.findByEstadoIn(Arrays.asList(estado))
                .stream()
                .filter(x -> x.getUsuarioAprobador().equals(usuarioSesion))
                .collect(Collectors.toList());
        return this.mapper.fromListDesviacionRequisitoToDto(salidaMateriales);
    }

    private DesviacionRequisitoReporteDTO crearReporteDTO(DesviacionRequisito desviacionRequisito) {
        List<Lote> lotes = loteRepo.findByDesviacionRequisito(desviacionRequisito);
        List<Lote> lotesReporte = new ArrayList<>();

        lotes.forEach(x -> {
            Lote lote = new Lote();
            lote.setId(x.getId());
            lote.setDesviacionRequisito(x.getDesviacionRequisito());
            lote.setUnidad(x.getUnidad());
            lote.setCantidad(x.getCantidad());
            lote.setFecha(x.getFecha());
            lote.setCantidad(x.getCantidad());
            lote.setUnidadText(x.getUnidad().getNombre());
            lote.setLote(x.getLote());

            lotesReporte.add(lote);
        });
        return new DesviacionRequisitoReporteDTO(desviacionRequisito, lotesReporte);
    }

    private List<DesviacionRequisito> obtenerDesviacionesPorCriterios(ConsultaDesviacionRequisitoDTO consulta) {
        try {
            final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<DesviacionRequisito> query = criteriaBuilder.createQuery(DesviacionRequisito.class);
            final Root<DesviacionRequisito> root = query.from(DesviacionRequisito.class);
            final List<Predicate> predicadosConsulta = new ArrayList<>();


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

            if (consulta.getProductoId() != null)
                predicadosConsulta.add(criteriaBuilder.equal(root.get("product").get("idProduct"), consulta.getProductoId()));

            if (consulta.getSecuencial() != null)
                predicadosConsulta.add(criteriaBuilder.equal(root.get("secuencial"), consulta.getSecuencial()));

            if (consulta.getAfectacion() != null)
                predicadosConsulta.add(criteriaBuilder.ge(root.get("afectacion"), consulta.getSecuencial()));

            query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
                    .orderBy(criteriaBuilder.desc(root.get("secuencial")));

            final TypedQuery<DesviacionRequisito> statement = this.entityManager.createQuery(query);

            final List<DesviacionRequisito> desviacionesResultado = statement.getResultList();
            return desviacionesResultado;
        } catch (Exception e) {
            log.error(String.format("Error al consultar Desviacion de Requisitos %s", e.getMessage()));
            return new ArrayList<>();
        }
    }

    private DesviacionRequisito obtenerPorId(long id){
        return this.desviacionRequisitoRepo.findById(id)
                .orElseThrow(() -> new ConfiguracionErrorException("Desviacion de requisito no encontrada"));
    }
}
