package com.isacore.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilidadesFecha {

	public final static String formatear(LocalDateTime fecha, String patron) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patron);

		return fecha.format(formatter);
	}

	public final static String formatearLocalDateATexto(LocalDate fecha, String patron) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patron);

		return fecha.format(formatter);
	}
	
	public final static LocalDateTime stringToLocalDateTime(String fecha, String formato) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
		return LocalDateTime.parse(fecha, formatter);
	}

}
