package com.isacore.quality.service.impl;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.repository.IUnidadMedidadRepo;
import com.isacore.quality.service.IUnidadMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UnidadMedidaServiceImpl implements IUnidadMedidaService {

    private IUnidadMedidadRepo unidadMedidadRepo;

    @Autowired
    public UnidadMedidaServiceImpl(IUnidadMedidadRepo unidadMedidadRepo) {
        this.unidadMedidadRepo = unidadMedidadRepo;
    }

    @Override
    public List<UnidadMedida> findAll() {
        return this.unidadMedidadRepo.findAll();
    }

    @Override
    public UnidadMedida create(UnidadMedida obj) {
        this.aseguraraUnidadUnica(obj.getId());
        UnidadMedida unidad = new UnidadMedida(obj.getId(), obj.getNombre());
        return this.unidadMedidadRepo.save(unidad);
    }

    @Override
    public UnidadMedida findById(UnidadMedida id) {
        return null;
    }

    @Transactional
    @Override
    public UnidadMedida update(UnidadMedida obj) {
        Optional<UnidadMedida> unidadMedida = this.unidadMedidadRepo.findById(obj.getIdOriginal());
        if(!unidadMedida.isPresent())
            throw new ConfiguracionErrorException("Unidad de medida no encontrada");
        if(!unidadMedida.get().getId().equalsIgnoreCase(obj.getId())){
            this.aseguraraUnidadUnica(obj.getId());
            unidadMedida.get().setId(obj.getId());
        }
        unidadMedida.get().setNombre(obj.getNombre());
        unidadMedida.get().setActivo(obj.isActivo());
        return unidadMedida.get();
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    private void aseguraraUnidadUnica(String id){
        Optional<UnidadMedida> unidadMedida = this.unidadMedidadRepo.findById(id);
        if(unidadMedida.isPresent())
            throw new ConfiguracionErrorException("Unidad de medida ya registrada");
    }

    @Transactional(readOnly = true)
    @Override
    public List<UnidadMedida> listarActivos() {
        return this.unidadMedidadRepo.findByActivoTrue();
    }
}
