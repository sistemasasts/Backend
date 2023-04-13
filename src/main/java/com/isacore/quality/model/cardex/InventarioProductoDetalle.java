package com.isacore.quality.model.cardex;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class InventarioProductoDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    @NotNull
    private String responsable;
    @NotNull
    private String responsableNombreCompleto;
    @NotNull
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal cantidad;
    private LocalDate fechaEnsayo;
    private BigDecimal numeroEnsayo;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal stockActual;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    private InventarioProducto inventarioProducto;

    public InventarioProductoDetalle(String responsable, String responsableNombreCompleto, BigDecimal cantidad,
                                     LocalDate fechaEnsayo, BigDecimal numeroEnsayo, BigDecimal stockActual,
                                     TipoMovimiento tipoMovimiento, InventarioProducto inventarioProducto) {
        this.responsable = responsable;
        this.responsableNombreCompleto = responsableNombreCompleto;
        this.cantidad = cantidad;
        this.fechaEnsayo = fechaEnsayo;
        this.numeroEnsayo = numeroEnsayo;
        this.stockActual = stockActual;
        this.tipoMovimiento = tipoMovimiento;
        this.inventarioProducto = inventarioProducto;
        this.fechaRegistro = LocalDateTime.now();
    }
}
