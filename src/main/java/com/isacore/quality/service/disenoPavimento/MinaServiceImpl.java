package com.isacore.quality.service.disenoPavimento;

import com.isacore.quality.mapper.disenoPavimento.MinaAgregadoQuimicaMapper;
import com.isacore.quality.mapper.disenoPavimento.MinaMapper;
import com.isacore.quality.model.disenoPavimento.Mina;
import com.isacore.quality.model.disenoPavimento.MinaAgregadoQuimica;
import com.isacore.quality.model.disenoPavimento.MinaDto;
import com.isacore.quality.repository.disenoPavimento.MinaRepo;
import com.isacore.security.exception.RecursoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinaServiceImpl {

    private final MinaRepo minaRepo;
    private final MinaMapper mapper;
    private final MinaAgregadoQuimicaMapper agregadoGranulometriaMapper;

    public List<MinaDto> listar() {
        return this.mapper.fromListToDto(this.minaRepo.findAll(Sort.by(Sort.Direction.ASC, "Nombre")));
    }

    @Transactional
    public MinaDto registrar(MinaDto obj) {
        List<MinaAgregadoQuimica> agregados = obj.getAgregadosQuimica()
                .stream()
                .map(this.agregadoGranulometriaMapper::fromDtoToEntity)
                .collect(Collectors.toList());

        Mina Mina = new Mina(obj.getNombre(), obj.getLatitud(), obj.getLongitud(), obj.getUbicacion(), obj.getCanton(),
                obj.getProvincia(), obj.getCodigoPostal(), obj.getGooglePlaceId(), obj.getPropietario(), obj.getPais(),
                obj.isTienePermisos(), obj.getNumeroPermiso(), agregados);

        this.minaRepo.save(Mina);
        log.info(String.format("Mina registrado %s", Mina));
        return this.mapper.fromTipoDisenoToDto(Mina);
    }

    @Transactional(readOnly = true)
    public MinaDto obtenerPorId(long id) {
        Mina mina = this.buscarPorId(id);
        return mapper.fromTipoDisenoToDto(mina);
    }

    @Transactional
    public MinaDto actualizar(MinaDto obj) {
        Mina mina = this.buscarPorId(obj.getId());
        mina.setNombre(obj.getNombre());
        mina.setCanton(obj.getCanton());
        mina.setLatitud(obj.getLatitud());
        mina.setLongitud(obj.getLongitud());
        mina.setPropietario(obj.getPropietario());
        mina.setProvincia(obj.getProvincia());
        mina.setUbicacion(obj.getUbicacion());
        mina.setGooglePlaceId(obj.getGooglePlaceId());
        mina.setCodigoPostal(obj.getCodigoPostal());
        mina.setPais(obj.getPais());
        mina.setActivo(obj.isActivo());
        mina.setTienePermisos(obj.isTienePermisos());
        mina.setNumeroPermiso(obj.getNumeroPermiso());
        List<MinaAgregadoQuimica> agregados = obj.getAgregadosQuimica()
                .stream()
                .map(this.agregadoGranulometriaMapper::fromDtoToEntity)
                .collect(Collectors.toList());
        mina.getAgregadosQuimica().clear();
        mina.getAgregadosQuimica().addAll(agregados);
        log.info(String.format("Mina actualizado %s", mina));
        return this.mapper.fromTipoDisenoToDto(mina);
    }


    @Transactional(readOnly = true)
    public List<MinaDto> listarActivos() {
        return this.mapper.fromListToDto(this.minaRepo.findByActivoTrueOrderByNombreAsc());
    }

    private Mina buscarPorId(long id) {
        return this.minaRepo.findById(id).orElseThrow(() -> new RecursoNotFoundException("Mina con id= " + id + "no encontrado"));
    }

}
