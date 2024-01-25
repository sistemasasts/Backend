package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.UnidadMedida;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_id", nullable = false)
    private UnidadMedida unidad;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desviacion_requisito_id", nullable = false)
    private DesviacionRequisito desviacionRequisito;
    @Transient
    private String unidadText;

    private BigDecimal costo;

    @Override
    public String toString() {
        return "Lote{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", lote='" + lote + '\'' +
                ", cantidad=" + cantidad +
                ", unidad=" + unidad +
                ", unidadText='" + unidadText + '\'' +
                ", Desviacion='{ secuencial = " + desviacionRequisito.getSecuencial() + '\'' +
                '}';
    }
}
