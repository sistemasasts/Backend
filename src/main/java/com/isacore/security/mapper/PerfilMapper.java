package com.isacore.security.mapper;

import com.isacore.security.dto.PerfilDTO;
import com.isacore.security.model.Perfil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {Perfil.class})
public interface PerfilMapper {

    PerfilDTO toDTO(Perfil perfil);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creadoFecha", ignore = true)
    @Mapping(target = "creadoPor", ignore = true)
    @Mapping(target = "modificadoPor", ignore = true)
    Perfil toEntity(PerfilDTO profileRequestDTO);

    List<PerfilDTO> toDTOList(List<Perfil> perfiles);

}
