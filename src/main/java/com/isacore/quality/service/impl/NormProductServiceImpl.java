package com.isacore.quality.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.NormProduct;
import com.isacore.quality.repository.INormProductRepo;
import com.isacore.quality.service.INormProductService;

@Service
public class NormProductServiceImpl implements INormProductService {

	@Autowired
	private INormProductRepo repo;
	
	@Override
	public List<NormProduct> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NormProduct create(NormProduct obj) {
		return this.repo.save(obj);
	}

	@Override
	public NormProduct findById(NormProduct id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NormProduct update(NormProduct obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}
	

}
