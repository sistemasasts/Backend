package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.*;

import java.util.List;

public interface IPncDocumentoService {

    PncDocumento registrar(ProductoNoConforme productoNoConforme, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden);

    PncDocumento registrar(PncSalidaMaterial salidaMaterial, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden);

    PncDocumento subirArchivo(String jsonDTO, byte[] file, String nombreArchivo, String tipo);

    List<PncDocumento> buscarPorEstadoYOrdenYSalidaId(EstadoSalidaMaterial estado, PncOrdenFlujo orden, long salidaId);

    boolean eliminarDocumento(long documentoId);

    PncDocumento listarDocumentoPorId(long id);

    byte[] descargar(long id);
}
