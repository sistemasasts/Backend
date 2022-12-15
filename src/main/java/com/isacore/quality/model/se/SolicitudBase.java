package com.isacore.quality.model.se;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.*;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class SolicitudBase {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String codigo;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaCreacion;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaFinalizacion;

	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate fechaRespuesta;

	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate fechaAprobacion;

	private int tiempoRespuesta;

	private int tiempoAprobacion;

	private String creadoPor;

	private String nombreSolicitante;

	private String usuarioGestion;

	private String usuarioAprobador;

	private Long solicitudPadreId;
	protected SolicitudBase() {}

	public SolicitudBase(String codigo, String nombreSolicitante) {
		super();
		this.codigo = codigo;
		this.fechaCreacion = LocalDateTime.now();
		this.creadoPor = nombreSolicitante;
		this.nombreSolicitante = nombreSolicitante;
	}

	public void finalizarSolicitud() {
		this.fechaFinalizacion = LocalDateTime.now();
	}

    public String getFechaSolicitud() {
        return UtilidadesFecha.formatear(getFechaCreacion(), "YYYY-MM-dd");
    }
}
