package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PncDocumentoDto {
    private long id;
    private long productoNoConformeId;
    private Long salidaMaterialId;
    private PncOrdenFlujo orden;

    private EstadoSalidaMaterial estado;
    private String nombreArchivo;
    private String base64;

}
