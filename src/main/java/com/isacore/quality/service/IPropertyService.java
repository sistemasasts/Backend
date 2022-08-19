package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.dto.ProductPropertyDTO;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.Property;
import com.isacore.quality.model.PropertyList;
import com.isacore.util.CRUD;

public interface IPropertyService extends CRUD<Property>{

	String validateExistProperty(Integer idProduct, String idPropertyList);
	
	int createProperty(Property p, String user);
	
	int updateProperty(Property p, String user);

	Property findByIdProductandIdProperty(Product p, PropertyList pl);
	
	void updatePropertiesByProduct(List<ProductPropertyDTO> listProperty);
	
	List<ProductPropertyDTO> findByProduct(Product product);
	
	int deleteByProductAndProperty(Integer idProduct, String IdPropertyList);
	
	List<PropertyList> findPropertyAssignNot(Product product);
	
	void apdateNormsPropertiesByPropertyListId(String propertuListId, String norms);
	
}
