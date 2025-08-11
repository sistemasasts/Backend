package com.isacore.security.dto;

import com.isacore.security.model.Menu;
import com.isacore.security.model.MenuTipoEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
public class MenuDTO {

    private long id;
    private Integer padreId;
    private MenuTipoEnum tipo;
    private String etiqueta;
    private String descripcion;
    private Double ordenMenu = 0.0;
    private String icon;
    private String url;
    private boolean activo;
    private List<MenuDTO> menus;
}
