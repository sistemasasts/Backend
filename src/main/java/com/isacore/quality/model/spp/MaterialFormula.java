package com.isacore.quality.model.spp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class MaterialFormula {
    public static final BigDecimal CIEN = new BigDecimal(100);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    private String nombre;
    private BigDecimal cantidad;
    private BigDecimal porcentaje;
    private String unidad;

    public MaterialFormula(String nombre, BigDecimal cantidadRequerida, BigDecimal porcentaje, String unidad) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.unidad = unidad;
        this.calcularCantidad(cantidadRequerida);
    }

    public void cambiarNombreYPorcentaje(String nombre, BigDecimal cantidadRequerida,BigDecimal porcentaje){
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.calcularCantidad(cantidadRequerida);
    }

    private void calcularCantidad(BigDecimal cantidadRequerida){
        this.cantidad = cantidadRequerida.multiply(this.porcentaje).divide(CIEN).setScale(2, RoundingMode.HALF_UP);
    }
}
