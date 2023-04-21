package com.isacore.quality.service.impl.desviacionRequisito;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.model.desviacionRequisito.DesviacionRequisito;
import com.isacore.quality.model.desviacionRequisito.Lote;
import com.isacore.quality.repository.desviacionRequisito.IDesviacionRequisitoRepo;
import com.isacore.quality.repository.desviacionRequisito.ILoteRepo;
import com.isacore.quality.service.desviacionRequisito.ILoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoteServiceImpl implements ILoteService {
    private final ILoteRepo loteRepo;
    private final IDesviacionRequisitoRepo desviacionRequisitoRepo;

    @Transactional(readOnly = true)
    @Override
    public Lote listarLotePorId(Long id) {
        Optional<Lote> lote = this.loteRepo.findById(id);

        if (!lote.isPresent())
            throw new ConfiguracionErrorException("Lote no encontrado");

        return lote.get();
    }

    @Override
    public List<Lote> listarLotesPorDesviacionRequisitoId(Long desviacionRequisitoId) {
        Optional<DesviacionRequisito> desviacionRequisito = desviacionRequisitoRepo.findById(desviacionRequisitoId);

        if (!desviacionRequisito.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        List<Lote> listaLotes = loteRepo.findByDesviacionRequisito(desviacionRequisito.get());

        return listaLotes;
    }

    @Override
    public List<Lote> findAll() {
        return null;
    }

    @Override
    public Lote create(Lote lote) {
        Optional<DesviacionRequisito> desviacionRequisito = this.desviacionRequisitoRepo.findById(lote.getDesviacionRequisito().getId());

        if (!desviacionRequisito.isPresent())
            throw new ConfiguracionErrorException("Desviacion de requisito no encontrada");

        Lote nuevoLote = new Lote();
        nuevoLote.setLote(lote.getLote());
        nuevoLote.setFecha(lote.getFecha());
        nuevoLote.setUnidad(lote.getUnidad());
        nuevoLote.setCantidad(lote.getCantidad());
        nuevoLote.setDesviacionRequisito(lote.getDesviacionRequisito());

        this.loteRepo.save(nuevoLote);
        log.info(String.format("Lote registrado %s", nuevoLote));

        return nuevoLote;
    }

    @Override
    public Lote findById(Lote id) {
        return null;
    }

    @Transactional
    @Override
    public Lote update(Lote lote) {
        Optional<Lote> loteObtenido = this.loteRepo.findById(lote.getId());

        if (!loteObtenido.isPresent())
            throw new ConfiguracionErrorException("Lote no encontrada");

        loteObtenido.get().setLote(lote.getLote());
        loteObtenido.get().setFecha(lote.getFecha());
        loteObtenido.get().setUnidad(lote.getUnidad());
        loteObtenido.get().setCantidad(lote.getCantidad());
        loteObtenido.get().setDesviacionRequisito(lote.getDesviacionRequisito());

        log.info(String.format("Lote actualizado %s", loteObtenido));

        return loteObtenido.get();
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public boolean eliminarLotePorId(Long id) {

        this.loteRepo.deleteById(id);
        log.info(String.format("Lote eliminado %s", id));

        return true;
    }
}
