package com.isacore.quality.service.impl.pnc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.pnc.*;
import com.isacore.quality.repository.IUnidadMedidadRepo;
import com.isacore.quality.repository.pnc.IPncDefectoRepo;
import com.isacore.quality.repository.pnc.IProductoNoConformeRepo;
import com.isacore.quality.service.pnc.IPncDocumentoService;
import com.isacore.quality.service.pnc.IProductoNoConformeService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.isacore.util.UtilidadesSeguridad.usuarioEnSesion;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductoNoConformeServiceImpl implements IProductoNoConformeService {

    private final IProductoNoConformeRepo repositorio;
    private final EntityManager entityManager;
    private final IPncDocumentoService documentoService;
    private final IPncDefectoRepo defectoRepositorio;
    private final IUnidadMedidadRepo unidadMedidadRepo;
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Transactional
    @Override
    public ProductoNoConforme registrar(ProductoNoConforme dto) {
        ProductoNoConforme pnc = new ProductoNoConforme(
                repositorio.secuencialSiguiente(),
                usuarioEnSesion(),
                dto.getFechaProduccion(),
                dto.getFechaDeteccion(),
                dto.getCantidadProducida(),
                dto.getCantidadNoConforme(),
                dto.getUnidad(),
                dto.getPorcentajeValidez(),
                dto.getPesoNoConforme(),
                dto.getOrdenProduccion(),
                dto.getLote(),
                dto.getHcc(),
                dto.getObservacionCincoMs(),
                dto.getArea(),
                dto.getProducto(),
                dto.getProcedenciaLinea(),
                dto.getLineaAfecta(),
                dto.getNombreCliente()
        );
        this.repositorio.save(pnc);
        log.info(String.format("PNC registrado %s", pnc));
        return pnc;
    }

    @Transactional
    @Override
    public ProductoNoConforme actualizar(ProductoNoConforme dto) {
        ProductoNoConforme pnc = this.buscarPorId(dto.getId());
        pnc.setArea(dto.getArea());
        pnc.setFechaDeteccion(dto.getFechaDeteccion());
        pnc.setFechaProduccion(dto.getFechaProduccion());
        if (pnc.getEstado().equals(EstadoPnc.EN_PROCESO)) {
            if (pnc.getCantidadNoConforme().compareTo(dto.getCantidadNoConforme()) > 0) {
                throw new PncErrorException("No se puede actualizar cantidad no conforme inferior a la actual, debido al estado EN PROCESO");
            } else {
                pnc.setCantidadNoConforme(dto.getCantidadNoConforme());
                BigDecimal aumento = dto.getCantidadNoConforme().subtract(pnc.getSaldo());
                pnc.setSaldo(pnc.getSaldo().add(aumento));
            }
        }
        pnc.setCantidadProducida(dto.getCantidadProducida());
        pnc.setUnidad(dto.getUnidad());
        pnc.setNombreCliente(dto.getNombreCliente());
        pnc.setLineaAfecta(dto.getLineaAfecta());
        pnc.setProcedenciaLinea(dto.getProcedenciaLinea());
        pnc.setProducto(dto.getProducto());
        pnc.setHcc(dto.getHcc());
        pnc.setLote(dto.getLote());
        pnc.setOrdenProduccion(dto.getOrdenProduccion());
        pnc.setObservacionCincoMs(dto.getObservacionCincoMs());
        pnc.setVentaTotalMes(dto.getVentaTotalMes());
        pnc.setProduccionTotalMes(dto.getProduccionTotalMes());
        log.info(String.format("PNC actualizado %s", pnc));
        return pnc;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductoNoConforme listarPorId(long id) {
        ProductoNoConforme pnc = this.buscarPorId(id);
        return pnc;
    }

    @Override
    public Page<PncDTO> listar(Pageable pageable, ConsultaPncDTO dto) {
        try {

            List<PncDTO> respuesta = new ArrayList<>();
            respuesta.addAll(obtenerPncPorCriterios(dto));
            final int sizeTotal = respuesta.size();

            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > respuesta.size() ? respuesta.size()
                    : (start + pageable.getPageSize());

            respuesta = respuesta.subList(start, end);

            final Page<PncDTO> pageResut = new PageImpl<>(respuesta, pageable, sizeTotal);

            return pageResut;

        } catch (Exception e) {
            final Page<PncDTO> pageResult = new PageImpl<PncDTO>(new ArrayList<PncDTO>(), pageable, 0);
            return pageResult;
        }
    }

    private List<PncDTO> obtenerPncPorCriterios(ConsultaPncDTO consulta) {
        try {
            final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
            final CriteriaQuery<ProductoNoConforme> query = criteriaBuilder.createQuery(ProductoNoConforme.class);

            final Root<ProductoNoConforme> root = query.from(ProductoNoConforme.class);
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
                predicadosConsulta.add(criteriaBuilder.equal(root.get("producto").get("idProduct"), consulta.getProductoId()));

            if (null != consulta.getEstados() && !consulta.getEstados().isEmpty())
                predicadosConsulta.add(criteriaBuilder.in(root.get("estado")).value(consulta.getEstados()));

            if (consulta.getNumero() != null)
                predicadosConsulta.add(criteriaBuilder.equal(root.get("numero"), consulta.getNumero()));


            query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
                    .orderBy(criteriaBuilder.desc(root.get("numero")));

            final TypedQuery<ProductoNoConforme> statement = this.entityManager.createQuery(query);

            final List<ProductoNoConforme> cotizacionesResult = statement.getResultList();

            return cotizacionesResult.stream().map(c -> {
                return new PncDTO(
                        c.getId(),
                        c.getNumero(),
                        c.getUsuario(),
                        c.getFechaProduccion(),
                        c.getFechaDeteccion(),
                        c.getEstado(),
                        c.getCantidadProducida(),
                        c.getCantidadNoConforme(),
                        c.getSaldo(),
                        c.getUnidad(),
                        c.getPorcentajeValidez(),
                        c.getPesoNoConforme(),
                        c.getProduccionTotalMes(),
                        c.getVentaTotalMes(),
                        c.getOrdenProduccion(),
                        c.getLote(),
                        c.getHcc(),
                        "",
                        c.getArea().getNameArea(),
                        c.getProducto().getNameProduct()
                );
            }).collect(Collectors.toList());

        } catch (Exception e) {
            log.error(String.format("Error al consultar ProductoNoConforme %s", e.getMessage()));
            return new ArrayList<>();
        }
    }

    @Transactional
    @Override
    public boolean anular(ProductoNoConforme dto) {
        ProductoNoConforme pnc = this.buscarPorId(dto.getId());
        if (!pnc.getEstado().equals(EstadoPnc.CREADO))
            throw new PncErrorException("Pnc no puede ser anulado por estado actual");
        pnc.cambiarAnulado();
        log.info(String.format("PNC %s ANULADO", pnc.getNumero()));
        return false;
    }

    @Transactional
    @Override
    public List<PncDefecto> agregarDefecto(String jsonCriteria, byte[] file, String nombreArchivo, String tipo) {
        try {
            PncDefecto dto = JSON_MAPPER.readValue(jsonCriteria, PncDefecto.class);
            if (dto != null) {
                ProductoNoConforme pnc = this.buscarPorId(dto.getProductoNoConformeId());

                UnidadMedida unidad = buscarUnidadMedidaPorId(dto.getUnidad().getId());
                dto.setUnidad(unidad);
                List<Long> defectosIds = pnc.getDefectos().stream().map(PncDefecto::getId).collect(Collectors.toList());
                pnc.agregarDefecto(dto);
                //pnc.getDefectos().stream().filter(x -> !defectosIds.contains(x.getId())).findFirst().orElse(null);

                this.repositorio.save(pnc);
                pnc.getDefectos().forEach(x -> {
                    if (!defectosIds.contains(x.getId())) {
                        x.setNuevo(true);
                        dto.setId(x.getId());
                    }
                });
                if (file.length > 0) {
                    PncDocumento documento = this.documentoService.registrar(pnc, file, nombreArchivo, tipo, null);
                    pnc.agregarIdDocumentoADefecto(dto.getId(), documento.getId());
                }
                log.info(String.format("PNC %s -> Defecto agregado %s", pnc.getId(), dto));
                return pnc.getDefectos();
            }
        } catch (JsonProcessingException e) {
            log.error(String.format("Error al agregar defecto %s", e.getMessage()));
            throw new PncErrorException("Error al agregar defecto");
        }
        return null;
    }

    @Transactional
    @Override
    public List<PncDefecto> eliminarDefecto(long pncId, long defectoId) {
        ProductoNoConforme pnc = this.buscarPorId(pncId);
        PncDefecto defecto = pnc.getDefectos().stream().filter(x -> x.getId().equals(defectoId)).findFirst().orElse(null);
        if (defecto != null) {
            pnc.eliminarDefecto(defectoId);
            this.documentoService.eliminarDocumento(defecto.getIdImagen());
            log.info(String.format("PNC %s -> Defecto eliminado %s", pnc.getNumero(), defecto));
        }
        return pnc.getDefectos();
    }

    @Transactional
    @Override
    public List<PncDefecto> actualizarDefecto(String jsonCriteria, byte[] file, String nombreArchivo, String tipo) {
        try {
            PncDefecto dto = JSON_MAPPER.readValue(jsonCriteria, PncDefecto.class);
            ProductoNoConforme pnc = this.buscarPorId(dto.getProductoNoConformeId());
            PncDefecto defecto = pnc.getDefectos().stream().filter(x -> x.getId().equals(dto.getId())).findFirst().orElse(null);
            if (defecto == null)
                throw new PncErrorException(String.format("Defecto %s no encontrado", dto.getId()));
            defecto.setDefecto(dto.getDefecto());
            defecto.setUbicacion(dto.getUbicacion());
            UnidadMedida unidad = buscarUnidadMedidaPorId(dto.getUnidad().getId());
            defecto.setUnidad(unidad);
            defecto.setValidez(dto.getValidez());
            defecto.setCantidad(dto.getCantidad());

            if (file.length > 0) {
                if (defecto.getIdImagen() > 0) {
                    this.documentoService.eliminarDocumento(defecto.getIdImagen());
                }
                PncDocumento documento = this.documentoService.registrar(pnc, file, nombreArchivo, tipo, null);
                defecto.setIdImagen(documento.getId());
            }

            log.info(String.format("PNC %s -> Defecto actualizado %s", pnc.getNumero(), defecto));
            return pnc.getDefectos();
        } catch (JsonProcessingException e) {
            log.error(String.format("Error al actualizar defecto %s", e.getMessage()));
            throw new PncErrorException("Error al actualizar el defecto");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String consultarSaldoPorId(long id) {
        ProductoNoConforme pnc = this.buscarPorId(id);
        return String.format("%s %s", pnc.getSaldo(), pnc.getUnidad().getAbreviatura());
    }

    private ProductoNoConforme buscarPorId(long id) {
        Optional<ProductoNoConforme> pnc = this.repositorio.findById(id);
        if (!pnc.isPresent())
            throw new PncErrorException("Pnc no encontrado");
        return pnc.get();
    }

    private UnidadMedida buscarUnidadMedidaPorId(long id) {
        Optional<UnidadMedida> pnc = this.unidadMedidadRepo.findById(id);
        if (!pnc.isPresent())
            throw new PncErrorException("Unidad de medida no encontrada");
        return pnc.get();
    }
}
