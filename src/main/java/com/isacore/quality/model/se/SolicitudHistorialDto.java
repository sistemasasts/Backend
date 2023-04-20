package com.isacore.quality.model.se;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudHistorialDto extends SolicitudHistorialBase {

	private Long id;
	private LocalDateTime fechaRegistro;
	private String usuarioNombeCompleto;
	private Long solicitudId;
	private String codigoSolicitud;
	private String orden;
	private String observacion;
	private boolean tieneAdjuntos;

}
