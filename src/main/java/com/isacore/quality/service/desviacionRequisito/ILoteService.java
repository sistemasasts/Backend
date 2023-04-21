package com.isacore.quality.service.desviacionRequisito;

import com.isacore.quality.model.desviacionRequisito.Lote;
import com.isacore.util.CRUD;

import java.util.List;

public interface ILoteService extends CRUD<Lote> {
    Lote listarLotePorId(Long id);

    List<Lote> listarLotesPorDesviacionRequisitoId(Long desviacionRequisitoId);

    boolean eliminarLotePorId(Long id);
}
