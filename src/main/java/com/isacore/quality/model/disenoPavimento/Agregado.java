package com.isacore.quality.model.disenoPavimento;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Agregado extends EntidadBase {
    private String nombre;
    private boolean activo;

    protected Agregado() {
    }

    public Agregado(String nombre) {
        this.nombre = nombre;
        this.activo = true;
    }
}
