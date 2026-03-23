package com.isacore.quality.model.disenoPavimento;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MinaDto {

    private long id;
    private String nombre;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String ubicacion;
    private String canton;
    private String provincia;
    private String codigoPostal;
    private String googlePlaceId;
    private String propietario;
    private String pais;
    private boolean activo;

}
