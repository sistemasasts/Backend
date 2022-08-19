package com.isacore.sgc.acta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.sgc.acta.model.Committee;
import com.isacore.sgc.acta.repository.ICommitteeRepo;
import com.isacore.sgc.acta.service.ICommitteeService;

@Service
public class CommitteeServiceImpl implements ICommitteeService{

	@Autowired
	private ICommitteeRepo repo;
	
	@Override
	public List<Committee> findAll() {
		return this.repo.findAll();
	}

	@Override
	public Committee create(Committee obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Committee findById(Committee id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Committee update(Committee obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

}
