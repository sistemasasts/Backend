package com.isacore.quality.model.spp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudPruebaProcesoInforme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    private SolicitudPruebasProceso solicitudPruebasProceso;

    private LocalDate fechaPrueba;
    private BigDecimal cantidadProducida;
    private String lote;
    private String producto;
    private String ordenFabricacion;
    private String lineaFabricacion;
    private String lineaFabricacionUnidad;

    private BigDecimal cantidadProductoTerminado;
    private BigDecimal cantidadProductoNoConforme;
    private BigDecimal cantidadDesperdicio;
    private BigDecimal cantidadProductoPrueba;

    @Column(columnDefinition = "varchar(max)")
    private String conclucion;

    @Column(columnDefinition = "varchar(max)")
    private String observacionMantenimiento;
    @Column(columnDefinition = "varchar(max)")
    private String observacionCalidad;
    @Column(columnDefinition = "varchar(max)")
    private String observacionProduccion;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_prueba_proceso_informe_id")
    private List<MaterialUtilizado> materialesUtilizado = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_prueba_proceso_informe_id")
    private List<CondicionOperacion> condicionesOperacion = new ArrayList<>();

    public SolicitudPruebaProcesoInforme(SolicitudPruebasProceso solicitud) {
        this.solicitudPruebasProceso = solicitud;
        this.fechaPrueba = solicitud.getFechaPrueba();
        this.fechaRegistro = LocalDateTime.now();
        this.cargarMatrizMaterialesUtilizados(solicitud.getMaterialesFormula());
    }

    public void agregarMaterialUtilizado(MaterialUtilizado materialUtilizado) {
        this.materialesUtilizado.add(materialUtilizado);
    }

    public void agregarCondicionOperacion(CondicionOperacion condicionOperacion) {
        this.condicionesOperacion.add(condicionOperacion);
    }

    public void eliminarMaterialUtilizado(long materialId) {
        this.materialesUtilizado.removeIf(x -> x.getId() == materialId);
    }

    public void eliminarOperacionCondicion(long operacionCondicionId) {
        this.condicionesOperacion.removeIf(x -> x.getId() == operacionCondicionId);
    }

    private void cargarMatrizMaterialesUtilizados(List<MaterialFormula> materiales) {
        materiales.forEach(x -> {
            this.materialesUtilizado.add(new MaterialUtilizado(x));
        });
    }

    public List<CondicionOperacion> getCondicionesProduccion() {
        return this.condicionesOperacion.stream().filter(x -> x.getTipo().equals(CondicionOperacionTipo.PRODUCCION)).collect(Collectors.toList());
    }

    public List<CondicionOperacion> getCondicionesMantenimiento() {
        return this.condicionesOperacion.stream().filter(x -> x.getTipo().equals(CondicionOperacionTipo.MANTENIMIENTO)).collect(Collectors.toList());
    }

    public String getCodigo() {
        return this.solicitudPruebasProceso.getCodigo();
    }

    public String getTipoAprobacion() {
        if (this.getSolicitudPruebasProceso().getTipoAprobacion() == null)
            return "";
        return String.format("%s - %s", this.getSolicitudPruebasProceso().getTipoAprobacion().isAprobado() ? "APROBADO" : "NO APROBADO", this.getSolicitudPruebasProceso().getTipoAprobacion().getDescripcion());
    }
}
