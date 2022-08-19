package com.isacore.sgc.acta.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isacore.sgc.acta.model.ActionPlan;
import com.isacore.sgc.acta.service.impl.ActionPlanServiceImpl;

@RestController
@RequestMapping(value = "/actionplan")
public class ActionPlanController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ActionPlanServiceImpl planService;
	
	@RequestMapping(value = "/findAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ActionPlan>> findAll(){
		
		List<ActionPlan> plans = this.planService.findAll();
		
		this.logger.info("> obtiene la lista de planes de accion");
		
		if(plans == null || plans.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<List<ActionPlan>>(plans,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/findByIdMinute", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ActionPlan>> finByIdMinute(@RequestParam(value = "idMinute") String idMinute){		
		return new ResponseEntity<List<ActionPlan>>(this.planService.findPlanByIdMinute(idMinute),HttpStatus.OK);
	}
}
