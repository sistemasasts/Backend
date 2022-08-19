package com.isacore.quality.service;

import com.isacore.quality.model.ActionProcess;
import com.isacore.util.CRUD;

public interface IActionProcessService extends CRUD<ActionProcess> {
	
	
	String generateId();

}
