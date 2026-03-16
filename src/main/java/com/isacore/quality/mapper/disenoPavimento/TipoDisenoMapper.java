package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.TipoDiseno;
import com.isacore.quality.model.disenoPavimento.TipoDisenoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {TipoDiseno.class})
public interface TipoDisenoMapper {

    TipoDisenoDto fromTipoDisenoToDto(TipoDiseno tipoDiseno);

    List<TipoDisenoDto> fromListToDto(List<TipoDiseno> tipoDiseno);
}
