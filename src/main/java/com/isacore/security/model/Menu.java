package com.isacore.security.model;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Menu extends EntidadBase {

    @Column(name = "padre_id")
    private Long padreId;

    @ManyToOne
    @JoinColumn(name = "padre_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Menu padreMenu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private MenuTipoEnum tipo;

    @Column(nullable = false, length = 100)
    private String etiqueta;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Double ordenMenu = 0.0;

    @Column(length = 128)
    private String icon;

    private String url;

    @Column(nullable = false, columnDefinition = "bit default 1")
    private boolean activo = true;

    @Transient
    private List<Menu> menus;

}
