package com.isacore.quality.service.spp;

import com.isacore.quality.model.spp.EstadoSolicitudPP;
import com.isacore.quality.model.spp.OrdenFlujoPP;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoDocumento;

import java.util.List;

public interface ISolicitudPruebaProcesoDocumentoService {

	SolicitudPruebaProcesoDocumento subir(String jsonDTO, byte[] file, String nombreArchivo, String tipo);
	
	byte[] descargar(long id);
	
	boolean eliminar(long id);
	
	List<SolicitudPruebaProcesoDocumento> buscarPorEstadoYOrdenYSolicitudId(EstadoSolicitudPP estado, OrdenFlujoPP orden, long solicitudId);
	
	byte[] descargarPorHistorialId(long historialId);
}
