package com.isacore.quality.model.spp;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class AsignarResponsableDTO implements Serializable {

    private long id;
    private OrdenFlujoPP orden;
    private String usuario;
    private String observacion;
    private LocalDate fechaPruebas;
}
