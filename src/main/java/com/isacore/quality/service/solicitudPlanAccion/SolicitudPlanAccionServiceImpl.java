package com.isacore.quality.service.solicitudPlanAccion;

import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.quality.model.solicitudPlanAccion.SolicitudPlanAccion;
import com.isacore.quality.repository.solicitudPlanAccion.ISolicitudPlanAccionRepo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolicitudPlanAccionServiceImpl {
    private static final Log LOG = LogFactory.getLog(SolicitudPlanAccionServiceImpl.class);
    private ISolicitudPlanAccionRepo repo;

    @Autowired
    public SolicitudPlanAccionServiceImpl(ISolicitudPlanAccionRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public SolicitudPlanAccion registrar(SolicitudPlanAccion dto){
        SolicitudPlanAccion planAccion = new SolicitudPlanAccion(
            dto.getFechaInicio(),
            dto.getFechaFin(),
            dto.getDescripcion(),
            dto.getCumplido(),
            dto.getTipoSolicitud(),
            dto.getSolicitudId());
        LOG.info(String.format("Plan de accion registrado %s", planAccion));
        return this.repo.save(planAccion);
    }

    @Transactional
    public SolicitudPlanAccion modificar(SolicitudPlanAccion dto){
        SolicitudPlanAccion planAccion = this.repo.findById(dto.getId()).orElse(null);
        if(planAccion == null)
            throw new SolicitudEnsayoErrorException(String.format("Plan de acción no encontrado %s", dto.getId()));
        planAccion.setFechaInicio(dto.getFechaInicio());
        planAccion.setFechaFin(dto.getFechaFin());
        planAccion.setDescripcion(dto.getDescripcion());
        planAccion.setCumplido(dto.getCumplido());
        LOG.info(String.format("Plan de accion modificado %s", planAccion));
        return this.repo.save(planAccion);
    }

    @Transactional
    public SolicitudPlanAccion marcarCumplimiento(SolicitudPlanAccion dto){
        SolicitudPlanAccion planAccion = this.repo.findById(dto.getId()).orElse(null);
        if(planAccion == null)
            throw new SolicitudEnsayoErrorException(String.format("Plan de acción no encontrado %s", dto.getId()));
        planAccion.marcarComoCumplida(dto.getCumplido());
        LOG.info(String.format("Plan de accion marcado cumplimiento %s", planAccion));
        return this.repo.save(planAccion);
    }

    @Transactional(readOnly = true)
    public List<SolicitudPlanAccion> listarPorTipoSolicitudYSolicitudId(TipoSolicitud tipo, long solicitudId){
        return this.repo.findByTipoSolicitudAndSolicitudIdOrderByFechaRegistro(tipo, solicitudId);
    }

    public boolean eliminar(long id){
        this.repo.deleteById(id);
        LOG.info(String.format("Plan acción id= %s eliminado", id));
        return true;
    }
}
