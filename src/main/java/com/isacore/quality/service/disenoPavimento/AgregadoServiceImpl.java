package com.isacore.quality.service.disenoPavimento;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.mapper.disenoPavimento.AgregadoMapper;
import com.isacore.quality.model.disenoPavimento.Agregado;
import com.isacore.quality.model.disenoPavimento.AgregadoDto;
import com.isacore.quality.repository.disenoPavimento.AgregadoRepo;
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
public class AgregadoServiceImpl {

    private final AgregadoRepo defectodRepo;
    private final AgregadoMapper mapper;

    public List<AgregadoDto> listar() {
        return this.mapper.fromListToDto(this.defectodRepo.findAll(Sort.by(Sort.Direction.ASC, "Nombre")));
    }

    @Transactional
    public AgregadoDto registrar(AgregadoDto obj) {
        Agregado Agregado = new Agregado(obj.getNombre());
        this.defectodRepo.save(Agregado);
        log.info(String.format("Agregado registrado %s", Agregado));
        return this.mapper.fromEntityToDto(Agregado);
    }

    public Agregado findById(Agregado id) {
        return null;
    }

    @Transactional
    public AgregadoDto actualizar(AgregadoDto obj) {
        Optional<Agregado> Agregado = this.defectodRepo.findById(obj.getId());
        if (!Agregado.isPresent())
            throw new ConfiguracionErrorException("Agregado no encontrado");

        Agregado.get().setNombre(obj.getNombre());
        Agregado.get().setActivo(obj.isActivo());
        log.info(String.format("Agregado Diseno Pavimentos actualizado %s", Agregado.get()));
        return this.mapper.fromEntityToDto(Agregado.get());
    }


    @Transactional(readOnly = true)
    public List<AgregadoDto> listarActivos() {
        return this.mapper.fromListToDto(this.defectodRepo.findByActivoTrueOrderByNombreAsc());
    }

}
