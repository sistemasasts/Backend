package com.isacore.quality.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UnidadMedida {
    @Id
    private String Id;
    private String nombre;
    private boolean activo;

    @Transient
    private String idOriginal;

    public UnidadMedida(String id, String nombre) {
        Id = id;
        this.nombre = nombre;
        this.activo = true;
    }
}
