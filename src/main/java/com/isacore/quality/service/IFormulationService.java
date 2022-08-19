package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.FormulaList;
import com.isacore.quality.model.Formulation;
import com.isacore.util.CRUD;

public interface IFormulationService extends CRUD<Formulation>{
	
	List<Formulation> findFormulationByProductAndFormType(String idPSap, Integer idF);
	
	Boolean validateExistFormulation(Integer idFi, Integer idFl, Integer idProd);
	
	List<FormulaList> getTypeFormulationsListByProduct(Integer idProd);
	
	int createFormulation(Formulation f, String user);
	
	int updateFormulation(Formulation f, String user);
	
}
