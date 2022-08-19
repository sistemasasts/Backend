package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.dto.InformationAditionalFileDTO;
import com.isacore.quality.model.InformationAditional;
import com.isacore.util.CRUD;

public interface IInformationAditionalService extends CRUD<InformationAditional> {

	InformationAditional addImage(String jsonCriteria, byte[] file);
	
	byte[] readFile(String path);
	
	List<InformationAditionalFileDTO> readFiles(long criteriaId);	
	
	void deleteByCriteriaId(long criteriaId);

}
