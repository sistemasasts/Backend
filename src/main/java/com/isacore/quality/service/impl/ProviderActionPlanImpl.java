package com.isacore.quality.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isacore.quality.model.Complaint;
import com.isacore.quality.model.ProviderActionPlan;
import com.isacore.quality.repository.IComplaintRepo;
import com.isacore.quality.repository.IProviderActionPlanRepo;
import com.isacore.quality.service.IProviderActionPlanService;

@Component
public class ProviderActionPlanImpl implements IProviderActionPlanService {

	@Autowired
	private IProviderActionPlanRepo repo;

	@Override
	public List<ProviderActionPlan> findAll() {
		return this.repo.findAll();
	}

	@Override
	public ProviderActionPlan create(ProviderActionPlan obj) {
		return this.repo.save(obj);
	}

	@Override
	public ProviderActionPlan findById(ProviderActionPlan obj) {
		Optional<ProviderActionPlan> com = this.repo.findById(obj.getIdPrviderActionPlan());
		if (com.isPresent())
			return com.get();
		else
			return null;
	}

	@Override
	public ProviderActionPlan update(ProviderActionPlan obj) {

		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

}
