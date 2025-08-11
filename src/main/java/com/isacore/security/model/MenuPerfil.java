package com.isacore.security.model;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@Getter
@Setter
public class MenuPerfil extends EntidadBase {

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Menu menu;

    @Column(name = "perfil_id", nullable = false)
    private Long perfilId;

    @ManyToOne
    @JoinColumn(name = "perfil_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Perfil perfil;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean activo = true;

    public MenuPerfil() {
    }

    public MenuPerfil(long id) {
        this.setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuPerfil that = (MenuPerfil) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + getId() +
                ", menuId=" + menuId +
                ", perfilId=" + perfilId +
                ", activo=" + activo +
                '}';
    }
}
