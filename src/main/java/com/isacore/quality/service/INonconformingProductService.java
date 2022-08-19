package com.isacore.quality.service;

import com.isacore.quality.model.NonconformingProduct;
import com.isacore.util.CRUD;

public interface INonconformingProductService extends CRUD<NonconformingProduct>{
	
	public void consumeMaterialNC(Integer ncp, Double quantity) ;
	
	public void eliminarMaterilaConsumido(Integer ncp, Double quantity );
	
	byte[] generarReportePnc(Integer id);
	
}
