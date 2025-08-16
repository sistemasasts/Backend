package com.isacore.security.mapper;

import com.isacore.security.dto.UsuarioPerfilDTO;
import com.isacore.security.model.Perfil;
import com.isacore.security.model.UsuarioPerfil;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = {UsuarioPerfil.class, Perfil.class})
public interface UsuarioPerfilMapper {

    UsuarioPerfilDTO toDTO(UsuarioPerfil usuarioPerfil);

    List<UsuarioPerfilDTO> toDTOList(List<UsuarioPerfil> usuarioPerfiles);
}
