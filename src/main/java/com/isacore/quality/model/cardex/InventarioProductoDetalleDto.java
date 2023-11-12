package com.isacore.quality.model.cardex;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InventarioProductoDetalleDto {
    private long id;
    private LocalDateTime fechaRegistro;

    private String responsable;
    private BigDecimal cantidad;
    private LocalDate fechaEnsayo;
    private String numeroEnsayo;
    private BigDecimal stockActual;

    private TipoMovimiento tipoMovimiento;
    private long inventarioProductoId;

    public LocalDate getFechaRegistro2() {
        return this.fechaRegistro.toLocalDate();
    }
}