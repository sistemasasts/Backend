package com.isacore.quality.model.cardex;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.UnidadMedida;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class InventarioProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Product producto;

    @NotNull
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal minimo;
    @NotNull
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal maximo;
    @Column(columnDefinition = "decimal(19,2)")
    @NotNull
    private BigDecimal cantidadAlertar;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal stock = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unidad;

    public InventarioProducto(Product producto, BigDecimal minimo, BigDecimal maximo, BigDecimal cantidadAlertar,
                              UnidadMedida unidad) {
        this.producto = producto;
        this.minimo = minimo;
        this.maximo = maximo;
        this.cantidadAlertar = cantidadAlertar;
        this.unidad = unidad;
    }
}
