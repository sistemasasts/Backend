package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.MinaAgregadoQuimica;
import com.isacore.quality.model.disenoPavimento.MinaAgregadoQuimicaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {AgregadoQuimicaMapper.class})
public interface MinaAgregadoQuimicaMapper {

    MinaAgregadoQuimicaDto fromEntityToDto(MinaAgregadoQuimica mina);

    @Mapping(target = "creadoFecha", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modificadoFecha", ignore = true)
    @Mapping(target = "modificadoPor", ignore = true)
    MinaAgregadoQuimica fromDtoToEntity(MinaAgregadoQuimicaDto mina);

    List<MinaAgregadoQuimicaDto> fromListToDto(List<MinaAgregadoQuimica> minas);
}
