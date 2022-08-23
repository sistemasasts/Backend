package com.isacore.quality.model.spp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.util.LocalDateTimeDeserialize;
import com.isacore.util.LocalDateTimeSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class SolicitudPruebaProcesoHistorial {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private SolicitudPruebasProceso solicitudPruebasProceso;

	@JsonSerialize(using = LocalDateTimeSerialize.class)
	@JsonDeserialize(using = LocalDateTimeDeserialize.class)
	private LocalDateTime fechaRegistro;

	@Enumerated(EnumType.STRING)
	private EstadoSolicitudPP estadoSolicitud;

	@Enumerated(EnumType.STRING)
	private OrdenFlujoPP orden;

	private String observacion;

	private String usuarioId;

	private String usuarioNombeCompleto;

	private Boolean mostrar;

	@Transient
	private Boolean tieneAdjuntos;

	public SolicitudPruebaProcesoHistorial(SolicitudPruebasProceso solicitudE, OrdenFlujoPP orden, UserImptek usuario, String observacion) {
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
