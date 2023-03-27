package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.EstadoSalidaMaterial;
import com.isacore.quality.model.pnc.PncSalidaMaterial;
import com.isacore.quality.model.pnc.PncSalidaMaterialDto;

import java.util.List;

public interface IPncSalidaMaterialService {

    PncSalidaMaterialDto registrar(PncSalidaMaterialDto dto);

    PncSalidaMaterialDto actualizar(PncSalidaMaterialDto dto);

    PncSalidaMaterialDto listarPorId(long id);

    PncSalidaMaterial listarPorIdCompleto(long id);

    List<PncSalidaMaterialDto> listarPorPncId(long pncId);

    void enviarAprobacion(PncSalidaMaterialDto dto);

    void aprobarSalidaMaterial(PncSalidaMaterialDto dto);

    List<PncSalidaMaterialDto> listarPorEstado(EstadoSalidaMaterial estadoSalidaMaterial);

    List<PncSalidaMaterialDto> eliminar(long pncId, long id);
}
