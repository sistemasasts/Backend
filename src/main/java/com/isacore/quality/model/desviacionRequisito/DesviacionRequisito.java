package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DesviacionRequisito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCreacion;
    private String seguimiento;
    private String afectacion;
    private String motivo;
    private String descripcion;
    private String control;
    private String alcance;
    private String responsable;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product = new Product();

    public DesviacionRequisito(
            Product product,
            String seguimiento,
            String afectacion,
            String motivo,
            String descripcion,
            String control,
            String alcance,
            String responsable) {
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
