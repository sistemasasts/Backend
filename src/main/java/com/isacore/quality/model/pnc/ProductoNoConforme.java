package com.isacore.quality.model.pnc;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.Area;
import com.isacore.quality.model.Product;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductoNoConforme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    @NotNull
    private String usuario;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaProduccion;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaDeteccion;

    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal cantidadProducida;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal cantidadNoConforme;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unidad;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal porcentajeValidez;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal pesoNoConforme;

    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal produccionTotalMes;
    @Column(columnDefinition = "decimal(19,2)")
    private BigDecimal ventaTotalMes;

    private String ordenProduccion;
    private String lote;
    private String hcc;
    private String observacionCincoMs;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Area area;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Product producto;


}
