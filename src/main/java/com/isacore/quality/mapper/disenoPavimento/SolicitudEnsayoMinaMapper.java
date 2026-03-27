package com.isacore.quality.mapper.disenoPavimento;

import com.isacore.quality.model.se.SolicitudEnsayoMina;
import com.isacore.quality.model.se.SolicitudEnsayoMinaDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {SolicitudEnsayoMina.class})
public interface SolicitudEnsayoMinaMapper {

    SolicitudEnsayoMinaDto fromTipoDisenoToDto(SolicitudEnsayoMina mina);

    List<SolicitudEnsayoMinaDto> fromListToDto(List<SolicitudEnsayoMina> minas);
}
