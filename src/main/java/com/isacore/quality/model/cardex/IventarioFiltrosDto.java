package com.isacore.quality.model.cardex;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class IventarioFiltrosDto implements Serializable {
    private long inventarioId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
