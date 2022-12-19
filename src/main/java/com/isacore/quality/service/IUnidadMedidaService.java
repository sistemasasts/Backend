package com.isacore.quality.service;

import com.isacore.quality.model.UnidadMedida;
import com.isacore.util.CRUD;

import java.util.List;

public interface IUnidadMedidaService extends CRUD<UnidadMedida> {

    List<UnidadMedida> listarActivos();
}
