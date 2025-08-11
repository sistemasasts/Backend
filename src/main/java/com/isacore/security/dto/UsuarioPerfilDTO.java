package com.isacore.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsuarioPerfilDTO {
    private Long id;
    private long usuarioId;
    private long perfilId;
    private boolean activo;
}
