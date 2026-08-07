package com.isacore.quality.model.spp;

import com.isacore.quality.model.se.TipoSolicitud;
import com.isacore.security.model.Usuario;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class ConfiguracionFlujoPruebaProceso {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	private TipoSolicitud tipoSolicitud;
	
	@Enumerated(EnumType.STRING)
	private OrdenFlujoPP orden;
	
	public String getUsuarioId() {
		return getUsuario().getNombreUsuario();
	}
}
