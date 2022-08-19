package com.isacore.sgc.acta.service;

import java.util.List;

import com.isacore.sgc.acta.model.ActionPlan;
import com.isacore.util.CRUD;

public interface IActionPlanService extends CRUD<ActionPlan>{
	
	List<ActionPlan> findPlanByIdMinute(String idMinute);
	
}
