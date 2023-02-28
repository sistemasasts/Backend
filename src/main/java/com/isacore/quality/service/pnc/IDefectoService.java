package com.isacore.quality.service.pnc;

import com.isacore.quality.model.pnc.Defecto;
import com.isacore.util.CRUD;

import java.util.List;

public interface IDefectoService extends CRUD<Defecto> {

    List<Defecto> listarActivos();
}
