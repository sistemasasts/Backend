package com.isacore.quality.service;

import java.util.List;

import com.isacore.quality.model.HccHead;
import com.isacore.quality.model.HccdResultadoDto;
import com.isacore.util.CRUD;

public interface IHccHeadService extends CRUD<HccHead>{

	List<HccHead> findOnlyHccHead(String tp);
	
	List<HccHead> findOnlyHccHead();

	HccdResultadoDto registrarConImagen(String json, byte[] file, String nombreArchivo, String tipo);
	
}
