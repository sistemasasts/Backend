package com.isacore.quality.read.formulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.isacore.quality.model.Formulation;
import com.isacore.quality.service.IFormulationService;

@Component
public class GeneralReadFormulation {

	public static final String FILE_NAME = "Fórmulas Productos  V.0L.xlsx";

	public static final String PATH_TESTS_TEMPLATE = "C:\\CRIMPTEK\\Calidad\\EspecificacionesFormulaciones\\"
			+ FILE_NAME;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	IFormulationService formulationService;
	
	public void run(String user) {
		File excelFile = new File(PATH_TESTS_TEMPLATE);
		if (excelFile.exists()) {
			readFormulation(excelFile, user);
		} else {
			logger.error(">> El directorio:::" + PATH_TESTS_TEMPLATE + ":::no existe");
		}
	}
	
	private void readFormulation(File f, String user) {
		
		FileInputStream excelFile;
		XSSFWorkbook workbook = null;
		
		try {
			
			excelFile = new FileInputStream(f);

			workbook = new XSSFWorkbook(excelFile);
			
			List<Formulation> listFormulation;
			
			listFormulation = ReadFormulationImpermeabilizacion.read(workbook.getSheet("Formulas Impermeabilización"));
			writeFormulation(listFormulation, user);
			
		} catch (IOException e) {
			logger.info(">> No se ha podido leer el libro de excel:::" + FILE_NAME);
		} finally {
			try {
				workbook.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			;
		}
		
	}
	
	@Transactional
	private void writeFormulation(List<Formulation> listFormulation, String user) {
		
		listFormulation.forEach(x -> {
			
			if(this.formulationService.validateExistFormulation(x.getFormulationItem().getIdFItem(), 
					x.getFormulationList().getIdFormula(),
					x.getProduct().getIdProduct())) 
				this.formulationService.updateFormulation(x, user);
			else 
				this.formulationService.createFormulation(x, user);
			
		});
		System.out.println("================Fin de la tarea=================");
		
	}
}
