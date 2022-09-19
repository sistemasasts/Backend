package com.isacore.quality.service.impl.configuracionFlujo;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.configuracionFlujo.ConfiguracionGeneralFlujo;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.repository.configuracionFlujo.IConfiguracionGeneralFlujoRepo;
import com.isacore.quality.service.configuracionFlujo.IConfiguracionGeneralFlujoService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracionGeneralFlujoServiceImpl implements IConfiguracionGeneralFlujoService {
    private static final Log LOG = LogFactory.getLog(ConfiguracionGeneralFlujoServiceImpl.class);

    private IConfiguracionGeneralFlujoRepo repo;

    @Autowired
    public ConfiguracionGeneralFlujoServiceImpl(IConfiguracionGeneralFlujoRepo repo) {
        this.repo = repo;
    }

    @Transactional
    @Override
    public void modificar(ConfiguracionGeneralFlujo configuracionGeneralFlujo) {
        Optional<ConfiguracionGeneralFlujo> configuracion = this.repo.findById(configuracionGeneralFlujo.getId());
        if(!configuracion.isPresent())
            throw new SolicitudEnsayoErrorException("Configuración no encontrada");

        configuracion.get().setValorConfiguracion(configuracionGeneralFlujo.getValorConfiguracion());
        LOG.info(String.format("Configuración modificada %s", configuracion.get()));
    }

    @Override
    public List<ConfiguracionGeneralFlujo> listarConfiguracionesPorTipoSolicitud(TipoSolicitud tipo) {
        return this.repo.findByTipoSolicitud(tipo);
    }
}
