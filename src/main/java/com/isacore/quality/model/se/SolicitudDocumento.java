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

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class SolicitudDocumento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudEnsayo solicitudEnsayo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudPruebasProceso solicitudPruebasProceso;
	
	private String path;
	
	@Enumerated(EnumType.STRING)
	private EstadoSolicitud estado;
	
	private LocalDateTime fechaSubidaArchivo;
	
	private String nombreArchivo;
	
	@Enumerated(EnumType.STRING)
	private OrdenFlujo ordenFlujo;

	public SolicitudDocumento(SolicitudEnsayo solicitudEnsayo, String path,	String nombreArchivo, OrdenFlujo ordenFlujo) {
		super();
		this.solicitudEnsayo = solicitudEnsayo;
		this.path = path;
		this.estado = solicitudEnsayo.getEstado();
		this.nombreArchivo = nombreArchivo;
		this.ordenFlujo = ordenFlujo;
		this.fechaSubidaArchivo = LocalDateTime.now();
	}
	
	public SolicitudDocumento(SolicitudPruebasProceso solicitudPruebasProceso, String path,	String nombreArchivo, OrdenFlujo ordenFlujo) {
		super();
		this.solicitudPruebasProceso = solicitudPruebasProceso;
		this.path = path;
		this.estado = solicitudPruebasProceso.getEstado();
		this.nombreArchivo = nombreArchivo;
		this.ordenFlujo = ordenFlujo;
		this.fechaSubidaArchivo = LocalDateTime.now();
	}
	
	
	
	
}
