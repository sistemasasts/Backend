package com.isacore.quality.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class HccdResultadoDto implements Serializable {
    private long id;
    private String rutaArchivo;
    private String mensaje;

    public HccdResultadoDto(){ }

    public HccdResultadoDto(long id, String rutaArchivo) {
        this.id = id;
        this.rutaArchivo = rutaArchivo;
    }
}
