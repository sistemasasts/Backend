package com.isacore.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ReportUtil {

	public void guardarReportePDF(JRBeanCollectionDataSource beanColDataSource, String rutaReporte,
			String rutaArchivo) {
		Map parameters = new HashMap();
		try {
			// String ruta =
			// "C:\\Users\\USUARIO\\Documents\\NetBeansProjects\\fibraSisWeb\\web\\reportes\\inventarios\\orden_despacho\\"
			// + rutaReporte;
			String ruta = "C:\\Users\\Blade-Imptek\\Downloads\\MyReports\\" + rutaReporte;
			JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parameters, beanColDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, rutaArchivo);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
