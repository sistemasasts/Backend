package com.isacore.quality.model.se;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class SolicitudHistorial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudEnsayo solicitudEnsayo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudPruebasProceso solicitudPruebasProceso;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaRegistro;
	
	@Enumerated(EnumType.STRING)
	private EstadoSolicitud estadoSolicitud;
	
	@Enumerated(EnumType.STRING)
	private OrdenFlujo orden;
	
	private String observacion;
	
	private String usuarioId;
	
	private String usuarioNombeCompleto;
	
	private Boolean mostrar;
	
	@Transient
	private Boolean tieneAdjuntos;

	public SolicitudHistorial(SolicitudEnsayo solicitudEnsayo, OrdenFlujo orden, UserImptek usuario, String observacion) {
		super();
		this.solicitudEnsayo = solicitudEnsayo;
		this.estadoSolicitud = solicitudEnsayo.getEstado();
		this.orden = orden;
		this.observacion = observacion;
		this.fechaRegistro = LocalDateTime.now();
		this.usuarioId = usuario.getIdUser();
		this.usuarioNombeCompleto = usuario.getEmployee().getCompleteName();
		this.mostrar = Boolean.TRUE;
	}
	
	public SolicitudHistorial(SolicitudPruebasProceso solicitudE, OrdenFlujo orden, UserImptek usuario, String observacion) {
		super();
		this.solicitudPruebasProceso = solicitudE;
		this.estadoSolicitud = solicitudE.getEstado();
		this.orden = orden;
		this.observacion = observacion;
		this.fechaRegistro = LocalDateTime.now();
		this.usuarioId = usuario.getIdUser();
		this.usuarioNombeCompleto = usuario.getEmployee().getCompleteName();
		this.mostrar = Boolean.TRUE;
	}
	
	
}
