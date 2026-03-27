package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.disenoPavimento.Mina;
import com.isacore.quality.model.disenoPavimento.MinaDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {Mina.class})
public interface MinaMapper {

    MinaDto fromTipoDisenoToDto(Mina mina);

    List<MinaDto> fromListToDto(List<Mina> minas);
}
