package com.isacore.security.dto;

import com.isacore.security.model.RolEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PerfilDTO {
    private Long id;
    private String nombre;
    private RolEnum rol;
    private boolean activo;
}
