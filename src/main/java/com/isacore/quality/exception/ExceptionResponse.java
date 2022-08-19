package com.isacore.quality.exception;

import java.time.LocalDate;

public class ExceptionResponse {

	private LocalDate fecha	;
	
	private String mensaje;
	
	private String detalles;

	
	
	public ExceptionResponse(LocalDate fecha, String mensaje, String detalles) {
		this.fecha = fecha;
		this.mensaje = mensaje;
		this.detalles = detalles;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fechaHora) {
		this.fecha = fechaHora;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getDetalles() {
		return detalles;
	}

	public void setDetalles(String detalles) {
		this.detalles = detalles;
	}
	
	
	
}
