package com.isacore.quality.model.disenoPavimento;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

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

    protected Mina() {
    }

    public Mina(String nombre, BigDecimal latitud, BigDecimal longitud, String ubicacion, String canton,
                String provincia, String codigoPostal, String googlePlaceId, String propietario, String pais) {
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
        this.activo = true;
    }
}
