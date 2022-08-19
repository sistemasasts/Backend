package com.isacore.quality.model.se;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.isacore.sgc.acta.model.UserImptek;

import lombok.Data;

@Data
@Entity
public class ConfiguracionUsuarioRolEnsayo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", nullable = false, updatable = false)
	private UserImptek usuario;
	
	@Enumerated(EnumType.STRING)
	private TipoSolicitud tipoSolicitud;
	
	@Enumerated(EnumType.STRING)
	private OrdenFlujo orden;
	
	public String getUsuarioId() {
		return getUsuario().getIdUser();
	}
}
