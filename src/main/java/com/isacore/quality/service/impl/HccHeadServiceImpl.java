package com.isacore.quality.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.EmailDto;
import com.isacore.quality.dto.ReportDto;
import com.isacore.quality.exception.*;
import com.isacore.quality.model.*;
import com.isacore.quality.model.se.SolicitudDocumento;
import com.isacore.quality.model.se.SolicitudDocumentoDTO;
import com.isacore.quality.model.se.SolicitudEnsayo;
import com.isacore.quality.report.GenerateReportQuality;
import com.isacore.quality.repository.IHccHeadRepo;
import com.isacore.quality.service.IHccHeadService;
import com.isacore.quality.service.IReportHeadTService;
import com.isacore.quality.service.se.ConfiguracionSolicitud;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.UtilidadesCadena;
import com.isacore.util.UtilidadesSeguridad;
import com.isacore.util.WebResponseMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.isacore.quality.dto.HccFilterDto;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class HccHeadServiceImpl implements IHccHeadService {

    private static final Log LOG = LogFactory.getLog(HccHeadServiceImpl.class);
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Autowired
    private IHccHeadRepo repo;
    @Autowired
    private UsuarioRepositorio repoUsuario;
    @Autowired
    private ConfiguracionSolicitud configuracion;
    @Autowired
    private IReportHeadTService serviceRH;
    @Autowired
    private EntityManager entityManager;
    @Value("${reporteRutaBase}")
    private String reporteRutaBase;

    @Override
    public List<HccHead> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HccHead create(HccHead hcc) {
        return this.repo.save(hcc);
    }

    @Override
    public HccHead findById(HccHead hh) {
        Optional<HccHead> hccH = this.repo.findById(hh.getId());
        return hccH.get();
    }

    @Override
    public HccHead update(HccHead hcc) {
        return this.repo.save(hcc);
    }

    @Override
    public boolean delete(String id) {
        return true;

    }

    @Override
    public List<HccHead> findOnlyHccHead(String tp) {

        List<Object[]> list = this.repo.findOnlyHccHead(tp);

        if (list.isEmpty() || list == null)
            return null;
        else {
            List<HccHead> listhcc = new ArrayList<>();

            list.forEach((Object[] x) -> {
                HccHead hh = new HccHead();
                Product p = new Product();
                hh.setId(((BigInteger) x[0]).longValue());
                hh.setHcchBatch((String) x[1]);
                p.setNameProduct((String) x[2]);
                p.setTypeProduct(ProductType.valueOf((String) x[3]));
                hh.setProduct(p);
                Instant instant = Instant.ofEpochMilli(((Date) x[4]).getTime());
                hh.setDateCreate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
                hh.setAnalysis((String) x[5]);
                hh.setSapCode((String) x[6]);
                hh.setPeriodicity((String) x[7]);
                listhcc.add(hh);
            });
            return listhcc;
        }
    }

    @Override
    public List<HccHead> findOnlyHccHead() {
        List<Object[]> list = this.repo.findOnlyHccHead();

        if (list.isEmpty() || list == null)
            return null;
        else {
            List<HccHead> listhcc = new ArrayList<>();

            list.forEach((Object[] x) -> {
                HccHead hh = new HccHead();
                Product p = new Product();
                hh.setId(((BigInteger) x[0]).longValue());
                hh.setHcchBatch((String) x[1]);
                p.setNameProduct((String) x[2]);
                p.setTypeProduct(ProductType.valueOf((String) x[3]));
                hh.setProduct(p);
                Instant instant = Instant.ofEpochMilli(((Date) x[4]).getTime());
                hh.setDateCreate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate());
                hh.setAnalysis((String) x[5]);
                hh.setSapCode((String) x[6]);
                hh.setPeriodicity((String) x[7]);
                listhcc.add(hh);
            });
            return listhcc;
        }
    }

    @Transactional
    @Override
    public HccdResultadoDto registrarConImagen(String json, byte[] file, String nombreArchivo, String tipo) {
        try {
            HccdResultadoDto resultado = new HccdResultadoDto();
            HccHead dto = JSON_MAPPER.readValue(json, HccHead.class);
            if (dto != null) {
                Usuario ui = this.obtenerUsuarioSesion();
                dto.setUserName(ui.getNombre());
                dto.setJob(ui.getTrabajo());
                dto.setWorkArea(ui.getArea().getNameArea());
                LOG.info("> objeto a guardar: " + dto.toString());
                dto.setDateCreate(LocalDate.now());
                if (file.length > 0) {
                    try {
                        final String path = crearPathArchivo(dto, nombreArchivo);
                        PassFileToRepository.saveLocalFile(path, file);
                        dto.setRutaImagenMuestra(path);
                        LOG.info(String.format("HCC -> %s :Archivo guardado %s", dto.getId(), path));
                    } catch (IOException e) {
                        LOG.error(String.format("Error al subir Documento %s", e.getMessage()));
                        throw new HCCErrorException("Error al subir el archivo");
                    }
                }
                this.repo.save(dto);
                this.generarReporte(dto, resultado);
                return resultado;
            }
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApprobationCriteriaErrorException();
        }
    }

    private Usuario obtenerUsuarioSesion() {
        String nombreUsuario = UtilidadesSeguridad.nombreUsuarioEnSesion();
        Optional<Usuario> usuario = this.repoUsuario.findByNombreUsuario(nombreUsuario);
        if (!usuario.isPresent())
            throw new UsuarioErrorException("Usuario en sesión no encontrado");
        return usuario.get();
    }

    private String crearPathArchivo(HccHead hccHead, String nombreArchivo) {
        String path = crearRutaAlmacenamiento(hccHead.getId()).concat(File.separator).concat(nombreArchivo);
        if (PassFileToRepository.fileExists(path))
            path = crearRutaAlmacenamiento(hccHead.getId()).concat(File.separator).concat(PassFileToRepository.generateDateAsId()).concat("_").concat(nombreArchivo);
        LOG.info(String.format("Ruta creada %s para guardar archivo %s", path, nombreArchivo));
        return path;
    }

    private String crearRutaAlmacenamiento(long productoId) {
        try {
            String carpeta = configuracion.getRutaBase().concat(File.separator).concat("HCCS")
                    .concat(File.separator).concat(String.valueOf(productoId));
            Path path = Paths.get(carpeta);
            if (!Files.exists(path))
                Files.createDirectories(path);
            return carpeta;
        } catch (IOException e) {
            LOG.error(String.format("Error al subir Imagen %s", e.getMessage()));
            throw new SolicitudEnsayoErrorException("Error al crear el directorio");
        }
    }

    private void generarReporte(HccHead hh, HccdResultadoDto resultado) {
        ReportDto rpt = new ReportDto();
        rpt.setHccHead(hh);
        ReportHeadT rHTemp = new ReportHeadT();
        rHTemp.setType(hh.getReportHeadT());
        rpt.setRh(this.serviceRH.findHeadByTypeReport(rHTemp));

        List<ProductType> tipos = Arrays.asList(ProductType.PRODUCTO_TERMINADO, ProductType.PRODUCTO_MAQUILA, ProductType.PRODUCTO_EN_PROCESO);
        if (tipos.contains(hh.getProduct().getTypeProduct())) {
            String statusReport = GenerateReportQuality.runReportJasperHcc(rpt, reporteRutaBase);
            if (statusReport.equals(GenerateReportQuality.REPORT_SUCCESS)) {
                LOG.info(">> Reporte generado correctamente");
                resultado.setMensaje("El reporte de la HCC " + hh.getSapCode()
                        + "ha sido creado satisfactoriamente");
            } else {
                LOG.error(">> El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
                throw new HCCErrorException("El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
            }
        } else {
            String pathFile = GenerateReportQuality.runReportJasperHcc(rpt, reporteRutaBase);
            resultado.setRutaArchivo(pathFile);
            if (UtilidadesCadena.noEsNuloNiBlanco(resultado.getRutaArchivo())) {
                LOG.info(">> Reporte generado correctamente");
                resultado.setMensaje("El reporte de la HCC " + hh.getSapCode() + "ha sido creado satisfactoriamente");
            } else {
                LOG.error(">> El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
                throw new HCCErrorException("El reporte de la HCC" + hh.getSapCode() + "no se a podido crear");
            }
        }
    }

    public Page<HccHead> listarPorCriterios(Pageable pageable, HccFilterDto filters) {
        try {
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            
            // 1. Consulta de conteo (totalCount)
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<HccHead> countRoot = countQuery.from(HccHead.class);
            List<Predicate> countPredicates = new ArrayList<>();
            buildPredicates(cb, countRoot, filters, countPredicates);
            countQuery.select(cb.count(countRoot));
            countQuery.where(countPredicates.toArray(new Predicate[0]));
            Long totalCount = this.entityManager.createQuery(countQuery).getSingleResult();
            
            if (totalCount == 0 && countPredicates.size() > 0 ) {
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
            
            // 2. Consulta de resultados paginados
            CriteriaQuery<HccHead> query = cb.createQuery(HccHead.class);
            Root<HccHead> root = query.from(HccHead.class);
            List<Predicate> predicates = new ArrayList<>();
            buildPredicates(cb, root, filters, predicates);
            
            // Usar proyección de constructor para evitar cargar la colección detail (fetch eager)
            // y seleccionar únicamente el nombre y tipo del producto de forma plana (evitando construct anidado incompatible con Hibernate 5)
            query.select(cb.construct(
                HccHead.class,
                root.get("id"),
                root.get("sapCode"),
                root.get("product").get("nameProduct"),
                root.get("product").get("typeProduct"),
                root.get("dateCreate"),
                root.get("periodicity"),
                root.get("hcchBatch"),
                root.get("analysis")
            ));
            
            query.where(predicates.toArray(new Predicate[0]));
            
            // Aplicar ordenación por fecha de creación desc, y luego por id desc
            query.orderBy(cb.desc(root.get("dateCreate")), cb.desc(root.get("id")));
            
            List<HccHead> resultList = this.entityManager.createQuery(query)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
            
            return new PageImpl<>(resultList, pageable, totalCount);
        } catch (Exception e) {
            LOG.error("Error al momento de consultar HCC paginado", e);
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
    }

    private void buildPredicates(CriteriaBuilder cb, Root<HccHead> root, HccFilterDto filters, List<Predicate> predicates) {
        if (filters.getLote() != null && !filters.getLote().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("hcchBatch")), "%" + filters.getLote().toLowerCase().trim() + "%"));
        }
        if (filters.getProducto() != null && !filters.getProducto().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("product").get("nameProduct")), "%" + filters.getProducto().toLowerCase().trim() + "%"));
        }
        if (filters.getHcc() != null && !filters.getHcc().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("sapCode")), "%" + filters.getHcc().toLowerCase().trim() + "%"));
        }
        if (filters.getFechaInicio() != null && !filters.getFechaInicio().trim().isEmpty()) {
            LocalDate start = LocalDate.parse(filters.getFechaInicio().trim());
            predicates.add(cb.greaterThanOrEqualTo(root.get("dateCreate"), start));
        }
        if (filters.getFechaFin() != null && !filters.getFechaFin().trim().isEmpty()) {
            LocalDate end = LocalDate.parse(filters.getFechaFin().trim());
            predicates.add(cb.lessThanOrEqualTo(root.get("dateCreate"), end));
        }
        if (filters.getTipoProducto() != null && !filters.getTipoProducto().trim().isEmpty()) {
            ProductType type = ProductType.valueOf(filters.getTipoProducto().trim());
            predicates.add(cb.equal(root.get("product").get("typeProduct"), type));
        }
    }

}

