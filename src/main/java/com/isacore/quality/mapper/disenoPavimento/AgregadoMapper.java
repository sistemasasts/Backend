package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.Agregado;
import com.isacore.quality.model.disenoPavimento.AgregadoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AgregadoMapper {

    AgregadoDto fromEntityToDto(Agregado agregado);

    @Mapping(target = "creadoFecha", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modificadoFecha", ignore = true)
    @Mapping(target = "modificadoPor", ignore = true)
    Agregado fromDtoToEntity(AgregadoDto agregado);

    List<AgregadoDto> fromListToDto(List<Agregado> agregados);
}
