package com.isacore.quality.service.disenoPavimento;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.mapper.disenoPavimento.TipoDisenoMapper;
import com.isacore.quality.model.disenoPavimento.TipoDiseno;
import com.isacore.quality.model.disenoPavimento.TipoDisenoDto;
import com.isacore.quality.repository.disenoPavimento.TipoDisenoRepo;
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
public class TipoDisenoServiceImpl {

    private final TipoDisenoRepo defectodRepo;
    private final TipoDisenoMapper mapper;

    public List<TipoDisenoDto> listar() {
        return this.mapper.fromListToDto(this.defectodRepo.findAll(Sort.by(Sort.Direction.ASC, "Nombre")));
    }

    @Transactional
    public TipoDisenoDto registrar(TipoDisenoDto obj) {
        TipoDiseno TipoDiseno = new TipoDiseno(obj.getNombre());
        this.defectodRepo.save(TipoDiseno);
        log.info(String.format("TipoDiseno registrado %s", TipoDiseno));
        return this.mapper.fromTipoDisenoToDto(TipoDiseno);
    }

    public TipoDiseno findById(TipoDiseno id) {
        return null;
    }

    @Transactional
    public TipoDisenoDto actualizar(TipoDisenoDto obj) {
        Optional<TipoDiseno> TipoDiseno = this.defectodRepo.findById(obj.getId());
        if (!TipoDiseno.isPresent())
            throw new ConfiguracionErrorException("Tipo diseno no encontrada");

        TipoDiseno.get().setNombre(obj.getNombre());
        TipoDiseno.get().setActivo(obj.isActivo());
        log.info(String.format("TipoDiseno Diseno Pavimentos actualizado %s", TipoDiseno.get()));
        return this.mapper.fromTipoDisenoToDto(TipoDiseno.get());
    }


    @Transactional(readOnly = true)
    public List<TipoDisenoDto> listarActivos() {
        return this.mapper.fromListToDto(this.defectodRepo.findByActivoTrueOrderByNombreAsc());
    }

}
