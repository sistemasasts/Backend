package com.isacore.quality.model.spp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.Area;
import com.isacore.quality.model.se.SolicitudBase;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import com.isacore.util.UtilidadesFecha;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudPruebasProceso extends SolicitudBase {

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaEntrega;

    private String lineaAplicacion;

    private String motivo;

    private String motivoOtro;

    private String materialLineaProceso;

    private String materialLineaProcesoOtro;

    @Column(columnDefinition = "varchar(max)")
    private String descripcionProducto;

    @Column(columnDefinition = "varchar(max)")
    private String variablesProceso;

    @Column(columnDefinition = "varchar(max)")
    private String verificacionAdicional;

    @Column(columnDefinition = "varchar(max)")
    private String observacion;

    @Column(columnDefinition = "bit default 0")
    private boolean aprobado;

    @Enumerated(EnumType.STRING)
    private TipoAprobacionPP tipoAprobacion;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitudPP estado;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Area area;

    @Enumerated(EnumType.STRING)
    private OrigenSolicitudPP origen;

    private String imagen1Ruta;
    private Long imagen1Id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean requiereInforme;

    private String usuarioValidador;
    private String usuarioGestionPlanta;
    private String usuarioGestionCalidadJefe;
    private String usuarioGestionCalidad;
    private String usuarioGestionMantenimientoJefe;
    private String usuarioGestionMantenimiento;
    private LocalDate fechaPrueba;
    @Column(columnDefinition = "bit default 0")
    private boolean pruebaRealizada;
    private LocalDateTime fechaNotificacionPruebaRealizada;

    private LocalDate fechaEntregaInforme;

    @Column(columnDefinition = "varchar(max)")
    private String observacionMantenimiento;
    @Column(columnDefinition = "varchar(max)")
    private String observacionCalidad;
    @Column(columnDefinition = "varchar(max)")
    private String observacionProduccion;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_pruebas_proceso_id")
    private List<MaterialUtilizado> materiales;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_pruebas_proceso_id")
    private List<CondicionOperacion> condicionesOperacion;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_pruebas_proceso_id")
    private List<Mermas> mermas;

    @Transient
    private String observiacionFlujo;

    @Transient
    private OrdenFlujoPP orden;

    @Transient
    private String usuarioAsignado;

    public SolicitudPruebasProceso(
            String codigo, LocalDate fechaEntrega, String lineaAplicacion, String motivo, String motivoOtro,
            String materialLineaProceso, String materialLineaProOtro, String descripcionProducto,
            String variablesProceso, String verificacionAdicional, String observacion, String nombreSolicitante, Area area,
            OrigenSolicitudPP origen, boolean requiereInforme) {
        super(codigo, nombreSolicitante);
        this.fechaEntrega = fechaEntrega;
        this.lineaAplicacion = lineaAplicacion;
        this.motivo = motivo;
        this.motivoOtro = motivoOtro;
        this.materialLineaProceso = materialLineaProceso;
        this.materialLineaProcesoOtro = materialLineaProOtro;
        this.descripcionProducto = descripcionProducto;
        this.variablesProceso = variablesProceso;
        this.verificacionAdicional = verificacionAdicional;
        this.observacion = observacion;
        this.area = area;
        this.origen = origen;
        this.requiereInforme = requiereInforme;
        this.estado = EstadoSolicitudPP.NUEVO;
    }

    public void marcarSolicitudComoEnviada(String usuarioAsignado) {
        setEstado(EstadoSolicitudPP.ENVIADO_REVISION);
        setUsuarioValidador(usuarioAsignado);
    }

    public void marcarSolicitudComoValidada(String usuarioAsignado) {
        setEstado(EstadoSolicitudPP.EN_PROCESO);
        setUsuarioGestion(usuarioAsignado);
    }

    public void marcarSolicitudComoAsignadaPlanta(String usuarioAsignado, LocalDate fechaPruebas) {
        //setEstado(EstadoSolicitudPP.EN_PROCESO_PRODUCCION);
        setUsuarioGestionPlanta(usuarioAsignado);
        setFechaPrueba(fechaPruebas);
    }

    public void marcarSolicitudComoAsignadaCalidad(String usuarioAsignado) {
        setUsuarioGestionCalidad(usuarioAsignado);
    }

    public void marcarSolicitudComoAsignadaMantenimiento(String usuarioAsignado) {
        setUsuarioGestionMantenimiento(usuarioAsignado);
    }

    public void marcarComoProcesoFinalizado(String usuarioAprobador) {
        setEstado(EstadoSolicitudPP.PENDIENTE_APROBACION);
        setUsuarioAprobador(usuarioAprobador);
    }

    public void marcarComoAprobada(TipoAprobacionPP tipo) {
        setTipoAprobacion(tipo);
        setAprobado(tipo.isAprobado());
        setFechaAprobacion(LocalDate.now());
    }

    public void responderPlanta(String usuarioAsignado) {
        setEstado(EstadoSolicitudPP.EN_PROCESO_MANTENIMIENTO);
        setUsuarioGestionMantenimientoJefe(usuarioAsignado);
    }

    public void marcarComoPruebaNoEjecutada() {
        setEstado(EstadoSolicitudPP.PRUEBA_NO_EJECUTADA);
        setPruebaRealizada(false);
        setFechaNotificacionPruebaRealizada(LocalDateTime.now());
    }

    public void marcarComoPruebaEjecutada(int diasPlazo) {
        setPruebaRealizada(true);
        setFechaNotificacionPruebaRealizada(LocalDateTime.now());
        setTiempoRespuesta(diasPlazo);
        calcularFechaEntregaInforme(diasPlazo);
    }


    public void anular() {
        this.setFechaFinalizacion(LocalDateTime.now());
        this.setEstado(EstadoSolicitudPP.ANULADO);
    }

    public void guardarDatosImagen1(long id, String path) {
        this.imagen1Id = id;
        this.imagen1Ruta = path;
    }

    public String getFechaSolicitud() {
        return UtilidadesFecha.formatear(getFechaCreacion(), "YYYY-MM-dd");
    }

    public String getTipoAprobacionTexto() {
        return getTipoAprobacion() != null ? getTipoAprobacion().getDescripcion() : "";
    }

    public String getAprobadoTexto() {
        return getTipoAprobacion() != null ? isAprobado() ? "SI" : "NO" : "";
    }

    private void calcularFechaEntregaInforme(int dias) {
        LocalDate result = LocalDate.now();
        int agregarDias = 0;
        while (agregarDias < dias) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++agregarDias;
            }
        }
        setFechaEntregaInforme(result);
    }

    public int getVigencia() {
        Duration diff = Duration.between(LocalDate.now().atStartOfDay(), fechaEntregaInforme.atStartOfDay());
        return (int) diff.toDays();
    }
}
