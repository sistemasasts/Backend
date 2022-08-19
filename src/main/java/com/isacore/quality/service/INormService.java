package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.Norm;
import com.isacore.util.CRUD;

public interface INormService  extends CRUD<Norm>{

	List<Norm> findByKindNorm(Norm n);
	
}
