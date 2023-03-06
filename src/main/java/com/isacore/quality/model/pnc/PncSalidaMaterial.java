package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PncSalidaMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    @Column(columnDefinition = "decimal(19,5)")
    private BigDecimal cantidad;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDestino destino;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoSalidaMaterial estado;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductoNoConforme productoNoConforme;

    @Column(columnDefinition = "varchar(max)")
    private String observacion;
}
