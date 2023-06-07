package com.isacore.quality.model.desviacionRequisito;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RecursoRecuperarMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long materialId;
    private String descripcion;
    private BigDecimal cantidad;
    private BigDecimal costo;
    private boolean esMaterial;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desviacion_requisito_id", nullable = false)
    private DesviacionRequisito desviacionRequisito;

    public RecursoRecuperarMaterial(long materialId, String descripcion, BigDecimal cantidad, BigDecimal costo,
                                    DesviacionRequisito desviacionRequisito) {
        this.materialId = materialId;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.costo = costo;
        this.desviacionRequisito = desviacionRequisito;
        if (materialId > 0)
            this.esMaterial = true;
    }
}
