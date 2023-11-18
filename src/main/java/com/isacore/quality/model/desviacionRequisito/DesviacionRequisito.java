package com.isacore.quality.model.desviacionRequisito;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.pnc.LineaAfecta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product = new Product();

    @NotNull
    private Long secuencial;

    @Transient
    private String afectacionText;
    @Transient
    private String productTypeText;

    @Column(columnDefinition = "bit default 0")
    private boolean replanificacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_afectado_id")
    private Product productoAfectado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_replanificado_id")
    private Product productoReplanificado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_afectada_id")
    private UnidadMedida unidadAfectada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_recuperada_id")
    private UnidadMedida unidadRecuperada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "unidad_desperdicio_id")
    private UnidadMedida unidadDesperdicio;

    private BigDecimal cantidadAfectada = BigDecimal.ZERO;

    private BigDecimal cantidadRecuperada = BigDecimal.ZERO;

    private BigDecimal desperdicioGenerado = BigDecimal.ZERO;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "desviacion_requisito_id", nullable = false)
    private List<DesviacionRequisitoDefecto> defectos;


    public DesviacionRequisito(
            Long secuencial,
            Product product,
            String seguimiento,
            LineaAfecta afectacion,
            String motivo,
            String descripcion,
            String control,
            String alcance,
            String responsable,
            boolean replanificacion) {
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
        this.replanificacion = replanificacion;
    }

    public void agregarDefecto(DesviacionRequisitoDefecto defecto) {
        this.defectos.add(defecto);
    }

    public void eliminarDefecto(long defectoId) {
        this.defectos.removeIf(x -> x.getId() == defectoId);
    }

    @Override
    public String toString() {
        return "DesviacionRequisito{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                ", seguimiento='" + seguimiento + '\'' +
                ", afectacion=" + afectacion +
                ", responsable='" + responsable + '\'' +
                ", secuencial=" + secuencial +
                ", afectacionText='" + afectacionText + '\'' +
                ", productTypeText='" + productTypeText + '\'' +
                ", producto='" + product.getNameProduct() + '\'' +
                '}';
    }
}
