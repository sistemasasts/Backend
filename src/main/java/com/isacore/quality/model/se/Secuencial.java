package com.isacore.quality.model.se;

import static com.isacore.util.UtilidadesCadena.completarCerosIzquierda;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class Secuencial {

	private static final int LONGITUD_NUMERO = 10;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private TipoSolicitud tipoSolicitud;

	private long sucesion;

	private String abreviatura;

	@NotNull
	private boolean activo;

	private String numeroSecuencial;
	

	public long aumentarSucesion() {
			return sucesion++;
	}


	public String getNumeroSecuencial() {
		return this.abreviatura.concat("-").concat(completarCerosIzquierda(this.sucesion, LONGITUD_NUMERO));
	}


}
