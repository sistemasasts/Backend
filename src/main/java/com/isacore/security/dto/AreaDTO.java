package com.isacore.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AreaDTO {

    private long idArea;
    private String nameArea;
    private boolean activo;
    private boolean activoPruebasProceso;
}
