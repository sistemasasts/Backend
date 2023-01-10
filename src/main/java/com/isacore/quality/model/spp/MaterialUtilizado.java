package com.isacore.quality.model.spp;

import com.isacore.quality.model.UnidadMedida;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class MaterialUtilizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    private String nombre;
    private BigDecimal cantidadSolicitada = BigDecimal.ZERO;
    private BigDecimal cantidadUtilizada = BigDecimal.ZERO;
    //private String unidad;
    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unidad;
    private BigDecimal porcentajeVariacion = BigDecimal.ZERO;

    public MaterialUtilizado(String nombre, UnidadMedida unidad, BigDecimal cantidadSolicitada, BigDecimal cantidadUtilizada) {
        this.nombre = nombre;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadUtilizada = cantidadUtilizada;
        this.unidad = unidad;
        this.fechaRegistro = LocalDateTime.now();
        this.calcularVariacion();
    }

    public MaterialUtilizado(MaterialFormula material) {
        this.fechaRegistro = LocalDateTime.now();
        this.nombre = material.getNombre();
        this.cantidadSolicitada = material.getCantidad();
        this.unidad = material.getUnidad();
        this.calcularVariacion();
    }

    public void modificar(String nombre, UnidadMedida unidad, BigDecimal cantidadSolicitada, BigDecimal cantidadUtilizada) {
        this.nombre = nombre;
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadUtilizada = cantidadUtilizada;
        this.unidad = unidad;
        this.calcularVariacion();
    }

    private void calcularVariacion() {
        this.porcentajeVariacion = (this.cantidadSolicitada.subtract(this.cantidadUtilizada))
                .divide(this.cantidadSolicitada, 2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100));
    }

    public String getUnidadTexto() {
        return this.unidad != null ? this.unidad.getAbreviatura() : "";
    }
}
