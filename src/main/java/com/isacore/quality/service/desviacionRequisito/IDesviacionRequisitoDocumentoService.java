package com.isacore.quality.service.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoDocumento;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoOrdenFlujo;

import java.util.List;

public interface IDesviacionRequisitoDocumentoService {

    DesviacionRequisitoDocumento subirArchivo(String jsonDTO, byte[] file, String nombreArchivo, String tipo);


    List<DesviacionRequisitoDocumento> buscarPorEstadoYOrdenYSalidaId(DesviacionRequisitoOrdenFlujo orden, long salidaId);

    boolean eliminarDocumento(long documentoId);

    byte[] descargar(long id);

    byte[] descargarPorHistorialId(long historialId);
}
