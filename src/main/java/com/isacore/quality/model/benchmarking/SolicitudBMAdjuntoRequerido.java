package com.isacore.quality.model.benchmarking;

import com.isacore.EntidadBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudBMAdjuntoRequerido extends EntidadBase {

    private String nombre;
    private int secuencia;
    private boolean obligatorio;
    private Long documentoId;

    public SolicitudBMAdjuntoRequerido(String nombre, int secuencia, boolean obligatorio) {
        this.nombre = nombre;
        this.secuencia = secuencia;
        this.obligatorio = obligatorio;
    }
}
