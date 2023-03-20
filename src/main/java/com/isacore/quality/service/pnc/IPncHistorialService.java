package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.PncHistorial;
import com.isacore.quality.model.pnc.PncOrdenFlujo;
import com.isacore.quality.model.pnc.PncSalidaMaterial;

import java.util.List;

public interface IPncHistorialService {

    void agregar(PncSalidaMaterial salidaMaterial, PncOrdenFlujo ordenFlujo, String observacion);

    List<PncHistorial> buscarHistorialSalidaMaterial(long salidaMaterialId);

}
