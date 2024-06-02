package com.isacore.quality.model.desviacionRequisito;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.pnc.LineaAfecta;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConsultaDesviacionRequisitoDTO {
    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;

    @JsonSerialize(using = LocalDateTimeSerialize.class)
    @JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;

    private Long productoId;
    private LineaAfecta afectacion;
    private LineaNegocio lineaNegocio;
    private Long secuencial;
    private List<EstadoDesviacion> estados;
}
