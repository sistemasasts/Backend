package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.dto.ProdProvDTO;
import com.isacore.quality.model.ProdProv;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.Provider;
import com.isacore.util.CRUD;

public interface IProdProvService extends CRUD<ProdProv>{

	Integer validateProdProv(Integer idProduct, Integer idProveedor);
	
	List<ProdProvDTO> findByProduct(Product product);
	
	int deleteByProductAndProvider(Integer idProduct, Integer IdProvider);

	List<Provider> findProviderAssignNot(Product product);
	
	int createProdProv(ProdProvDTO dto);

	int updateProdProv(ProdProvDTO dto);
	
	
}
