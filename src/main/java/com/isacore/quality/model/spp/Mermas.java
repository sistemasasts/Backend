package com.isacore.quality.model.spp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class Mermas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    private String material;
    private BigDecimal cantidadRendimiento;
    private String unidadRendimiento;
    private BigDecimal cantidadDesperdicio;
    private String unidadDesperdicio;

    public Mermas(String material, BigDecimal cantidadRendimiento, String unidadRendimiento, BigDecimal cantidadDesperdicio,
                  String unidadDesperdicio) {
        this.material = material;
        this.cantidadRendimiento = cantidadRendimiento;
        this.unidadRendimiento = unidadRendimiento;
        this.cantidadDesperdicio = cantidadDesperdicio;
        this.unidadDesperdicio = unidadDesperdicio;
        this.fechaRegistro = LocalDateTime.now();
    }
}
