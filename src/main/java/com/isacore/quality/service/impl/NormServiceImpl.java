package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.Norm;
import com.isacore.quality.repository.INormRepo;
import com.isacore.quality.service.INormService;

@Service
public class NormServiceImpl implements INormService{
	
	@Autowired
	private INormRepo repo;

	@Override
	public List<Norm> findAll() {
		return this.repo.findAll();
	}

	@Override
	public Norm create(Norm obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Norm findById(Norm n) {
		Optional<Norm> o = this.repo.findById(n.getIdNorm());
		if(o.isPresent())
			return o.get();
		else
			return null;
	}

	@Override
	public Norm update(Norm obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public List<Norm> findByKindNorm(Norm n) {
		//return this.repo.findByKindNorm(n.getKind());
		return null;
	}


}
