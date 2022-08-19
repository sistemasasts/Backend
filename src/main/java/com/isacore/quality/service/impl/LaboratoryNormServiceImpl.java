package com.isacore.quality.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.isacore.quality.exception.LaboratoryNormEliminarErrorException;
import com.isacore.quality.model.LaboratoryNorm;
import com.isacore.quality.model.NormState;
import com.isacore.quality.repository.ILaboratoryNormRepo;
import com.isacore.quality.service.ILaboratoryNormService;

@Service
public class LaboratoryNormServiceImpl implements ILaboratoryNormService {

	private static final Log LOG = LogFactory.getLog(LaboratoryNormServiceImpl.class);
	
	@Autowired
	private ILaboratoryNormRepo repository;
	
	@Autowired
	private Gson gsonLog;
	
	@Override
	public List<LaboratoryNorm> findAll() {
		return repository.findAll();
	}

	@Override
	public LaboratoryNorm create(LaboratoryNorm obj) {
		LOG.info(String.format("Registrando Norma de Laboraorio %s", gsonLog.toJson(obj)));
		return repository.save(obj);
	}

	@Override
	public LaboratoryNorm findById(LaboratoryNorm obj) {
		Optional<LaboratoryNorm> optional = repository.findById(obj.getId());
		if(optional.isPresent())
			return optional.get();
		
		return null;
	}

	@Override
	public LaboratoryNorm update(LaboratoryNorm obj) {
		LOG.info(String.format("Actualizando Norma de Laboraorio %s", gsonLog.toJson(obj)));
		return repository.save(obj);
	}

	@Override
	public boolean delete(String id) {
		return false;
	}

	@Override
	public List<LaboratoryNorm> listNormsVigentes() {
		return repository.findByStateIn(Arrays.asList(NormState.VIGENTE));
	}

	@Override
	public boolean deleteById(long id) {
		try {
			
			LOG.info(String.format("Eliminado Norma de Laboraorio id: %s", id));
			repository.deleteById(id);
			
		} catch (Exception e) {
			throw new LaboratoryNormEliminarErrorException();
		}
		return true;
	}

	@Override
	public List<LaboratoryNorm> listNormsVigentesAssignNot(List<Long> ids) {
		if(ids.isEmpty())
			return repository.findByStateIn(Arrays.asList(NormState.VIGENTE));
		else			
			return repository.findByIdNotInAndStateIn(ids, Arrays.asList(NormState.VIGENTE));
		
	}

}
