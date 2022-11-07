package com.isacore.quality.model.spp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class Condicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;
    private String maquinaria;
    private String nombre;
    private BigDecimal valor;
    private String unidad;

    @Transient
    private long condicionOperacionId;

    @Transient
    private CondicionOperacionTipo tipo;

    public Condicion(String maquinaria, String nombre, BigDecimal valor, String unidad) {
        this.maquinaria = maquinaria;
        this.nombre = nombre;
        this.valor = valor;
        this.unidad = unidad;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void actualizar(String maquinaria, String nombre, BigDecimal valor, String unidad){
        this.maquinaria = maquinaria;
        this.nombre = nombre;
        this.valor = valor;
        this.unidad = unidad;
    }
}
