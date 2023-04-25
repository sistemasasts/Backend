package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.UnidadMedida;
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
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate fecha;
    @NotNull
    private String lote;
    @NotNull
    private BigDecimal cantidad;
    @NotNull
    @ManyToOne(cascade = CascadeType.DETACH ,fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_id", nullable = false)
    private UnidadMedida unidad;
    @ManyToOne(cascade = CascadeType.DETACH ,fetch = FetchType.EAGER)
    @JoinColumn(name = "desviacion_requisito_id", nullable = false)
    private DesviacionRequisito desviacionRequisito;
    @Transient
    private String unidadText;

}
