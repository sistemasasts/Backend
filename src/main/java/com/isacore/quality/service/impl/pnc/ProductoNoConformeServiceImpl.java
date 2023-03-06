package com.isacore.quality.service.impl.pnc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.InformationAditionalFileDTO;
import com.isacore.quality.exception.ApprobationCriteriaErrorException;
import com.isacore.quality.exception.PncErrorException;
import com.isacore.quality.model.pnc.*;
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
                dto.getProducto()
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
        pnc.setCantidadNoConforme(dto.getCantidadNoConforme());
        pnc.setCantidadProducida(dto.getCantidadProducida());
        pnc.setHcc(dto.getHcc());
        pnc.setLote(dto.getLote());
        pnc.setOrdenProduccion(dto.getOrdenProduccion());
        log.info(String.format("PNC actualizado %s", pnc));
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

            query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()]))
                    .orderBy(criteriaBuilder.desc(root.get("fechaCreacion")));

            final TypedQuery<ProductoNoConforme> statement = this.entityManager.createQuery(query);

            final List<ProductoNoConforme> cotizacionesResult = statement.getResultList();

            return cotizacionesResult.stream().map(c -> {
                return new PncDTO(
                        c.getId(),
                        c.getNumero(),
                        c.getUsuario(),
                        c.getFechaProduccion(),
                        c.getFechaDeteccion(),
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

    @Override
    public boolean anular(ProductoNoConforme dto) {
        return false;
    }

    @Transactional
    @Override
    public List<PncDefecto> agregarDefecto(String jsonCriteria, byte[] file, String nombreArchivo, String tipo) {
        try {
            PncDefecto dto = JSON_MAPPER.readValue(jsonCriteria, PncDefecto.class);
            if (dto != null) {
                ProductoNoConforme pnc = this.buscarPorId(dto.getProductoNoConformeId());
                PncDocumento documento = this.documentoService.registrar(pnc, file, nombreArchivo, tipo, null);
                dto.setIdImagen(documento.getId());
                pnc.agregarDefecto(dto);
                log.info(String.format("PNC %s -> Defecto agregado %s", pnc.getId(), dto));
                return pnc.getDefectos();
            }
        } catch (JsonProcessingException e) {
            log.error(String.format("Error al agregar defecto %s", e.getMessage()));
            throw new PncErrorException("Error al agregar defecto");
        }
        return null;
    }

    @Override
    public List<PncDefecto> eliminarDefecto(PncDefecto dto) {
        return null;
    }

    @Override
    public List<PncDefecto> actualizarDefecto(PncDefecto dto) {
        return null;
    }

    private ProductoNoConforme buscarPorId(long id) {
        Optional<ProductoNoConforme> pnc = this.repositorio.findById(id);
        if (!pnc.isPresent())
            throw new PncErrorException("Pnc no encontrado");
        return pnc.get();
    }
}
