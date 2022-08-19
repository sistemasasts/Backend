package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.isacore.quality.model.ProcessTestRequest;
import com.isacore.quality.repository.IProcessTestRequestRepo;
import com.isacore.quality.service.IProcessTestRequestService;

@Service
public class ProcessTestRequestServiceImpl implements IProcessTestRequestService{
	
	@Autowired
	private IProcessTestRequestRepo repo;
	
	@Override
	public List<ProcessTestRequest> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessTestRequest create(ProcessTestRequest obj) {
		return this.repo.save(obj);
	}

	@Override
	public ProcessTestRequest findById(ProcessTestRequest ptr) {
		Optional<ProcessTestRequest> o = this.repo.findById(ptr.getIdProcessTest());
		if(o.isPresent())
			return o.get();
		else 
			return null;
	}

	@Override
	public ProcessTestRequest update(ProcessTestRequest obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

}
