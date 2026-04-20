package com.isacore.quality.model.disenoPavimento;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class TipoLigante extends EntidadBase {
    private String nombre;
    private boolean activo;

    protected TipoLigante() {
    }

    public TipoLigante(String nombre) {
        this.nombre = nombre;
        this.activo = true;
    }
}
