package com.isacore.security.model;

import com.isacore.EntidadBase;
import com.isacore.quality.model.Area;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Usuario extends EntidadBase {

    @Column(nullable = false, unique = true, length = 100)
    private String nombreUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String numeroIdentificacion;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private UsuarioEstadoEnum estado;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID", nullable = false)
    private Area area;

    private String trabajo;

    public Usuario() {
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
        return "Usuario{" +
                "id='" + getId() + '\'' +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", numeroIdentificacion='" + numeroIdentificacion + '\'' +
                ", email='" + email + '\'' +
                ", trabajo=" + trabajo +
                ", estado=" + estado +
                ", area=" + area +
                '}';
    }
}
