package com.isacore.quality.model.disenoPavimento;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Entity
public class Mina extends EntidadBase {

    private String nombre;
    @Column(precision = 20, scale = 16)
    private BigDecimal latitud;
    @Column(precision = 20, scale = 16)
    private BigDecimal longitud;
    private String ubicacion;
    private String canton;
    private String provincia;
    private String codigoPostal;
    private String googlePlaceId;
    private String propietario;
    private String pais;
    private boolean activo;
    @Column(columnDefinition = "bit default 0")
    private boolean tienePermisos;
    private String numeroPermiso;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "mina_id", nullable = false)
    private List<MinaAgregadoQuimica> agregadosQuimica = new ArrayList<>();

    protected Mina() {
    }

    public Mina(String nombre, BigDecimal latitud, BigDecimal longitud, String ubicacion, String canton,
                String provincia, String codigoPostal, String googlePlaceId, String propietario, String pais,
                boolean tienePermisos, String numeroPermiso, List<MinaAgregadoQuimica> agregados) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ubicacion = ubicacion;
        this.canton = canton;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
        this.googlePlaceId = googlePlaceId;
        this.propietario = propietario;
        this.pais = pais;
        this.tienePermisos = tienePermisos;
        this.numeroPermiso = numeroPermiso;
        this.agregadosQuimica = agregados;
        this.activo = true;
    }
}
