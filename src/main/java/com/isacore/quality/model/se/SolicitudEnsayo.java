package com.isacore.quality.model.se;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudEnsayo extends SolicitudBase {

    private String proveedorNombre;

    private Integer proveedorId;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaEntrega;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaEntregaValidacion;

    private String objetivo;

    @Enumerated(EnumType.STRING)
    private PrioridadNivel prioridad;

    @Enumerated(EnumType.STRING)
    private TiempoEntrega tiempoEntrega;

    @Enumerated(EnumType.STRING)
    private TipoAprobacionSolicitud tipoAprobacion;

    private String detalleMaterial;

    private String lineaAplicacion;

    private String uso;

    private BigDecimal cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unidad;

    private Boolean dataSheet;

    private Boolean msds;

    private String validador;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_ensayo_adjunto_requerido_id", nullable = false)
    private List<SolicitudEnsayoAdjuntoRequerido> adjuntosRequeridos = new ArrayList<>();

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate muestraEntrega;
    @Column(columnDefinition = "varchar(max)")
    private String muestraUbicacion;
    private Long muestraImagenId;
    private String muestraImagenRuta;
    private Long solicitudPruebaProcesoId;
    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate fechaEntregaInforme;

    private String nombreComercial;

    @Transient
    private String observacion;

    @Transient
    private OrdenFlujo orden;

    public SolicitudEnsayo(String codigo, String proveedorNombre, Integer proveedorId, LocalDate fechaEntrega, String objetivo,
                           PrioridadNivel prioridad, TiempoEntrega tiempoEntrega, String detalleMaterial, String lineaAplicacion, BigDecimal cantidad,
                           UnidadMedida unidad, String nombreSolicitante, LocalDate muestraEntrega, String muestraUbicacion, String nombreComercial, List<SolicitudEnsayoAdjuntoRequerido> adjuntos) {
        super(codigo, nombreSolicitante);
        this.proveedorNombre = proveedorNombre;
        this.proveedorId = proveedorId;
        this.fechaEntrega = fechaEntrega;
        this.objetivo = objetivo;
        this.prioridad = prioridad;
        this.tiempoEntrega = tiempoEntrega;
        this.detalleMaterial = detalleMaterial;
        this.lineaAplicacion = lineaAplicacion;
        this.cantidad = cantidad;
        this.unidad = unidad;
        this.estado = EstadoSolicitud.NUEVO;
        this.muestraEntrega = muestraEntrega;
        this.muestraUbicacion = muestraUbicacion;
        this.adjuntosRequeridos = adjuntos;
        this.nombreComercial = nombreComercial;
    }

    public void marcarSolicitudComoValidada(String usuarioAsignado, int tiempoRespuesta, LocalDate fechaInicioEntregaInforme) {
        setEstado(EstadoSolicitud.EN_PROCESO);
        setUsuarioGestion(usuarioAsignado);
        setTiempoRespuesta(tiempoRespuesta);
        this.fechaEntregaValidacion = LocalDate.now();
        setFechaEntregaInforme(this.calcularFechaLimiteDiasLaborables(fechaInicioEntregaInforme, tiempoRespuesta));
    }

    public void marcarSolicitudComoEnviada(String usuarioAsignado) {
        setEstado(EstadoSolicitud.ENVIADO_REVISION);
        this.validador = usuarioAsignado;
    }

    public void marcarSolicitudComoRespondida() {
        setEstado(EstadoSolicitud.REVISION_INFORME);
        setFechaRespuesta(LocalDate.now());
    }

    public void marcarSolicitudComoInformeAprobado(String usuarioAsignado, int tiempoAprobacion) {
        if (getEstado().equals(EstadoSolicitud.REVISION_INFORME)) {
            setTiempoAprobacion(tiempoAprobacion);
        }
        setEstado(EstadoSolicitud.PENDIENTE_APROBACION);
        setUsuarioAprobador(usuarioAsignado);
    }

    public void marcarSolicitudComoAprobada(TipoAprobacionSolicitud tipoAprobacion) {
        setEstado(EstadoSolicitud.FINALIZADO);
        this.tipoAprobacion = tipoAprobacion;
        finalizarSolicitud();
    }

    public void marcarSolicitudComoRegresada() {
        setEstado(EstadoSolicitud.REGRESADO_NOVEDAD_INFORME);
    }

    public int getVigencia() {
        if (this.fechaEntregaInforme != null) {
            if (getEstado().equals(EstadoSolicitud.EN_PROCESO) || getEstado().equals(EstadoSolicitud.REVISION_INFORME)) {
                LocalDate fechaLimite = this.fechaEntregaInforme.minusDays(getTiempoAprobacion());
                Duration diff = Duration.between(LocalDate.now().atStartOfDay(), fechaLimite.atStartOfDay());
                return (int) diff.toDays();
            }

            if (getEstado().equals(EstadoSolicitud.PENDIENTE_APROBACION)) {
                if (this.getFechaRespuesta() != null) {
                    LocalDate fechaLimite = this.getFechaRespuesta().plusDays(getTiempoAprobacion());
                    Duration diff = Duration.between(LocalDate.now().atStartOfDay(), fechaLimite.atStartOfDay());
                    return (int) diff.toDays();
                } else
                    return 0;
            }
        }
        return 0;
    }

//    public LocalDate getFechaEntregaInforme() {
//        if (fechaEntregaValidacion != null)
//            return this.fechaEntregaValidacion.plusDays(getTiempoRespuesta());
//        return null;
//    }

    public void anular() {
        this.setFechaFinalizacion(LocalDateTime.now());
        this.estado = EstadoSolicitud.ANULADO;
    }

    public void rechazar() {
        this.setFechaFinalizacion(LocalDateTime.now());
        this.estado = EstadoSolicitud.RECHAZADO;
    }

    public void marcarAdjuntoRequeridoComoCargado(long documentoId, long adjuntoRequeridoId) {
        Optional<SolicitudEnsayoAdjuntoRequerido> adjunto = this.adjuntosRequeridos.stream().filter(x -> x.getId() == adjuntoRequeridoId).findFirst();
        if (adjunto.isPresent()) {
            adjunto.get().setDocumentoId(documentoId);
        }
    }

    public void guardarDatosMuestraImagen(long id, String path) {
        this.muestraImagenId = id;
        this.muestraImagenRuta = path;
    }

    public boolean adjuntosRequeridosCompletos() {
        return this.adjuntosRequeridos.stream().filter(SolicitudEnsayoAdjuntoRequerido::isObligatorio).allMatch(x -> x.getDocumentoId() != null);
    }

    public void marcarAdjuntoRespaldoComoObligatorio(boolean obligatorio) {
        this.adjuntosRequeridos.stream().filter(x -> x.getNombre().equalsIgnoreCase("Respaldo")).findFirst().ifPresent(adjuntoRequerido -> adjuntoRequerido.setObligatorio(obligatorio));
    }

    public void marcarEstadoFinal(EstadoSolicitud estado, TipoAprobacionSolicitud tipoAprobacion) {
        setEstado(estado);
        setTipoAprobacion(tipoAprobacion);
    }

    private LocalDate calcularFechaLimiteDiasLaborables(LocalDate fechaInicio, int dias) {
        int agregarDias = 0;
        while (agregarDias < dias) {
            fechaInicio = fechaInicio.plusDays(1);
            if (!(fechaInicio.getDayOfWeek() == DayOfWeek.SATURDAY || fechaInicio.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++agregarDias;
            }
        }
        return fechaInicio;
    }

    public String getTipoAprobacionTexto() {
        if (this.tipoAprobacion != null)
            return this.tipoAprobacion.getDescripcion();
        return "";
    }
}
