package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.dto.ReportTest;
import com.isacore.quality.model.Test;
import com.isacore.util.CRUD;

public interface ITestService extends CRUD<Test>{
	
	List<Test> findByBatch(String batch);
	
	List<Test> findByBatchAll(String batch);
	
	List<Test> findByProductIDBatchNull(Integer idp, String idProp);
	
	List<Test> findByBatchAndPromissingNull(String batch);
	
	List<Test> findByBatchMP(String batch);
	
	List<Test> findByBatchAndIdProduct(String batch, Integer idP);
	
	List<Object[]> generateDataReport(String dateIni, String dateFin);
}
