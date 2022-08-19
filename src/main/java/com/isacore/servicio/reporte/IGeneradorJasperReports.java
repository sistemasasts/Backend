package com.isacore.servicio.reporte;

import java.util.Collection;
import java.util.Map;

import com.isacore.exception.reporte.JasperReportsException;

public interface IGeneradorJasperReports {

	 byte[] generarReporte(final String reporteNombre, final Collection<?> objetos,
			Map<String, Object> parametrosEspecificos) throws JasperReportsException;
}
