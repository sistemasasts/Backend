package com.isacore.quality.service.se;

import java.util.List;

import com.isacore.quality.model.se.SolicitudHistorial;
import com.isacore.quality.model.spp.HistorialPPCompletoDto;
import com.isacore.util.CRUD;

public interface ISolicitudHistorialService extends CRUD<SolicitudHistorial> {

	List<SolicitudHistorial> buscarHistorial(long solicitudId);

    List<HistorialPPCompletoDto> buscarHistorialCompleto(long solicitudId);
//	List<SolicitudHistorial> buscarHistorialPruebasProceso(long solicitudId);

}
