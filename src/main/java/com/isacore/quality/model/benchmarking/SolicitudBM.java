package com.isacore.quality.model.benchmarking;

import com.isacore.quality.model.Product;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.PrioridadNivel;
import com.isacore.quality.model.se.SolicitudBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudBM extends SolicitudBase {

    private String origenProducto;
    private String orientacionAnalisis;
    private String nombreComercialProducto;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unidad;

    private String presentacion;
    private BigDecimal presentacionPrecio;
    private String empresaFabricante;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product productoComparar;

    private String canalVenta;
    @Column(columnDefinition = "varchar(1028)")
    private String motivo;

    private String tipoMuestra;

    @Enumerated(EnumType.STRING)
    private PrioridadNivel prioridad;

    @Column(columnDefinition = "varchar(1028)")
    private String caracteristicasComparar;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_bm_adjunto_requerido_id", nullable = false)
    private List<SolicitudBMAdjuntoRequerido> adjuntosRequeridos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

}
