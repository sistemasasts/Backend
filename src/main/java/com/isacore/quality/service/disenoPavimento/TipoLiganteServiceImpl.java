package com.isacore.quality.service.disenoPavimento;

import com.isacore.quality.exception.ConfiguracionErrorException;
import com.isacore.quality.mapper.disenoPavimento.TipoLiganteMapper;
import com.isacore.quality.model.disenoPavimento.TipoLigante;
import com.isacore.quality.model.disenoPavimento.TipoLiganteDto;
import com.isacore.quality.repository.disenoPavimento.TipoLiganteRepo;
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
public class TipoLiganteServiceImpl {

    private final TipoLiganteRepo defectodRepo;
    private final TipoLiganteMapper mapper;

    public List<TipoLiganteDto> listar() {
        return this.mapper.fromListToDto(this.defectodRepo.findAll(Sort.by(Sort.Direction.ASC, "Nombre")));
    }

    @Transactional
    public TipoLiganteDto registrar(TipoLiganteDto obj) {
        TipoLigante TipoLigante = new TipoLigante(obj.getNombre());
        this.defectodRepo.save(TipoLigante);
        log.info(String.format("TipoLigante registrado %s", TipoLigante));
        return this.mapper.fromTipoDisenoToDto(TipoLigante);
    }

    public TipoLigante findById(TipoLigante id) {
        return null;
    }

    @Transactional
    public TipoLiganteDto actualizar(TipoLiganteDto obj) {
        Optional<TipoLigante> TipoLigante = this.defectodRepo.findById(obj.getId());
        if (!TipoLigante.isPresent())
            throw new ConfiguracionErrorException("Tipo Ligante no encontrado");

        TipoLigante.get().setNombre(obj.getNombre());
        TipoLigante.get().setActivo(obj.isActivo());
        log.info(String.format("TipoLigante Diseno Pavimentos actualizado %s", TipoLigante.get()));
        return this.mapper.fromTipoDisenoToDto(TipoLigante.get());
    }


    @Transactional(readOnly = true)
    public List<TipoLiganteDto> listarActivos() {
        return this.mapper.fromListToDto(this.defectodRepo.findByActivoTrueOrderByNombreAsc());
    }

}
