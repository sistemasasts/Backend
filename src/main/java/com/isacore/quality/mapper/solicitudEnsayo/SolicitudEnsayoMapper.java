package com.isacore.quality.mapper.solicitudEnsayo;

import com.isacore.quality.model.se.SolicitudDTO;
import com.isacore.quality.model.se.SolicitudEnsayo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {SolicitudEnsayo.class})
public interface SolicitudEnsayoMapper {

    @Mapping(target = "tipoSolicitud", ignore = true)
    SolicitudDTO fromEntityToDto(SolicitudEnsayo solicitudEnsayo);

    List<SolicitudDTO> fromListToDto(List<SolicitudEnsayo> solicitudEnsayos);
}
