package com.isacore.quality.model.se;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.UnidadMedida;
import com.isacore.quality.model.disenoPavimento.TipoDiseno;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
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
    private String tipoDiseno;
    private String tipoDisenoOtro;

    @JsonSerialize(using = LocalDateSerializeIsa.class)
    @JsonDeserialize(using = LocalDateDeserializeIsa.class)
    private LocalDate extensionFecha;

    private String usuarioAprobadorExtensionPlazo;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_ensayo_id", nullable = false)
    private List<SolicitudExtensionPlazo> extensionesPlazo = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_ensayo_id", nullable = false)
    private List<SolicitudEnsayoMina> minas = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "solicitud_ensayo_id", nullable = false)
    private List<SolicitudEnsayoDisenio> disenios = new ArrayList<>();

    @NotNull
    @Column(columnDefinition = "bit default  0")
    private boolean requiereMateriaPrima;

    private String proyectoNombre;
    private String proyectoUbicacion;
    private String proyectoProvincia;
    private String proyectoCanton;
    private String proyectoPais;
    private String proyectoContratista;
    private String proyectoFiscalizador;
    private String proyectoPropietario;
    @NotNull
    @Column(columnDefinition = "bit default 0")
    private boolean proyectoIniciado;
    @Column(precision = 20, scale = 16)
    private BigDecimal proyectoLatInicial;
    @Column(precision = 20, scale = 16)
    private BigDecimal proyectoLatFinal;
    @Column(precision = 20, scale = 16)
    private BigDecimal proyectoLngInicial;
    @Column(precision = 20, scale = 16)
    private BigDecimal proyectoLngFinal;
    private Integer proyectoNumeroCarriles;
    private BigDecimal proyectoDimension;

    @Transient
    private String observacion;

    @Transient
    private OrdenFlujo orden;

    public SolicitudEnsayo(String codigo, String proveedorNombre, Integer proveedorId, LocalDate fechaEntrega, String objetivo,
                           PrioridadNivel prioridad, TiempoEntrega tiempoEntrega, String detalleMaterial, String lineaAplicacion, BigDecimal cantidad,
                           UnidadMedida unidad, String nombreSolicitante, LocalDate muestraEntrega, String muestraUbicacion, String nombreComercial,
                           String tipoDiseno, String tipoDisenoOtro, List<SolicitudEnsayoAdjuntoRequerido> adjuntos) {
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
        this.tipoDiseno = tipoDiseno;
        this.tipoDisenoOtro = tipoDisenoOtro;
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

    public void marcarSolicitudComoAprobada(TipoAprobacionSolicitud tipoAprobacion, boolean requiereMateriaPrima) {
        setEstado(EstadoSolicitud.FINALIZADO);
        this.tipoAprobacion = tipoAprobacion;
        this.requiereMateriaPrima = requiereMateriaPrima;
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

    public void marcarAdjuntoRespaldoComoObligatorio() {
        boolean proyectoVial = this.getObjetivo().contains("Diseño Vial");
        if (this.getPrioridad().equals(PrioridadNivel.ALTO) || proyectoVial) {
            this.adjuntosRequeridos
                    .stream()
                    .filter(x -> x.getNombre().equalsIgnoreCase("Respaldo"))
                    .findFirst()
                    .ifPresent(adjuntoRequerido -> adjuntoRequerido.setObligatorio(true));
        }

        if (proyectoVial) {
            this.descmarcarAdjuntosComoObligatorio();
        }
    }

    private void descmarcarAdjuntosComoObligatorio() {
        this.adjuntosRequeridos
                .stream()
                .filter(x -> !x.getNombre().equalsIgnoreCase("Respaldo"))
                .forEach(x -> x.setObligatorio(false));
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

    public void agregarExtensionPlazo(SolicitudExtensionPlazo extensionPlazo) {
        this.extensionesPlazo.add(extensionPlazo);
    }

    public LocalDate getFechaSolicitudExtension() {
        LocalDate fechaTentativa = LocalDate.of(1999, 1, 1);
        if (this.extensionesPlazo.isEmpty())
            return fechaTentativa;
        else {
            SolicitudExtensionPlazo extensionPlazo = extensionesPlazo
                    .stream()
                    .filter(x -> x.getEstado().equals(EstadoExtensionPlazo.PENDIENTE))
                    .findFirst().orElse(null);
            return extensionPlazo == null ? fechaTentativa : extensionPlazo.getFechaSolicitud();
        }
    }

    public void agregarMina(SolicitudEnsayoMina solicitudEnsayoMina) {
        this.minas.add(solicitudEnsayoMina);
    }

    public String getEstadoTexto() {
        return estado.getDescripcion();
    }

    public List<TipoDiseno> getTipoDisenios() {
        if (this.disenios.isEmpty()) {
            return Collections.emptyList();
        }
        return this.getDisenios().stream().map(SolicitudEnsayoDisenio::getTipoDiseno).collect(Collectors.toList());
    }
}
