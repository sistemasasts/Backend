package com.isacore.quality.model.se;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ConfiguracionTiempoSolicitud {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private TipoSolicitud tipoSolicitud;
	
	@Enumerated(EnumType.STRING)
	private TiempoEntrega tipoEntrega;
	
	@Enumerated(EnumType.STRING)
	private OrdenFlujo orden;
	
	private int vigenciaDias;

	@Column(columnDefinition = "INT DEFAULT 1", nullable = false)
	private int vigenciaDiasDisenioPavimentos;
}
