package com.isacore.quality.model.spp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.se.EstadoSolicitud;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.model.se.SolicitudBase;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
public class SolicitudPruebasProceso  extends SolicitudBase {

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
	
	@Transient
	private String observiacionFlujo;

	@Transient
	private OrdenFlujoPP orden;

	public SolicitudPruebasProceso(String codigo, LocalDate fechaEntrega, String lineaAplicacion, String motivo, String motivoOtro,
			String materialLineaProceso, String materialLineaProOtro, String descripcionProducto,
			String variablesProceso, String verificacionAdicional, String observacion, String nombreSolicitante) {
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
	}
	
	public void marcarSolicitudComoEnviada(String usuarioAsignado) {
		setEstado(EstadoSolicitudPP.ENVIADO_REVISION);
		setUsuarioAprobador(usuarioAsignado);
	}
	
	public void marcarSolicitudComoValidada(String usuarioAsignado) {
		setEstado(EstadoSolicitudPP.EN_PROCESO_CALIDAD);
		setUsuarioGestion(usuarioAsignado);
	}

	public void marcarSolicitudComoRegresada() {
		setEstado(EstadoSolicitudPP.RECHAZADO);
	}

	public void anular(){
		this.setFechaFinalizacion(LocalDateTime.now());
		this.setEstado(EstadoSolicitudPP.ANULADO);
	}
	
}
