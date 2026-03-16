package com.isacore.quality.model.disenoPavimento;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class TipoDiseno extends EntidadBase {
    private String nombre;
    private boolean activo;

    public TipoDiseno(String nombre) {
        this.nombre = nombre;
        this.activo = true;
    }
}
