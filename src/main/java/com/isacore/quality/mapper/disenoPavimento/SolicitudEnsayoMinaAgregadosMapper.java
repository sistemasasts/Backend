package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.se.SolicitudEnsayoMinaAgregados;
import com.isacore.quality.model.se.SolicitudEnsayoMinaAgregadosDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {AgregadoMapper.class})
public interface SolicitudEnsayoMinaAgregadosMapper {

    SolicitudEnsayoMinaAgregadosDto fromEntityToDto(SolicitudEnsayoMinaAgregados agregado);

    @Mapping(target = "creadoFecha", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modificadoFecha", ignore = true)
    @Mapping(target = "modificadoPor", ignore = true)
    SolicitudEnsayoMinaAgregados fromDtoToEntity(SolicitudEnsayoMinaAgregadosDto agregado);

    List<SolicitudEnsayoMinaAgregadosDto> fromListToDto(List<SolicitudEnsayoMinaAgregados> agregados);
}
