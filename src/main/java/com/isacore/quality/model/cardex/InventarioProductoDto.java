package com.isacore.quality.model.cardex;

import com.isacore.quality.model.UnidadMedida;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class InventarioProductoDto {
    private long id;
    private Integer productoId;
    private String productoNombre;

    private BigDecimal minimo;
    private BigDecimal maximo;
    private BigDecimal cantidadAlertar;
    private BigDecimal stock = BigDecimal.ZERO;
    private UnidadMedida unidad;
}
