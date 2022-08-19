package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.ConfigurationFormTestEntryMP;
import com.isacore.quality.model.PropertyList;
import com.isacore.util.CRUD;

public interface IConfigurationFormTestEntryMPService extends CRUD<ConfigurationFormTestEntryMP>{
	
	List<ConfigurationFormTestEntryMP> listByProductTypeText(String productTypeText);
	
	List<String> listCatalogProductTypeText();
	
	List<PropertyList> listOnlyPropertiesByProductTypeText(String productTypeText);
	
}
