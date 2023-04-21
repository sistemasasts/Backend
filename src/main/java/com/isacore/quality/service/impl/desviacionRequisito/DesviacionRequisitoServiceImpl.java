package com.isacore.quality.service.impl.desviacionRequisito;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoRepo;
import com.isacore.quality.service.desviacionRequisito.IDesviacionRequisitoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DesviacionRequisitoServiceImpl implements IDesviacionRequisitoService {
    private final IDesviacionRequisitoRepo desviacionRequisitoRepo;

    @Override
    public List<DesviacionRequisito> findAll() {
        return this.desviacionRequisitoRepo.findAll();
    }

    @Override
    public DesviacionRequisito create(DesviacionRequisito desviacionRequisito) {
        DesviacionRequisito nuevaDesviacionRequisito = new DesviacionRequisito(
                desviacionRequisito.getProduct(),
                desviacionRequisito.getSeguimiento(),
                desviacionRequisito.getAfectacion(),
                desviacionRequisito.getMotivo(),
                desviacionRequisito.getDescripcion(),
                desviacionRequisito.getControl(),
                desviacionRequisito.getAlcance(),
                desviacionRequisito.getResponsable()
        );

        this.desviacionRequisitoRepo.save(nuevaDesviacionRequisito);
        log.info(String.format("Desviacion deRequisito registrado %s", nuevaDesviacionRequisito));

        return nuevaDesviacionRequisito;
    }

    @Override
    public DesviacionRequisito findById(DesviacionRequisito id) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public DesviacionRequisito listarDesviacionRequisitosPorId(Long desviacionId) {
        Optional<DesviacionRequisito> desviacionRequisito = this.desviacionRequisitoRepo.findById(desviacionId);

        if(!desviacionRequisito.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        return desviacionRequisito.get();
    }

    @Transactional
    @Override
    public DesviacionRequisito update(DesviacionRequisito obj) {
        Optional<DesviacionRequisito> desviacionRequisito = this.desviacionRequisitoRepo.findById(obj.getId());
        if (!desviacionRequisito.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        desviacionRequisito.get().setProduct(obj.getProduct());
        desviacionRequisito.get().setSeguimiento(obj.getSeguimiento());
        desviacionRequisito.get().setAfectacion(obj.getAfectacion());
        desviacionRequisito.get().setMotivo(obj.getMotivo());
        desviacionRequisito.get().setDescripcion(obj.getDescripcion());
        desviacionRequisito.get().setControl(obj.getControl());
        desviacionRequisito.get().setAlcance(obj.getAlcance());
        desviacionRequisito.get().setResponsable(obj.getResponsable());

        log.info(String.format("Desviacion de requisito actualizado %s", desviacionRequisito));

        return desviacionRequisito.get();
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<DesviacionRequisito> listarDesviacionRequistoActivos() {
        return null;
    }
}
