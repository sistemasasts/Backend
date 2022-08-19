package com.isacore.quality.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.Test;
import com.isacore.quality.repository.ITestRepo;
import com.isacore.quality.service.ITestService;

@Service
public class TestServiceImpl implements ITestService {

	@Autowired
	private ITestRepo repo;

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

}
