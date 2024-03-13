package com.isacore.quality.repository;

import com.isacore.RepositorioBase;
import com.isacore.quality.model.comunes.MatrizAprobacionAdicional;
import com.isacore.quality.model.se.TipoSolicitud;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatrizAprobacionAdicionalRepo extends RepositorioBase<MatrizAprobacionAdicional> {

    List<MatrizAprobacionAdicional> findByTipoSolicitud(TipoSolicitud tipoSolicitud);
}
