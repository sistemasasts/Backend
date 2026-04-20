package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.AgregadoQuimica;
import com.isacore.quality.model.disenoPavimento.AgregadoQuimicaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AgregadoQuimicaMapper {

    AgregadoQuimicaDto fromEntityToDto(AgregadoQuimica agregado);

    @Mapping(target = "creadoFecha", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modificadoFecha", ignore = true)
    @Mapping(target = "modificadoPor", ignore = true)
    AgregadoQuimica fromDtoToEntity(AgregadoQuimicaDto agregado);

    List<AgregadoQuimicaDto> fromListToDto(List<AgregadoQuimica> agregados);
}
