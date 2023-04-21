package com.isacore.quality.service.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.util.CRUD;

import java.util.List;

public interface IDesviacionRequisitoService extends CRUD<DesviacionRequisito> {
    List<DesviacionRequisito> listarDesviacionRequistoActivos();

    DesviacionRequisito listarDesviacionRequisitosPorId(Long desviacionId);
}
