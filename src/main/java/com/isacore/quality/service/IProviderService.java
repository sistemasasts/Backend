package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.Provider;
import com.isacore.util.CRUD;

public interface IProviderService extends CRUD<Provider>{

	List<Provider> findByProductId(Integer idP);
	
	List<Provider> findByProductIdVigente(Integer idP);
	
}
