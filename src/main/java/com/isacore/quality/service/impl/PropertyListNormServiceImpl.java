package com.isacore.quality.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.model.PropertyListNorm;
import com.isacore.quality.repository.IPropertyListNormRepo;
import com.isacore.quality.service.IPropertyListNormService;

@Service
public class PropertyListNormServiceImpl implements IPropertyListNormService {

	private static final Log LOG = LogFactory.getLog(PropertyListNormServiceImpl.class);
	
	@Autowired
	private IPropertyListNormRepo repository;
	
	@Autowired
	private Gson gsonLog;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void associatePropertyListAndNorms(List<PropertyListNorm> norms, PropertyList propertyList) {
		cleanNormsByPropId(propertyList.getIdProperty());
		savePropertyListAndNorms(norms, propertyList);						
	}
	
	private void cleanNormsByPropId(String propId) {
		LOG.info(String.format("Suprimiendo Normas asociadas de la propiedad %s", propId));
		repository.deleteByPropertyId(propId);
	}
	
	private void savePropertyListAndNorms(List<PropertyListNorm> norms, PropertyList propertyList) {
		LOG.info(String.format("Agregando a la propiedad %s las siguientes normas %s", propertyList.getIdProperty(), gsonLog.toJson(norms)));
		
		norms.forEach(x -> {
			repository.insertPropertyListNorm(x.getLaboratoryNorm().getId(), propertyList.getIdProperty());
		});		
		
	}

}
