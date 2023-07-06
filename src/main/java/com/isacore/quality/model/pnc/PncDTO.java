package com.isacore.quality.model.pnc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class PncDTO implements Serializable {
    private long id;
    private long numero;
    private String usuario;
    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaProduccion;
    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaDeteccion;

    private EstadoPnc estado;
    private BigDecimal cantidadProducida;
    private BigDecimal cantidadNoConforme;
    private BigDecimal saldo;

    private UnidadMedida unidad;
    private BigDecimal porcentajeValidez;
    private BigDecimal pesoNoConforme;

    private BigDecimal produccionTotalMes;
    private BigDecimal ventaTotalMes;

    private String ordenProduccion;
    private String lote;
    private String hcc;
    private String observacionCincoMs;
    private String areaNombre;
    private String productoNombre;

    public String getEstadoTexto(){
        return this.estado.getDescripcion();
    }
}
