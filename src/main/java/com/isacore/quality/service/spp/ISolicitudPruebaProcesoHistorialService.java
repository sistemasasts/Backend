package com.isacore.quality.service.spp;

import com.isacore.quality.model.spp.SolicitudPruebaProcesoHistorial;
import com.isacore.util.CRUD;

import java.util.List;

public interface ISolicitudPruebaProcesoHistorialService extends CRUD<SolicitudPruebaProcesoHistorial> {

	List<SolicitudPruebaProcesoHistorial> buscarHistorial(long solicitudId);
	
}
