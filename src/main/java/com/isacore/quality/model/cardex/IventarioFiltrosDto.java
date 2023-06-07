package com.isacore.quality.model.cardex;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class IventarioFiltrosDto implements Serializable {
    private long inventarioId;
    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;
    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;
}
