package com.isacore.servicio.reporte;

import com.isacore.exception.reporte.JasperReportsException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

@Service
public class GeneradorJasperReportsImpl implements IGeneradorJasperReports {

	private static final Log LOG = LogFactory.getLog(GeneradorJasperReportsImpl.class);

	@Override
	public byte[] generarReporte(String reporteNombre, Collection<?> objetos, Map<String, Object> parametrosEspecificos)
			throws JasperReportsException {

		LOG.info("INICIA creacion de documento JASPER");

		try {
			final InputStream ruta = prepararReporte(reporteNombre);
			this.cargarParametrosBase(parametrosEspecificos);
			JasperPrint print = JasperFillManager.fillReport(ruta, parametrosEspecificos,
					new JRBeanCollectionDataSource(objetos));

			LOG.info("FINALIZA creacion de documento JASPER");
			return JasperExportManager.exportReportToPdf(print);

		} catch (Exception e) {
			LOG.error("Se produjo un error al generar el reporte: {}", e);
			LOG.info("FINALIZA creacion de documento JASPER");
			throw new JasperReportsException();
		}

	}

	//Funciona para cuando se compila un war
	private String prepararReporteRuta(final String reporteNombre) throws IOException {

		final String ruta = "/reports/" + reporteNombre + ".jasper";
		final File file = new ClassPathResource(ruta).getFile();
		LOG.info("Ruta del reporte: " + ruta);
		return file.getPath();
	}

	//Funciona para cuando se compila en jar
	private InputStream prepararReporte(final String reporteNombre) throws IOException {
		final String ruta = "/reports/" + reporteNombre + ".jasper";
		LOG.info("Ruta del reporte: " + ruta);
		return new ClassPathResource(ruta).getInputStream();
	}

	private void cargarParametrosBase(Map<String, Object> parametrosBase){
		parametrosBase.put(ReporteConstantes.LOGO_PARAMETRO, getImagenStream(ReporteConstantes.LOGO_IMAGEN));
		parametrosBase.put(ReporteConstantes.FONDO_PARAMETRO, getImagenStream(ReporteConstantes.FONDO_IMAGEN));
		parametrosBase.put(ReporteConstantes.SELLO_CALIDAD_PARAMETRO, getImagenStream(ReporteConstantes.SELLO_CALIDAD_IMAGEN));
	}

	private InputStream getImagenStream(String imageName) {
		String imagenBase = "/images/";
		InputStream is = this.getClass().getResourceAsStream(imagenBase + imageName);
		if (is == null) {
			// Opcional: devolver una imagen por defecto o lanzar excepción
			throw new IllegalArgumentException("Recurso de imagen no encontrado: " + imageName);
		}
		return is;
	}

}
