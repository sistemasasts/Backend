package com.isacore.quality.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PatronImgenDto implements Serializable {
    private Integer idProduct;
    private String nombreImagen;
    private String tipo;
    private String base64;

    public PatronImgenDto() {    }

    public PatronImgenDto(Integer idProduct, String nombreImagen, String tipo, String base64) {
        this.idProduct = idProduct;
        this.nombreImagen = nombreImagen;
        this.tipo = tipo;
        this.base64 = base64;
    }
}
