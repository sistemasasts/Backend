package com.isacore.quality.service.reclamoMP;

import com.isacore.quality.model.reclamoMP.ComplaintDocumento;
import com.isacore.quality.model.reclamoMP.ComplaintOrdenFlujo;

import java.util.List;

public interface IComplaintDocumentoService {

    ComplaintDocumento subirArchivo(String jsonDTO, byte[] file, String nombreArchivo, String tipo);


    List<ComplaintDocumento> buscarPorOrdenYReclamoId(ComplaintOrdenFlujo orden, long salidaId);

    boolean eliminarDocumento(long documentoId);

    byte[] descargar(long id);

    byte[] descargarPorHistorialId(long historialId);
}
