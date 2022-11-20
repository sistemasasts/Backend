package com.isacore.quality.service.impl;

import com.isacore.quality.dto.TestCriterioDTO;
import com.isacore.quality.exception.ErrorReporteException;
import com.isacore.quality.model.Test;
import com.isacore.quality.model.VistaTest;
import com.isacore.quality.repository.ITestRepo;
import com.isacore.quality.repository.IVistaTestRepositorio;
import com.isacore.quality.service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;

@Slf4j
@Service
public class TestServiceImpl implements ITestService {
    private ITestRepo repo;
    private IVistaTestRepositorio vistaTestRepositorio;
    private EntityManager entityManager;
    private GeneradorReporteTest generadorReporteTest;

    @Autowired
    public TestServiceImpl(
        ITestRepo repo,
        IVistaTestRepositorio vistaTestRepositorio,
        EntityManager entityManager,
        GeneradorReporteTest generadorReporteTest) {
        this.repo = repo;
        this.vistaTestRepositorio = vistaTestRepositorio;
        this.entityManager = entityManager;
        this.generadorReporteTest = generadorReporteTest;
    }

    @Override
    public List<Test> findAll() {
        return this.repo.findAll();
    }

    @Override
    public Test create(Test obj) {
        return this.repo.save(obj);
    }

    @Override
    public Test findById(Test obj) {
        Optional<Test> test = this.repo.findById(obj.getIdTest());
        if (test.isPresent())
            return test.get();
        else
            return null;
    }

    @Override
    public Test update(Test obj) {
        return this.repo.save(obj);
    }

    @Override
    public boolean delete(String id) {
        return true;

    }

    @Override
    public List<Test> findByBatch(String batch) {
        return this.repo.findByBatch(batch);
    }

    @Override
    public List<Test> findByProductIDBatchNull(Integer idP, String idProp) {
        List<Test> t = this.repo.findByIdProduct(idP, idProp);
        List<Test> tb = new ArrayList<>();
        t.forEach((Test x) -> {
            if (x.getBatchTest().length() == 0) {
                tb.add(x);
            }

        });
        return tb;
    }

    @Override
    public List<Test> findByBatchAndPromissingNull(String batch) {
        return this.repo.findByBatchAndPromissing(batch);
    }

    @Override
    public List<Test> findByBatchMP(String batch) {
        return this.repo.findByBatchMP(batch);
    }

    @Override
    public List<Test> findByBatchAndIdProduct(String batch, Integer idP) {
        return this.repo.findByBatchAndIdProduct(batch, idP);
    }

    @Override
    public List<Test> findByBatchAll(String batch) {
        return this.repo.findByBatchAll(batch);
    }

    @Override
    public List<Object[]> generateDataReport(String dateIni, String dateFin) {

        return this.repo.dataReport(dateIni, dateFin);
    }

    @Override
    public Page<VistaTest> consultarPorCriterio(Pageable pageable, TestCriterioDTO dto) {
        try {
            List<VistaTest> descuentosResult = this.consultarEnsayos(dto);
            final int sizeTotal = descuentosResult.size();
            final int start = (int) pageable.getOffset();
            final int end = (start + pageable.getPageSize()) > descuentosResult.size() ? descuentosResult.size() : (start + pageable.getPageSize());
            descuentosResult = descuentosResult.subList(start, end);
            final Page<VistaTest> pageResult = new PageImpl<VistaTest>(descuentosResult, pageable, sizeTotal);
            return pageResult;
        } catch (final Exception ex) {
            log.error(String.format("Error al momento de consultar tests por los criterios %s : ex: %s", dto, ex));
            final Page<VistaTest> pageResult = new PageImpl<VistaTest>(new ArrayList<VistaTest>(), pageable, 0);
            return pageResult;
        }
    }

    @Override
    public byte[] generarReporte(TestCriterioDTO dto) {
        try {
            log.info(String.format("Generando reporte de ensayos %s", dto));
            List<VistaTest> resultado = this.consultarEnsayos(dto);
            final Workbook workbook = this.generadorReporteTest.generar(resultado);
            ByteArrayOutputStream ms = new ByteArrayOutputStream();
            workbook.write(ms);
            return ms.toByteArray();
        } catch (IOException e) {
            log.error(String.format("Error al momento de genrear el reporte de Obras Qr %s", e.getCause()));
            throw new ErrorReporteException();
        }
    }

    private List<VistaTest> consultarEnsayos(TestCriterioDTO dto) {
        final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<VistaTest> query = criteriaBuilder.createQuery(VistaTest.class);

        final Root<VistaTest> descuentoRoot = query.from(VistaTest.class);
        final List<Predicate> predicadosConsulta = new ArrayList<Predicate>();

        if (dto.getFechaInicio() != null && dto.getFechaFin() != null)
            predicadosConsulta.add(criteriaBuilder.between(descuentoRoot.get("fecha"), dto.getFechaInicio().withHour(0).withMinute(0).withSecond(0),
                dto.getFechaFin().withHour(23).withMinute(59).withSecond(59)));

        if (dto.getFechaInicio() != null && dto.getFechaFin() == null)
            predicadosConsulta.add(criteriaBuilder.between(descuentoRoot.get("fecha"), dto.getFechaInicio(), LocalDateTime.now()));

        if (dto.getProductoId() != null)
            predicadosConsulta.add(criteriaBuilder.equal(descuentoRoot.get("productoId"), dto.getProductoId()));

        if (noEsNuloNiBlanco(dto.getPropiedadId()))
            predicadosConsulta.add(criteriaBuilder.equal(descuentoRoot.get("propiedadId"), dto.getPropiedadId()));

        query.where(predicadosConsulta.toArray(new Predicate[predicadosConsulta.size()])).orderBy(criteriaBuilder.desc(descuentoRoot.get("fecha")));
        final TypedQuery<VistaTest> statement = this.entityManager.createQuery(query);
        return statement.getResultList();
    }

}
