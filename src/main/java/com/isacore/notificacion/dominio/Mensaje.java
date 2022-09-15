package com.isacore.notificacion.dominio;

import lombok.Data;

import java.util.Set;

@Data
public class Mensaje {
	
	private String asunto;
	private String cuerpo;
	private DireccionesDestino direccionesDestino;
	private MensajeFormato formato;
	private MensajeTipo tipo;
	
	public Mensaje(MensajeTipo tipo, MensajeFormato formato, DireccionesDestino direccionesDestino, String asunto, String cuerpo) {
		this.tipo = tipo;
		this.formato = formato;
		this.direccionesDestino = direccionesDestino;
		this.asunto = asunto;
		this.cuerpo = cuerpo;
	}

	public Set<String> getDireccionesA() {
		return direccionesDestino.getDireccionesA();
	}

	public Set<String> getDireccionesCC() {
		return direccionesDestino.getDireccionesCC();
	}

	public Set<String> getDireccionesCCO() {
		return direccionesDestino.getDireccionesCCO();
	}

	public boolean isTieneDireccionesA() {
		return direccionesDestino.isTieneDireccionesA();
	}

	public boolean isTieneDireccionesCC() {
		return direccionesDestino.isTieneDireccionesCC();
	}

	public boolean isTieneDireccionesCCO() {
		return direccionesDestino.isTieneDireccionesCCO();
	}
}
