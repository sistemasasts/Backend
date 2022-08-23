package com.isacore.quality.model.se;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class SolicitudDocumento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudEnsayo solicitudEnsayo;
	
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
	
}
