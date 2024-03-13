package com.isacore.quality.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DocumentoDto implements Serializable {

    private long id;
    private long solicitudId;
    private String orden;

    private String nombreArchivo;

    protected DocumentoDto(){}
}
