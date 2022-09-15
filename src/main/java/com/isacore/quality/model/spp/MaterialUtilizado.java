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
public class MaterialUtilizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    private String nombre;
    private BigDecimal cantidad;
    private String unidad;
    private String clasificacion;
    private String lote;

    public MaterialUtilizado(String nombre, BigDecimal cantidad, String unidad, String clasificacion, String lote) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.clasificacion = clasificacion;
        this.lote = lote;
        this.fechaRegistro = LocalDateTime.now();
    }
}
