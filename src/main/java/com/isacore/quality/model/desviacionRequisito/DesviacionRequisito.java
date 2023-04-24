package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.pnc.LineaAfecta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DesviacionRequisito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime fechaCreacion;

    @Column(columnDefinition = "varchar(max)")
    private String seguimiento;

    @Column(columnDefinition = "varchar(max)")
    @Enumerated(EnumType.STRING)
    private LineaAfecta afectacion;

    @Column(columnDefinition = "varchar(max)")
    private String motivo;

    @Column(columnDefinition = "varchar(max)")
    private String descripcion;

    @Column(columnDefinition = "varchar(max)")
    private String control;

    @Column(columnDefinition = "varchar(max)")
    private String alcance;

    private String responsable;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product = new Product();

    @NotNull
    private Long secuencial;

    @Transient
    private String afectacionText;
    @Transient
    private String productTypeText;

    public DesviacionRequisito(
            Long secuencial,
            Product product,
            String seguimiento,
            LineaAfecta afectacion,
            String motivo,
            String descripcion,
            String control,
            String alcance,
            String responsable) {
        this.secuencial = secuencial;
        this.product = product;
        this.seguimiento = seguimiento;
        this.afectacion = afectacion;
        this.descripcion = descripcion;
        this.motivo = motivo;
        this.control = control;
        this.alcance = alcance;
        this.fechaCreacion = LocalDateTime.now();
        this.responsable = responsable;
    }
}
