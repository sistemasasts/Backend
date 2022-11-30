package com.isacore.quality.dto;

import com.isacore.quality.model.se.OrdenFlujo;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdjuntoDto implements Serializable {
    private long solicitudId;
    private long adjuntoRequeridoId;
    private OrdenFlujo orden;
}
