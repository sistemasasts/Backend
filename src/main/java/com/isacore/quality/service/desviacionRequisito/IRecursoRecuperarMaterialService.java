package com.isacore.quality.service.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.RecursoRecuperarMaterial;
import com.isacore.util.CRUD;

import java.util.List;

public interface IRecursoRecuperarMaterialService extends CRUD<RecursoRecuperarMaterial> {

    List<RecursoRecuperarMaterial> listarPorDesviacionRequisitoId(Long desviacionRequisitoId);

    boolean eliminarPorId(Long id);
}
