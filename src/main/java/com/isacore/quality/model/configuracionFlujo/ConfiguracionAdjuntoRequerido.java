package com.isacore.quality.model.configuracionFlujo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class ConfiguracionAdjuntoRequerido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private int secuencia;
    private boolean obligatorio;

    public ConfiguracionAdjuntoRequerido(String nombre, int secuencia, boolean obligatorio) {
        this.nombre = nombre;
        this.secuencia = secuencia;
        this.obligatorio = obligatorio;
    }
}
