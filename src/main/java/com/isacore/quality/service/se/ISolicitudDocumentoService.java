package com.isacore.quality.service.se;

import java.util.List;

import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudDocumento;
import com.isacore.util.CRUD;

public interface ISolicitudDocumentoService extends CRUD<SolicitudDocumento> {

	SolicitudDocumento subir(String jsonDTO, byte[] file, String nombreArchivo, String tipo);
	
	byte[] descargar(long id);
	
	boolean eliminar(long id);
	
	List<SolicitudDocumento> buscarPorEstadoYOrdenYSolicitudId(EstadoSolicitud estado,OrdenFlujo orden, long solicitudId);
	
	byte[] descargarPorHistorialId(long historialId);
	
	void validarInformeSubido(long solicitudId, EstadoSolicitud estado);

}
