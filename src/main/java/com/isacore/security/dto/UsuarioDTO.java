package com.isacore.security.dto;

import com.isacore.security.model.UsuarioEstadoEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsuarioDTO {

    private Long id;
    private String nombreUsuario;
    private String nombre;
    private String numeroIdentificacion;
    private String email;
    private UsuarioEstadoEnum estado;
    private AreaDTO area;
    private String trabajo;

}
