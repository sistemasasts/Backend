package com.isacore.quality.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UnidadMedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String abreviatura;
    private String nombre;
    private boolean activo;

    public UnidadMedida(String abreviatura, String descripcion) {
        this.nombre = descripcion;
        this.abreviatura = abreviatura;
        this.activo = true;
    }
}
