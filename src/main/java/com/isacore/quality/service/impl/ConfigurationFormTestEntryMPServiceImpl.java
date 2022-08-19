package com.isacore.quality.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isacore.quality.exception.ConfigFormEntryPropertyExistsException;
import com.isacore.quality.model.ConfigurationFormTestEntryMP;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.repository.IConfigurationFormTestEntryRepo;
import com.isacore.quality.service.IConfigurationFormTestEntryMPService;

@Service
public class ConfigurationFormTestEntryMPServiceImpl implements IConfigurationFormTestEntryMPService {
	
	@Autowired
	private IConfigurationFormTestEntryRepo repo;

	@Override
	public List<ConfigurationFormTestEntryMP> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConfigurationFormTestEntryMP create(ConfigurationFormTestEntryMP obj) {
		validarPropiedadEstaAsociada(obj.getProductTypeText(), obj.getProperty());
		return repo.save(obj);
	}

	@Override
	public ConfigurationFormTestEntryMP findById(ConfigurationFormTestEntryMP id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConfigurationFormTestEntryMP update(ConfigurationFormTestEntryMP obj) {
		return repo.save(obj);
	}

	@Override
	public boolean delete(String id) {
		repo.deleteById(Integer.parseInt(id));
		return true;
	}

	@Override
	public List<ConfigurationFormTestEntryMP> listByProductTypeText(String productTypeText) {
		return repo.findByProductTypeText(productTypeText);
	}

	@Override
	public List<String> listCatalogProductTypeText() {
		List<String> catalog = new ArrayList<>();
		List<Object[]> result =repo.findCatalogByTypeText();
		if(!result.isEmpty()) {
			result.forEach(x -> {
				catalog.add((String) x[0]);
			});
		}
		return catalog;
	}
	
	private void validarPropiedadEstaAsociada(String productTypeText, PropertyList property) {
		Optional<ConfigurationFormTestEntryMP> configOp = repo.findByProductTypeTextAndProperty_IdProperty(productTypeText, property.getIdProperty());
		if(configOp.isPresent())
			throw new ConfigFormEntryPropertyExistsException(property.getNameProperty(), productTypeText);
	}

	@Override
	public List<PropertyList> listOnlyPropertiesByProductTypeText(String productTypeText) {
		return repo.findByProductTypeText(productTypeText).stream().map(x -> x.getProperty()).collect(Collectors.toList());
	}
	
	
}
