package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.model.ClientImptek;
import com.isacore.quality.repository.IClientImptekRepo;
import com.isacore.quality.service.IClientImptekService;

@Service
public class ClientImptekServiceImpl implements IClientImptekService{

	@Autowired
	private IClientImptekRepo repo;
	
	@Override
	public List<ClientImptek> findAll() {
		return this.repo.findAll();
	}

	@Override
	public ClientImptek create(ClientImptek obj) {
		return this.repo.save(obj);
	}

	@Override
	public ClientImptek findById(ClientImptek c) {
		Optional<ClientImptek> o = this.repo.findById(c.getIdClient());
		if(o.isPresent())
			return o.get();
		else
			return null;
	}

	@Override
	public ClientImptek update(ClientImptek obj) {
		return this.repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		return true;
		
	}

}
