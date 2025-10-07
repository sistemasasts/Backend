package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.LaboratoryNorm;
import com.isacore.quality.model.PropertyList;
import com.isacore.util.CRUD;

public interface IPropertyListService extends CRUD<PropertyList> {

	List<PropertyList> findAllOnlyProperty();

	PropertyList findOneOnlyPropertyById(PropertyList pl);

	PropertyList buscarPorId(String id);
	
	List<PropertyList> BuscarTodasPropiedades();

	List<LaboratoryNorm> findNormsAssignNot(String idPropl);

	PropertyList agregarNorma(String id, long idNorma);

	PropertyList eliminarNorma(String id, long idNorma);

}
