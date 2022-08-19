package com.isacore.quality.model.se;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;

import lombok.Data;
import lombok.NoArgsConstructor;


@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class SolicitudDTO implements Serializable{

	private Long id;
	
	private String codigo;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaCreacion;
	
	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate fechaAprobacion;
	
	private String nombreSolicitante;
	
	private String usuarioGestion;
	
	private String usuarioAprobador;
	
	private EstadoSolicitud estado;
	
	private String proveedorNombre;
	
	private Integer proveedorId;
	
	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate fechaEntrega;
	
	private String detalleMaterial;
	
	private TipoSolicitud tipoSolicitud;
	
	private TipoAprobacionSolicitud tipoAprobacion;

	public SolicitudDTO(Long id, String codigo, LocalDateTime fechaCreacion, LocalDate fechaAprobacion,
			String nombreSolicitante, String usuarioGestion, String usuarioAprobador, EstadoSolicitud estado,
			String proveedorNombre, Integer proveedorId, LocalDate fechaEntrega, String detalleMaterial,
			TipoSolicitud tipoSolicitud, TipoAprobacionSolicitud tipoAprobacion) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.fechaCreacion = fechaCreacion;
		this.fechaAprobacion = fechaAprobacion;
		this.nombreSolicitante = nombreSolicitante;
		this.usuarioGestion = usuarioGestion;
		this.usuarioAprobador = usuarioAprobador;
		this.estado = estado;
		this.proveedorNombre = proveedorNombre;
		this.proveedorId = proveedorId;
		this.fechaEntrega = fechaEntrega;
		this.detalleMaterial = detalleMaterial;
		this.tipoSolicitud = tipoSolicitud;
		this.tipoAprobacion = tipoAprobacion;
	}

		
	
	
}
