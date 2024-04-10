package com.isacore.quality.service.impl.reclamoMP;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.isacore.quality.model.reclamoMP.ExecutedAction;
import com.isacore.quality.repository.IExecutedActionRepo;
import com.isacore.quality.service.IExecutedActionService;

public class ExecutedActionImpl implements IExecutedActionService {
	@Autowired
	private IExecutedActionRepo repo;
	
	@Override
	public List<ExecutedAction> findAll() {
		return this.repo.findAll();
	}

	@Override
	public ExecutedAction create(ExecutedAction obj) {
		return this.repo.save(obj);
	}

	@Override
	public ExecutedAction findById(ExecutedAction obj) {
		Optional<ExecutedAction> com = this.repo.findById(1);
		if (com.isPresent())
			return com.get();
		else
			return null;
	}

	@Override
	public ExecutedAction update(ExecutedAction obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		// TODO Auto-generated method stub
		return true;
		
	}

}
