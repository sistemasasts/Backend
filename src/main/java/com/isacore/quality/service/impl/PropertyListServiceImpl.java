package com.isacore.quality.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.isacore.quality.exception.PropertyDeleteErrorException;
import com.isacore.quality.model.LaboratoryNorm;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.model.PropertyListNorm;
import com.isacore.quality.repository.IPropertyListNormRepo;
import com.isacore.quality.repository.IPropertyListRepo;
import com.isacore.quality.service.ILaboratoryNormService;
import com.isacore.quality.service.IPropertyListNormService;
import com.isacore.quality.service.IPropertyListService;
import com.isacore.quality.service.IPropertyService;

@Service
public class PropertyListServiceImpl implements IPropertyListService {

	private static final Log LOG = LogFactory.getLog(PropertyListServiceImpl.class);

	@Autowired
	private IPropertyListRepo repo;

	@Autowired
	private IPropertyListNormRepo propertyListNormRepo;

	@Autowired
	private IPropertyListNormService propertyListNormService;

	@Autowired
	private ILaboratoryNormService laboratoryNormService;

	@Autowired
	private Gson gsonLog;
	
	@Autowired
	private IPropertyService propertyService;

	@Override
	public List<PropertyList> findAll() {
		return this.repo.findAll();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public PropertyList create(PropertyList obj) {
		PropertyList propiedad = new PropertyList(obtenerSiguienteSecuencial(), obj.getNameProperty(),
				obj.getLineApplication(), obj.getPeriodicity(), obj.getMachine(), obj.getTypeProperty(),
				obj.getMethod(), obj.getLaboratory(), obj.getSamplingPlan());
		
		LOG.info(String.format("Creando propiedad %s", propiedad));
		propiedad = repo.save(propiedad);
		
		if (!obj.getNorms().isEmpty())
			propertyListNormService.associatePropertyListAndNorms(obj.getNorms(), propiedad);
		
		return propiedad;
	}

	@Override
	public PropertyList findById(PropertyList id) {
		Optional<PropertyList> propiedad = repo.findById(id.getIdProperty());
		return propiedad.isPresent() ? propiedad.get() : null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public PropertyList update(PropertyList obj) {

		Optional<PropertyList> propertyoptional = repo.findById(obj.getIdProperty());

		if (propertyoptional.isPresent()) {

			PropertyList property = propertyoptional.get();

			LOG.info(String.format("Actualizando Propiedad %s", gsonLog.toJson(obj)));

			property.setLaboratory(obj.getLaboratory());
			property.setLineApplication(obj.getLineApplication());
			property.setMachine(obj.getMachine());
			property.setMethod(obj.getMethod());
			property.setNameProperty(obj.getNameProperty());
			property.setPeriodicity(obj.getPeriodicity());
			property.setSamplingPlan(obj.getSamplingPlan());
			property.setTypeProperty(obj.getTypeProperty());
			propertyListNormService.associatePropertyListAndNorms(obj.getNorms(), obj);
			propertyService.apdateNormsPropertiesByPropertyListId(
					obj.getIdProperty(), obj.getNorms().stream().map(x ->x.getLaboratoryNorm().getName()).collect(Collectors.joining("/")));
			return property;
		}
		return null;
	}

	@Override
	public boolean delete(String id) {
		try {
			repo.deleteById(id);
		} catch (Exception e) {
			throw new PropertyDeleteErrorException();
		}
		return true;
	}

	@Override
	public List<PropertyList> findAllOnlyProperty() {
		return this.repo.findAllOnlyProperty();
	}

	@Override
	public PropertyList findOneOnlyPropertyById(PropertyList pl) {
		return this.repo.findOneOnlyPropertyById(pl.getIdProperty());
	}

	@Override
	public List<PropertyList> BuscarTodasPropiedades() {

		// return repo.findAllOrderByNamePropertyDesc();
		return new ArrayList<>();
	}

	private String obtenerSiguienteSecuencial() {
		int codigo = repo.secuencialSiguiente();
		return "PROP_" + codigo;
	}

	@Override
	public List<LaboratoryNorm> findNormsAssignNot(String idPropl) {

		List<LaboratoryNorm> normsByAssigned = new ArrayList<>();

		LOG.info("Buscar Normas Laboratorio asignadas a la propiedad:" + idPropl);

		List<PropertyListNorm> normas = this.propertyListNormRepo.findByPropertyList_IdProperty(idPropl);

		LOG.info("Normas Laboratorio asignadas :" + normas.size());

		List<Long> ids = normas.stream().map(x -> x.getLaboratoryNorm().getId()).collect(Collectors.toList());

		normsByAssigned = laboratoryNormService.listNormsVigentesAssignNot(ids);

		LOG.info("Normas Laboratorio no asignadas :" + normsByAssigned.size());

		return normsByAssigned;
	}
}
