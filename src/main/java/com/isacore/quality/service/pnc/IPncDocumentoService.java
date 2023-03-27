package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.*;

import java.util.List;
import java.util.Optional;

public interface IPncDocumentoService {

    PncDocumento registrar(ProductoNoConforme productoNoConforme, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden);

    PncDocumento registrar(PncSalidaMaterial salidaMaterial, Optional<PncPlanAccion> planAccionId, byte[] bytes, String nombreArchivo, String tipo, PncOrdenFlujo orden);

    PncDocumento subirArchivo(String jsonDTO, byte[] file, String nombreArchivo, String tipo);

    List<PncDocumento> buscarPorEstadoYOrdenYSalidaId(EstadoSalidaMaterial estado, PncOrdenFlujo orden, long salidaId);

    List<PncDocumento> buscarPorOrdenYSalidaIdYPlanAccionId(PncOrdenFlujo orden, long salidaId, long planAccionId);

    boolean eliminarDocumento(long documentoId);

    PncDocumento listarDocumentoPorId(long id);

    byte[] descargar(long id);
}
