package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.dto.ReportTest;
import com.isacore.quality.dto.TestCriterioDTO;
import com.isacore.quality.model.Test;
import com.isacore.quality.model.VistaTest;
import com.isacore.util.CRUD;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITestService extends CRUD<Test> {

    List<Test> findByBatch(String batch);

    List<Test> findByBatchAll(String batch);

    List<Test> findByProductIDBatchNull(Integer idp, String idProp);

    List<Test> findByBatchAndPromissingNull(String batch);

    List<Test> findByBatchMP(String batch);

    List<Test> findByBatchAndIdProduct(String batch, Integer idP);

    List<Object[]> generateDataReport(String dateIni, String dateFin);

    Page<VistaTest> consultarPorCriterio(Pageable pageable, TestCriterioDTO dto);

    byte[] generarReporte(TestCriterioDTO dto);
}
