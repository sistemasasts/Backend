package com.isacore.quality.service.impl.desviacionRequisito;

import com.isacore.exception.reporte.JasperReportsException;
import com.isacore.exception.reporte.ReporteExeption;
import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.model.desviacionRequisito.ConsultaDesviacionRequisitoDTO;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoReporteDTO;
import com.isacore.quality.model.desviacionRequisito.Lote;
import com.isacore.quality.model.pnc.ConsultaPncDTO;
import com.isacore.quality.model.pnc.PncDTO;
import com.isacore.quality.model.pnc.ProductoNoConforme;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoRepo;
import com.isacore.quality.repository.desviacionRequisito.ILoteRepo;
import com.isacore.quality.service.desviacionRequisito.IDesviacionRequisitoService;
import com.isacore.servicio.reporte.IGeneradorJasperReports;
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

    @Override
    public List<DesviacionRequisito> findAll() {
        return this.desviacionRequisitoRepo.findAll();
    }

    @Override
    public DesviacionRequisito create(DesviacionRequisito desviacionRequisito) {
        DesviacionRequisito nuevaDesviacionRequisito = new DesviacionRequisito(
                this.desviacionRequisitoRepo.generarSecuencial(),
                desviacionRequisito.getProduct(),
                desviacionRequisito.getSeguimiento(),
                desviacionRequisito.getAfectacion(),
                desviacionRequisito.getMotivo(),
                desviacionRequisito.getDescripcion(),
                desviacionRequisito.getControl(),
                desviacionRequisito.getAlcance(),
                desviacionRequisito.getResponsable()
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
                        c.getResponsable()
                );
                desviacion.setId(c.getId());
                desviacion.setAfectacionText(c.getAfectacion().getDescripcion());
                desviacion.setProductTypeText(c.getProduct().getTypeProduct().getDescripcion());
                desviacion.setFechaCreacion(c.getFechaCreacion());

                return  desviacion;
            }).collect(Collectors.toList());

            final Page<DesviacionRequisito> pageResut = new PageImpl<>(listaMapeada, pageable, sizeTotal);

            return pageResut;

        } catch (Exception e) {
            final Page<DesviacionRequisito> pageResult = new PageImpl<DesviacionRequisito>(new ArrayList<DesviacionRequisito>(), pageable, 0);
            return pageResult;
        }
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
}
