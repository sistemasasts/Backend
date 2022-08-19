package com.isacore.quality.model.se;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
}
