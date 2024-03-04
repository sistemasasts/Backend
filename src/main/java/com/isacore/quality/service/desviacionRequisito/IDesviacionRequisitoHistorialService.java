package com.isacore.quality.service.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoHistorial;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisitoOrdenFlujo;

import java.util.List;

public interface IDesviacionRequisitoHistorialService {

    void agregar(DesviacionRequisito salidaMaterial, DesviacionRequisitoOrdenFlujo ordenFlujo, String observacion);

    List<DesviacionRequisitoHistorial> buscarHistorial(long id);

}
