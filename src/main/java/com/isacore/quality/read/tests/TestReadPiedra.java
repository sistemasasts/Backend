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
public class TestReadPiedra {

	public static final String RESISTENCIA_FLEXION = "PROP_000000";

	public static final String PATH_TESTS_PIEDRA = "C:\\CRIMPTEK\\Calidad\\Tests\\EquipoUniversal\\Piedra";
	public static final String PATH_TESTSREAD_PIEDRA = "C:\\CRIMPTEK\\Calidad\\Tests\\TestRead\\Piedra";

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ITestService serviceTest;
	
	public void run() {

		File file = new File(PATH_TESTS_PIEDRA);

		if (file.exists()) {
			logger.info(">> Accediendo al directorio:::" + PATH_TESTS_PIEDRA + ":::");

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
			logger.error(">> El directorio:::" + PATH_TESTS_PIEDRA + ":::no existe");
	}

	public void readTest(String fileName, List<Test> listTests) {
		try {
			logger.info(">> ISA::method::readTest:::"+fileName);
			
			String pathTest = PATH_TESTS_PIEDRA + "\\" + fileName;
			String pathTestDestino = PATH_TESTSREAD_PIEDRA + "\\" + fileName;
			//Estraemos el numero de lote
			String[] dataName = fileName.split(" ");
			String batch = dataName[0];
			//leemos el contenido del archivo plano
			CSVReader reader = new CSVReader(new FileReader(pathTest), ';');
			List<String[]> data = reader.readAll();
			reader.close();

			String[] resistencia = data.get(0);
			
			Test t1 = new Test();
			t1.setBatchTest(batch);
			t1.setIdProperty(RESISTENCIA_FLEXION);
			t1.setResultTest(Double.parseDouble((String)resistencia[0]));
			listTests.add(t1);
			
			Path origenPath = Paths.get(pathTest);
            Path destinoPath = Paths.get(pathTestDestino);
			
			Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (FileNotFoundException e) {
			logger.error(">> ISA::method::readTest:::FileNotFoundException");
		} catch (IOException e) {
			logger.error(">> ISA::method::readTest:::IOException");
		}
	}	
	
}
