package com.isacore.quality.service;

import com.isacore.quality.dto.OutputMaterialDTO;
import com.isacore.quality.model.ExitMaterialHistory;
import com.isacore.util.CRUD;

public interface IExitMaterialHistoryService extends CRUD<ExitMaterialHistory> {

	OutputMaterialDTO buscarPorIdPNC(Integer id);
	
	
	
	
}
