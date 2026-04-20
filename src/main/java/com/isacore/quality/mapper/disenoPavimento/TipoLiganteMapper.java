package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.TipoLigante;
import com.isacore.quality.model.disenoPavimento.TipoLiganteDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {TipoLigante.class})
public interface TipoLiganteMapper {

    TipoLiganteDto fromTipoDisenoToDto(TipoLigante tipoDiseno);

    List<TipoLiganteDto> fromListToDto(List<TipoLigante> tipoDiseno);
}
