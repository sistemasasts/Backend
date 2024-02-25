package com.isacore.quality.model.pnc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PncSalidaMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaCreacion;
    private LocalDate fecha;
    @Column(columnDefinition = "decimal(19,2)")
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

    @ManyToOne(fetch = FetchType.EAGER)
    private PncDefecto pncDefecto;

    @Column(columnDefinition = "varchar(max)")
    private String observacion;

    @NotNull
    private String usuario;
    private String usuarioAprobador;

    private LocalDateTime fechaAprobacion;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "pnc_salida_material_id", nullable = false)
    private List<PncSalidaMaterialInfoAdd> informacionAdicional = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private PncSalidaConcesion salidaConcesion;

    @Transient
    private boolean verPlanesAccion;

    public PncSalidaMaterial(LocalDate fecha, BigDecimal cantidad, TipoDestino destino,
                             ProductoNoConforme productoNoConforme, String observacion, String usuario, PncDefecto defecto) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.destino = destino;
        this.productoNoConforme = productoNoConforme;
        this.observacion = observacion;
        this.usuario = usuario;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoSalidaMaterial.CREADO;
        this.usuario = usuario;
        this.pncDefecto = defecto;
    }

    public void marcarComoEnviada(String usuario) {
        this.estado = EstadoSalidaMaterial.PENDIENTE_APROBACION;
        this.usuarioAprobador = usuario;
    }

    public void marcarComoCerrada() {
        this.estado = EstadoSalidaMaterial.CERRADO;
    }

    public void marcarComoAprobada() {
        this.estado = EstadoSalidaMaterial.APROBADO;
    }

    public void marcarComoNoAprobada() {
        this.estado = EstadoSalidaMaterial.RECHAZADO;
    }


    public boolean tieneCantidadDisponible() {
        return (this.getPncDefecto().getSaldo().subtract(this.getCantidad())).compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean verPlanesAccion(){
        return this.destino.isPlanesAccion();
    }

    public String cantidadConUnidad(){
        return String.format("%s %s", this.cantidad, this.productoNoConforme.getUnidad().getAbreviatura());
    }

    public int getGrupoAdicional(){
        return this.destino.getGrupoInfoAdicional();
    }

    public void agregarInfoAdd(PncSalidaMaterialInfoAdd info){
        this.informacionAdicional.add(info);
    }

    @Override
    public String toString() {
        return "PncSalidaMaterial{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                ", fecha=" + fecha +
                ", cantidad=" + cantidad +
                ", destino=" + destino +
                ", estado=" + estado +
                ", productoNoConforme=" + productoNoConforme.getNumero() +
                ", pncDefectoId=" + pncDefecto.getId() +
                ", observacion='" + observacion + '\'' +
                ", usuario='" + usuario + '\'' +
                ", usuarioAprobador='" + usuarioAprobador + '\'' +
                '}';
    }
}
