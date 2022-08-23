package com.isacore.quality.model.spp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.quality.model.se.TipoAprobacionSolicitud;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class SolicitudPPDTO implements Serializable{

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

	private EstadoSolicitudPP estado;

	private String proveedorNombre;

	private Integer proveedorId;

	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate fechaEntrega;

	private String detalleMaterial;

	private TipoAprobacionSolicitud tipoAprobacion;

	public SolicitudPPDTO(Long id, String codigo, LocalDateTime fechaCreacion, LocalDate fechaAprobacion,
                          String nombreSolicitante, String usuarioGestion, String usuarioAprobador, EstadoSolicitudPP estado,
                          String proveedorNombre, Integer proveedorId, LocalDate fechaEntrega, String detalleMaterial,
                          TipoAprobacionSolicitud tipoAprobacion) {
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
		this.tipoAprobacion = tipoAprobacion;
	}
	
}
