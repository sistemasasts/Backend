package com.isacore.security.mapper;

import com.isacore.quality.model.Area;
import com.isacore.security.dto.AreaDTO;
import com.isacore.security.dto.PerfilDTO;
import com.isacore.security.model.Perfil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AreaMapper {

    AreaDTO toDTO(Area area);

    Area toEntity(AreaDTO areaDTO);

    List<AreaDTO> toDTOList(List<Area> areas);

}
