package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PncReporteComercialDto implements Serializable {
    private long id;
    private String nombreProducto;
    private BigDecimal cantidadExistente;
    private String unidad;
    private long numero;
    private String lote;
    private String ubicacion;
    private BigDecimal validez;
    private long defectoId;


}
