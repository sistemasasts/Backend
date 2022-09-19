package com.isacore.quality.service.configuracionFlujo;

import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.se.TipoSolicitud;

import java.util.List;

public interface IConfiguracionGeneralFlujoService {

    void modificar(ConfiguracionGeneralFlujo configuracionGeneralFlujo);

    List<ConfiguracionGeneralFlujo> listarConfiguracionesPorTipoSolicitud(TipoSolicitud tipo);
}
