package com.isacore.quality.read.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isacore.quality.model.Test;
import com.isacore.quality.service.ITestService;

import au.com.bytecode.opencsv.CSVReader;

@Component
public class TestReadTraction {

	public static final String ALARGAMIENTO_ROTURA_LONGITUDINAL = "PROP_27";
	public static final String ALARGAMIENTO_ROTURA_TRANSVERSE = "PROP_26";

	public static final String RESISTENCIA_TRACCION_LONGITUDINAL = "PROP_20";
	public static final String RESISTENCIA_TRACCION_TRANSVERSE = "PROP_19";

	public static final String PATH_TESTS_TRACTION = "C:\\CRIMPTEK\\Calidad\\Tests\\EquipoUniversal\\Traccion";
	
	public static final String PATH_TESTREAD_TRACTION = "C:\\CRIMPTEK\\Calidad\\Tests\\TestRead\\Traccion";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ITestService serviceTest;

	public void run() {

		File file = new File(PATH_TESTS_TRACTION);

		if (file.exists()) {
			logger.info(">> Accediendo al directorio:::" + PATH_TESTS_TRACTION + ":::");

			String[] tests = file.list();

			if (tests.length != 0) {
				List<Test> listTests = new ArrayList<>();
				for (int i = 0; i < tests.length; i++)					
					readTest(tests[i], listTests);
				
				listTests.forEach(x -> System.out.println(x.getBatchTest() + "::" + x.getIdProperty() + "::" + x.getResultTest()));
			
				listTests.forEach(x -> this.serviceTest.create(x));
				
			} else
				logger.info(">> No existen tests");
		} else
			logger.error(">> El directorio:::" + PATH_TESTS_TRACTION + ":::no existe");
	}

	public void readTest(String fileName, List<Test> listTests) {
		try {
			logger.info(">> ISA::method::readTest:::"+fileName);
			
			String pathTest = PATH_TESTS_TRACTION + "\\" + fileName;
			String pathTestDestino = PATH_TESTREAD_TRACTION + "\\" + fileName;
			//Estraemos el numero de lote
			String[] dataName = fileName.split(" ");
			String batch = dataName[0];
			//leemos el contenido del archivo plano
			CSVReader reader = new CSVReader(new FileReader(pathTest), ';');
			List<String[]> data = reader.readAll();
			reader.close();

			String[] longitudinal = data.get(0);
			String[] transverse = data.get(1);
			
			Test t1 = new Test();
			t1.setBatchTest(batch);
			t1.setIdProperty(ALARGAMIENTO_ROTURA_LONGITUDINAL);
			t1.setResultTest(Double.parseDouble((String)longitudinal[1]));
			listTests.add(t1);
			
			Test t2 = new Test();
			t2.setBatchTest(batch);
			t2.setIdProperty(RESISTENCIA_TRACCION_LONGITUDINAL);
			t2.setResultTest(Double.parseDouble((String)longitudinal[2]));
			listTests.add(t2);
			
			Test t3 = new Test();
			t3.setBatchTest(batch);
			t3.setIdProperty(ALARGAMIENTO_ROTURA_TRANSVERSE);
			t3.setResultTest(Double.parseDouble((String)transverse[1]));
			listTests.add(t3);
			
			Test t4 = new Test();
			t4.setBatchTest(batch);
			t4.setIdProperty(RESISTENCIA_TRACCION_TRANSVERSE);
			t4.setResultTest(Double.parseDouble((String)transverse[2]));
			listTests.add(t4);
			
			Path origenPath = Paths.get(pathTest);
            Path destinoPath = Paths.get(pathTestDestino);
			
			Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
			//f.delete();
			
		} catch (FileNotFoundException e) {
			logger.error(">> ISA::method::readTest:::FileNotFoundException");
		} catch (IOException e) {
			logger.error(">> ISA::method::readTest:::IOException");
		}
	}	
}
