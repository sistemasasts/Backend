package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.LaboratoryNorm;
import com.isacore.util.CRUD;

public interface ILaboratoryNormService extends CRUD<LaboratoryNorm> {

	List<LaboratoryNorm> listNormsVigentes();
	
	List<LaboratoryNorm> listNormsVigentesAssignNot(List<Long> ids);
	
	boolean deleteById(long id);
}
