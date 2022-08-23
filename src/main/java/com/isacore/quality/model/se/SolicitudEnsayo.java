package com.isacore.quality.model.se;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
	
	private String unidad;
	
	private Boolean dataSheet;
	
	private Boolean msds;
	
	private String validador;
	
	@Transient
	private String observacion;

	@Transient
	private OrdenFlujo orden;

	public SolicitudEnsayo(String codigo, String proveedorNombre, Integer proveedorId, LocalDate fechaEntrega, String objetivo,
			PrioridadNivel prioridad, TiempoEntrega tiempoEntrega ,String detalleMaterial, String lineaAplicacion, String uso, BigDecimal cantidad,
			String unidad, String nombreSolicitante) {
		super(codigo, nombreSolicitante);
		this.proveedorNombre = proveedorNombre;
		this.proveedorId = proveedorId;
		this.fechaEntrega = fechaEntrega;
		this.objetivo = objetivo;
		this.prioridad = prioridad;
		this.tiempoEntrega = tiempoEntrega;
		this.detalleMaterial = detalleMaterial;
		this.lineaAplicacion = lineaAplicacion;
		this.uso = uso;
		this.cantidad = cantidad;
		this.unidad = unidad;
		this.estado = EstadoSolicitud.NUEVO;
	}
	
	public void marcarSolicitudComoValidada(String usuarioAsignado, int tiempoRespuesta) {
		setEstado(EstadoSolicitud.EN_PROCESO);
		setUsuarioGestion(usuarioAsignado);
		setTiempoRespuesta(tiempoRespuesta);
		this.fechaEntregaValidacion =  LocalDate.now();
	}
	
	public void marcarSolicitudComoEnviada(String usuarioAsignado) {
		setEstado(EstadoSolicitud.ENVIADO_REVISION);
		this.validador = usuarioAsignado;
	}
	
	public void marcarSolicitudComoRespondida(String usuarioAsignado, int tiempoAprobacion) {
		if(getEstado().equals(EstadoSolicitud.EN_PROCESO)) {
			setFechaRespuesta(LocalDate.now());
			setTiempoAprobacion(tiempoAprobacion);
		}
		setEstado(EstadoSolicitud.REVISION_INFORME);		
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
		if(getEstado().equals(EstadoSolicitud.EN_PROCESO)) {			
			LocalDate fechaLimite = this.fechaEntregaValidacion.plusDays(getTiempoRespuesta());
			Duration diff = Duration.between(LocalDate.now().atStartOfDay(), fechaLimite.atStartOfDay());
			return (int) diff.toDays();
		}
		
		if(getEstado().equals(EstadoSolicitud.REVISION_INFORME)) {			
			LocalDate fechaLimite = this.getFechaRespuesta().plusDays(getTiempoAprobacion());
			Duration diff = Duration.between(LocalDate.now().atStartOfDay(), fechaLimite.atStartOfDay());
			return (int) diff.toDays();
		}
		return 0;
	}

	public void anular() {
		this.setFechaFinalizacion(LocalDateTime.now());
		this.estado = EstadoSolicitud.ANULADO;
	}

	public void rechazar() {
		this.setFechaFinalizacion(LocalDateTime.now());
		this.estado = EstadoSolicitud.RECHAZADO;
	}
}
