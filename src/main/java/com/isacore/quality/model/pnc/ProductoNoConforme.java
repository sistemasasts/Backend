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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductoNoConforme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private long numero;

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
    @Enumerated(EnumType.STRING)
    private EstadoPnc estado;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Area area;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Product producto;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "producto_no_conforme_id", nullable = false)
    List<PncDefecto> defectos;

    @Enumerated(EnumType.STRING)
    private ProcedenciaLinea procedenciaLinea;

    @Enumerated(EnumType.STRING)
    private LineaAfecta lineaAfecta;
    private String nombreCliente;

    @Enumerated(EnumType.STRING)
    private OrigenPnc origen;

    public ProductoNoConforme(long numero, String usuario, LocalDate fechaProduccion, LocalDate fechaDeteccion,
                              BigDecimal cantidadProducida, BigDecimal cantidadNoConforme, UnidadMedida unidad,
                              BigDecimal porcentajeValidez, BigDecimal pesoNoConforme, String ordenProduccion,
                              String lote, String hcc, String observacionCincoMs,
                              Area area, Product producto, ProcedenciaLinea procedenciaLinea, LineaAfecta lineaAfecta,
                              String nombreCliente, OrigenPnc origen) {
        this.numero = numero;
        this.usuario = usuario;
        this.fechaProduccion = fechaProduccion;
        this.fechaDeteccion = fechaDeteccion;
        this.cantidadProducida = cantidadProducida;
        this.cantidadNoConforme = cantidadNoConforme;
        this.unidad = unidad;
        this.porcentajeValidez = porcentajeValidez;
        this.pesoNoConforme = pesoNoConforme;
        this.ordenProduccion = ordenProduccion;
        this.lote = lote;
        this.hcc = hcc;
        this.observacionCincoMs = observacionCincoMs;
        this.area = area;
        this.producto = producto;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaModificacion = LocalDateTime.now();
        this.saldo = this.cantidadNoConforme;
        this.estado = EstadoPnc.CREADO;
        this.procedenciaLinea = procedenciaLinea;
        this.lineaAfecta = lineaAfecta;
        this.nombreCliente = nombreCliente;
        this.origen = origen;
    }

    public void agregarDefecto(PncDefecto defecto) {
        this.defectos.add(defecto);
    }

    public void eliminarDefecto(long defectoId) {
        this.defectos.removeIf(x -> x.getId() == defectoId);
    }

    public void reducirStock(BigDecimal cantidad) {
        this.setSaldo(this.saldo.subtract(cantidad));
    }

    public void cambiarEnProceso() {
        if (estado.equals(EstadoPnc.CREADO))
            this.estado = EstadoPnc.EN_PROCESO;
    }

    public void cambiarAnulado() {
        this.estado = EstadoPnc.ANULADO;
    }

    public void cambiarFinalizado() {
        this.estado = EstadoPnc.FINALIZADO;
    }

    public void agregarIdDocumentoADefecto(long defecto, long documento) {
        this.defectos.forEach(x -> {
            if (x.getId().compareTo(defecto) == 0)
                x.setIdImagen(documento);
        });
    }

    public String ubicacion() {
        return this.defectos.isEmpty() ? "" : this.defectos.stream().findFirst().get().getUbicacion();
    }

    public BigDecimal validez() {
        return this.defectos.isEmpty() ? BigDecimal.ZERO : this.defectos.stream().findFirst().get().getValidez();
    }


    @Override
    public String toString() {
        return "ProductoNoConforme{" +
                "id=" + id +
                ", numero=" + numero +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaModificacion=" + fechaModificacion +
                ", usuario='" + usuario + '\'' +
                ", fechaProduccion=" + fechaProduccion +
                ", fechaDeteccion=" + fechaDeteccion +
                ", cantidadProducida=" + cantidadProducida +
                ", cantidadNoConforme=" + cantidadNoConforme +
                ", saldo=" + saldo +
                ", unidad=" + unidad +
                ", porcentajeValidez=" + porcentajeValidez +
                ", pesoNoConforme=" + pesoNoConforme +
                ", produccionTotalMes=" + produccionTotalMes +
                ", ventaTotalMes=" + ventaTotalMes +
                ", ordenProduccion='" + ordenProduccion + '\'' +
                ", lote='" + lote + '\'' +
                ", hcc='" + hcc + '\'' +
                ", observacionCincoMs='" + observacionCincoMs + '\'' +
                ", estado=" + estado +
                ", area=" + area +
                ", defectos=" + defectos +
                ", procedenciaLinea=" + procedenciaLinea +
                ", lineaAfecta=" + lineaAfecta +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", producto=" + producto.getIdProduct() + producto.getNameProduct() +
                '}';
    }
}
