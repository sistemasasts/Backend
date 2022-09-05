package com.isacore.quality.model.spp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.Area;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudBase;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;

import com.isacore.util.UtilidadesFecha;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    private String usuarioGestionPlanta;
    private String usuarioGestionCalidadJefe;
    private String usuarioGestionCalidad;
    private String usuarioGestionMantenimientoJefe;
    private String usuarioGestionMantenimiento;
    private LocalDate fechaPrueba;
    @Column(columnDefinition = "bit default 0")
    private boolean pruebaRealizada;

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
        setUsuarioAprobador(usuarioAsignado);
    }

    public void marcarSolicitudComoValidada(String usuarioAsignado) {
        setEstado(EstadoSolicitudPP.EN_PLANIFICACION);
        setUsuarioGestion(usuarioAsignado);
    }

    public void marcarSolicitudComoAsignadaPlanta(String usuarioAsignado, LocalDate fechaPruebas) {
        setEstado(EstadoSolicitudPP.EN_PROCESO_PRODUCCION);
        setUsuarioGestionPlanta(usuarioAsignado);
        setFechaPrueba(fechaPruebas);
    }

    public void marcarSolicitudComoAsignadaCalidad(String usuarioAsignado) {
        setUsuarioGestionCalidad(usuarioAsignado);
    }

    public void marcarSolicitudComoAsignadaMantenimiento(String usuarioAsignado) {
        setUsuarioGestionMantenimiento(usuarioAsignado);
    }

    public void marcarSolicitudComoRegresada() {
        setEstado(EstadoSolicitudPP.RECHAZADO);
    }

    public void responderPlanta(String usuarioAsignado){
        setEstado(EstadoSolicitudPP.EN_PROCESO_MANTENIMIENTO);
        setUsuarioGestionMantenimientoJefe(usuarioAsignado);
    }

    public void responderMantenimiento(String usuarioAsignado){
        setEstado(EstadoSolicitudPP.EN_PROCESO_CALIDAD);
        setUsuarioGestionCalidadJefe(usuarioAsignado);
    }

    public void responderCalidad(String usuarioAsignado){
        setEstado(EstadoSolicitudPP.PENDIENTE_APROBACION);
        setUsuarioAprobador(usuarioAsignado);
        setFechaAprobacion(LocalDate.now());
    }

    public void marcarComoPruebaNoEjecutada() {
        setEstado(EstadoSolicitudPP.PRUEBA_NO_EJECUTADA);
        setPruebaRealizada(false);
    }

    public void anular() {
        this.setFechaFinalizacion(LocalDateTime.now());
        this.setEstado(EstadoSolicitudPP.ANULADO);
    }

    public void guardarDatosImagen1(long id, String path){
        this.imagen1Id = id;
        this.imagen1Ruta = path;
    }

    public String getFechaSolicitud(){
        return UtilidadesFecha.formatear(getFechaCreacion(), "YYYY-MM-dd");
    }
}
