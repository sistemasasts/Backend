package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.Agregado;
import com.isacore.quality.model.disenoPavimento.AgregadoDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {Agregado.class})
public interface AgregadoMapper {

    AgregadoDto fromTipoDisenoToDto(Agregado agregado);

    List<AgregadoDto> fromListToDto(List<Agregado> agregados);
}
