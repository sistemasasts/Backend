package com.isacore.sgc.acta.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.sgc.acta.model.ActionPlan;
import com.isacore.sgc.acta.repository.IActionPlanRepo;
import com.isacore.sgc.acta.service.IActionPlanService;

@Service
public class ActionPlanServiceImpl implements IActionPlanService{

	@Autowired
	private IActionPlanRepo repo;
	
	@Override
	public List<ActionPlan> findAll() {
		return this.repo.findAll();
	}

	@Override
	public ActionPlan create(ActionPlan obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionPlan findById(ActionPlan ap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActionPlan update(ActionPlan obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(String id) {
		return true;

	}

	@Override
	public List<ActionPlan> findPlanByIdMinute(String idMinute) {		
		return this.repo.findPlanByIdMinute(idMinute);
	}

}
