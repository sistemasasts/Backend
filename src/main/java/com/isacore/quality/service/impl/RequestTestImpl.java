package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isacore.quality.model.ActionProcess;
import com.isacore.quality.model.RequestTest;
import com.isacore.quality.repository.IRequestTestRepo;
import com.isacore.quality.service.IRequestTestService;
@Component
public class RequestTestImpl implements IRequestTestService{

	@Autowired
	private IRequestTestRepo repo;
	
	@Override
	public List<RequestTest> findAll() {
		List<RequestTest> rtTmp = this.repo.findAll();
		return rtTmp;
	}

	@Override
	public RequestTest create(RequestTest obj) {
		return this.repo.save(obj);
	}

	@Override
	public RequestTest findById(RequestTest rt) {
		Optional<RequestTest> rtTmp = this.repo.findById(rt.getIdRequestTest());
		return rtTmp.get();
	}

	@Override
	public RequestTest update(RequestTest obj) {
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

}
