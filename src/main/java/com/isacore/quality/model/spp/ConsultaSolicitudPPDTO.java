package com.isacore.quality.model.spp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class ConsultaSolicitudPPDTO implements Serializable{

	private String codigo;
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaInicio;
	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
    private LocalDateTime fechaFin;
	private TipoAprobacionPP tipoAprobacion;
	private String nombreSolicitante;
	private String usuarioGestionPlanta;
	private String usuarioGestionCalidad;
	private String usuarioGestionMantenimiento;
	private String usuarioAprobador;
	private String usuarioValidador;
	private List<EstadoSolicitudPP> estados;
}
