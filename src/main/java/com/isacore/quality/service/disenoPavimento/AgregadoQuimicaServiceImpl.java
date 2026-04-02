package com.isacore.quality.service.disenoPavimento;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.mapper.disenoPavimento.AgregadoQuimicaMapper;
import com.isacore.quality.model.disenoPavimento.AgregadoQuimica;
import com.isacore.quality.model.disenoPavimento.AgregadoQuimicaDto;
import com.isacore.quality.repository.disenoPavimento.AgregadoQuimicaRepo;
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
public class AgregadoQuimicaServiceImpl {

    private final AgregadoQuimicaRepo defectodRepo;
    private final AgregadoQuimicaMapper mapper;

    public List<AgregadoQuimicaDto> listar() {
        return this.mapper.fromListToDto(this.defectodRepo.findAll(Sort.by(Sort.Direction.ASC, "Nombre")));
    }

    @Transactional
    public AgregadoQuimicaDto registrar(AgregadoQuimicaDto obj) {
        AgregadoQuimica AgregadoQuimica = new AgregadoQuimica(obj.getNombre());
        this.defectodRepo.save(AgregadoQuimica);
        log.info(String.format("AgregadoGranulometria registrado %s", AgregadoQuimica));
        return this.mapper.fromEntityToDto(AgregadoQuimica);
    }

    public AgregadoQuimica findById(AgregadoQuimica id) {
        return null;
    }

    @Transactional
    public AgregadoQuimicaDto actualizar(AgregadoQuimicaDto obj) {
        Optional<AgregadoQuimica> AgregadoGranulometria = this.defectodRepo.findById(obj.getId());
        if (!AgregadoGranulometria.isPresent())
            throw new ConfiguracionErrorException("AgregadoGranulometria no encontrado");

        AgregadoGranulometria.get().setNombre(obj.getNombre());
        AgregadoGranulometria.get().setActivo(obj.isActivo());
        log.info(String.format("AgregadoGranulometria Diseno Pavimentos actualizado %s", AgregadoGranulometria.get()));
        return this.mapper.fromEntityToDto(AgregadoGranulometria.get());
    }


    @Transactional(readOnly = true)
    public List<AgregadoQuimicaDto> listarActivos() {
        return this.mapper.fromListToDto(this.defectodRepo.findByActivoTrueOrderByNombreAsc());
    }

}
