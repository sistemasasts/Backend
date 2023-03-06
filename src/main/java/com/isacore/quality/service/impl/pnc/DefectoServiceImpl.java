package com.isacore.quality.service.impl.pnc;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.model.pnc.Defecto;
import com.isacore.quality.repository.pnc.IDefectoRepo;
import com.isacore.quality.service.pnc.IDefectoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefectoServiceImpl implements IDefectoService {

    private final IDefectoRepo defectodRepo;

    @Override
    public List<Defecto> findAll() {
        return this.defectodRepo.findAll(Sort.by(Sort.Direction.ASC, "Nombre"));
    }

    @Override
    public Defecto create(Defecto obj) {
        Defecto defecto = new Defecto(obj.getNombre());
        this.defectodRepo.save(defecto);
        log.info(String.format("Defecto PNC registrado %s", defecto));
        return defecto;
    }

    @Override
    public Defecto findById(Defecto id) {
        return null;
    }

    @Transactional
    @Override
    public Defecto update(Defecto obj) {
        Optional<Defecto> defecto = this.defectodRepo.findById(obj.getId());
        if(!defecto.isPresent())
            throw new ConfiguracionErrorException("Unidad de medida no encontrada");

        defecto.get().setNombre(obj.getNombre());
        defecto.get().setActivo(obj.isActivo());
        log.info(String.format("Defecto PNC actualizado %s", defecto.get()));
        return defecto.get();
    }

    @Override
    public boolean delete(String id) {
        return false;
    }



    @Transactional(readOnly = true)
    @Override
    public List<Defecto> listarActivos() {
        return this.defectodRepo.findByActivoTrueOrderByNombreAsc();
    }

}
