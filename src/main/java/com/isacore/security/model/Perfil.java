package com.isacore.security.model;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Perfil extends EntidadBase {
    @Column(nullable = false, length = 128)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private RolEnum rol;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean activo = true;

    public Perfil() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(getId(), usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Perfil{" +
                "id='" + getId() + '\'' +
                "nombre='" + nombre + '\'' +
                ", rol=" + rol +
                ", activo=" + activo +
                '}';
    }
}
