package com.isacore.quality.model.pnc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConsultaPncDTO {

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicioDeteccion;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFinDeteccion;

    private Long productoId;
    private List<EstadoPnc> estados;
    private Long numero;
    private BigDecimal saldo;
    private String lote;

}
