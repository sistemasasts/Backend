package com.isacore.security.mapper;

import com.isacore.security.dto.MenuDTO;
import com.isacore.security.model.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {Menu.class})
public interface MenuMapper {

    //@Mapping(target = "menus", source = "menus")
    MenuDTO toResponseDTO(Menu menu);

    List<MenuDTO> toResponseDTOList(List<Menu> menus);
}
