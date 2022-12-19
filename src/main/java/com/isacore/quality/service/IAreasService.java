package com.isacore.quality.service;

import com.isacore.quality.model.Area;
import com.isacore.util.CRUD;

import java.util.List;

public interface IAreasService extends CRUD<Area>{

    List<Area> listarActivasPruebasProcesos();
}
