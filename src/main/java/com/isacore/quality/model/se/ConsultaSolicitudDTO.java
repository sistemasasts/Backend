package com.isacore.quality.model.se;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;

import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class ConsultaSolicitudDTO implements Serializable{

	private TipoSolicitud tipoSolicitud;
	
	private String codigo;
	
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;
	
	private TipoAprobacionSolicitud tipoAprobacion;
	
	private String nombreSolicitante;
	
	private String usuarioGestion;
	
	private String usuarioAprobador;
	
	private String usuarioValidador;
	
	private EstadoSolicitud estado;
	
	
}
