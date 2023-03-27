package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.EstadoPncPlanAccion;
import com.isacore.quality.model.pnc.PncPlanAccionDto;

import java.util.List;

public interface IPncPlanAccionService {

    List<PncPlanAccionDto> registrar(PncPlanAccionDto dto);

    List<PncPlanAccionDto> actualizar(PncPlanAccionDto dto);

    List<PncPlanAccionDto> listarPorSalidaMaterialId(long salidaMaterialId);

    PncPlanAccionDto listarPorId(long id);

    List<PncPlanAccionDto> eliminar(long id, long salidaMaterilaId);

    void iniciarGestionPlanes(long salidaMaterialId);

    void procesar(PncPlanAccionDto dto);

    List<PncPlanAccionDto> listarPorEstado(EstadoPncPlanAccion estado);

    void eliminarPorSalidaMaterialId(long salidaMaterialId);

}
