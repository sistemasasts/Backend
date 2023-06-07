package com.isacore.quality.service.impl.desviacionRequisito;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.RecursoRecuperarMaterial;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoRepo;
import com.isacore.quality.repository.desviacionRequisito.IRecursoRecuperarMaterialRepo;
import com.isacore.quality.service.desviacionRequisito.IRecursoRecuperarMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecursoRecuperarMaterialServiceImpl implements IRecursoRecuperarMaterialService {
    private final IRecursoRecuperarMaterialRepo recursoRecuperarMaterialRepo;
    private final IDesviacionRequisitoRepo desviacionRequisitoRepo;

    @Override
    public List<RecursoRecuperarMaterial> listarPorDesviacionRequisitoId(Long desviacionRequisitoId) {
        List<RecursoRecuperarMaterial> listaLotes = recursoRecuperarMaterialRepo
                .findByDesviacionRequisito_Id(desviacionRequisitoId);
        return listaLotes;
    }

    @Override
    public boolean eliminarPorId(Long id) {
        this.recursoRecuperarMaterialRepo.deleteById(id);
        return true;
    }

    @Override
    public List<RecursoRecuperarMaterial> findAll() {
        return null;
    }

    @Transactional
    @Override
    public RecursoRecuperarMaterial create(RecursoRecuperarMaterial dto) {
        Optional<DesviacionRequisito> desviacionRequisito = this.desviacionRequisitoRepo.findById(dto.getDesviacionRequisito().getId());

        if (!desviacionRequisito.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        RecursoRecuperarMaterial nuevoLote = new RecursoRecuperarMaterial(
                dto.getMaterialId(), dto.getDescripcion(), dto.getCantidad(), dto.getCosto(), desviacionRequisito.get());

        this.recursoRecuperarMaterialRepo.save(nuevoLote);
        log.info(String.format("Recurso registrado %s", nuevoLote));
        return nuevoLote;
    }

    @Override
    public RecursoRecuperarMaterial findById(RecursoRecuperarMaterial id) {
        return null;
    }

    @Transactional
    @Override
    public RecursoRecuperarMaterial update(RecursoRecuperarMaterial lote) {
        RecursoRecuperarMaterial recurso = this.recursoRecuperarMaterialRepo.findById(lote.getId())
                .orElse(null);
        if (recurso == null)
            throw new ConfiguracionErrorException("Recurso no encontrado");

        recurso.setCantidad(lote.getCantidad());
        recurso.setCosto(lote.getCosto());
        recurso.setDescripcion(lote.getDescripcion());
        recurso.setMaterialId(lote.getMaterialId());
        recurso.setEsMaterial(lote.getMaterialId() > 0);
        log.info(String.format("Lote actualizado %s", recurso));

        return recurso;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

}
