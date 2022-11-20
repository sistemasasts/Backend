package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class ErrorReporteException extends QualityException{

	@Override
	public String getMessage() {
		return "Error al momento de genrerar el reporte";
	}

}
