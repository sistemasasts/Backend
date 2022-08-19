package com.isacore.quality.read.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isacore.localdate.converter.LocalDateTimeConverter;
import com.isacore.quality.model.Test;
import com.isacore.quality.service.ITestService;

import au.com.bytecode.opencsv.CSVReader;
import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy.SelfInjection.Split;

@Component
public class TestReblandecimiento {

	public static final String PUNTO_REBLANDECIMIENTO = "PROP_1";

	public static final String PATH_TESTS_REBLANDECIMIENTO = "Z:\\";
	//public static final String PATH_TESTS_REBLANDECIMIENTO = "C:\\CRIMPTEK\\Calidad\\Tests\\EquipoReb\\Reblandecimiento";

	public static final String PATH_TESTREAD_REBLANDECIMIENTO = "C:\\CRIMPTEK\\Calidad\\Tests\\TestRead\\Reblandecimiento";

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ITestService serviceTest;

	public void run(String batch) {

		File file = new File(PATH_TESTS_REBLANDECIMIENTO);

		if (file.exists()) {
			logger.info(">> Accediendo al directorio:::" + PATH_TESTS_REBLANDECIMIENTO + ":::");

			String[] tests = file.list();

			if (tests.length != 0) {
				List<Test> listTests = new ArrayList<>();
				for (int i = 0; i < tests.length; i++) {
					if ((!tests[i].equalsIgnoreCase("System Volume Information"))
							& (!tests[i].equalsIgnoreCase("FOUND.000"))) {
						readTest(tests[i], listTests, batch);
					}

				}

				listTests.forEach(x -> System.out
						.println(x.getBatchTest() + "::" + x.getIdProperty() + "::" + x.getResultTest()));

				listTests.forEach(x -> this.serviceTest.create(x));

			} else
				logger.info(">> No existen tests");
		} else
			logger.error(">> El directorio:::" + PATH_TESTS_REBLANDECIMIENTO + ":::no existe");
	}

	public void readTest(String fileName, List<Test> listTests, String batchtoFound) {
		try {

			logger.info(">> ISA::method::readTest:::" + fileName);

			String pathTest = PATH_TESTS_REBLANDECIMIENTO + "\\" + fileName;
			String pathTestDestino = PATH_TESTREAD_REBLANDECIMIENTO + "\\" + fileName;
			// Estraemos el numero de lote
			String[] dataName = fileName.split("-");
			String batch = dataName[0];
			if(batch.equalsIgnoreCase(batchtoFound)) {
				String tankString = dataName[1];
				String[] tankVector = tankString.split("T");
				Integer tank = Integer.parseInt(tankVector[1]);
				String PoM = dataName[2];

				// leemos el contenido del archivo plano
				CSVReader reader = new CSVReader(new FileReader(pathTest), '\n');
				List<String[]> data = reader.readAll();
				reader.close();
				
				String[] date=data.get(6);
				String[] hour=data.get(8);
				LocalDateTime dateTime=getDateTimeTestPF(date[0], hour[0]);

				String[] bolaDerecha = data.get(26);
				// String[] rightBall = bolaDerecha[0].split(" ");
				Double rightBallValue = getValueDataRead(bolaDerecha[0]);

				String[] bolaIzquierda = data.get(28);
				// String[] lefttBall = bolaIzquierda[0].split(" ");
				Double leftBallValue = getValueDataRead(bolaIzquierda[0]);
				
				if(rightBallValue !=null & leftBallValue != null) {
					
					/*String[] media = data.get(30);
					String[] averagMP = media[0].split(" ");
					String cadenaTransform = averagMP[1].replaceAll("", ";");
					String[] cadenaConversion = cadenaTransform.split(";");
					String value = "";
					for (int i = 0; i < cadenaConversion.length; i++) {
						if (i % 2 == 0)
							value = value + cadenaConversion[i];
					}*/					
					Double averagMPValue =  Math.round(((rightBallValue + leftBallValue)/2) * 10d) / 10d;
					
					Test t1 = new Test();
					t1.setBatchTest(batch);

					t1.setIdProperty(PUNTO_REBLANDECIMIENTO);
					t1.setDateLog(dateTime);
					if (PoM.equalsIgnoreCase("P")) {

						t1.setPremixer(tank);
						t1.setM1Ini(rightBallValue);
						t1.setM2Ini(leftBallValue);
						t1.setAverageMP(averagMPValue);

					} else if (PoM.equalsIgnoreCase("M")) {

						t1.setMixer(tank);
						t1.setM1End(rightBallValue);
						t1.setM2End(leftBallValue);
						t1.setResultTest(averagMPValue);
					}

					listTests.add(t1);

					Path origenPath = Paths.get(pathTest);
					Path destinoPath = Paths.get(pathTestDestino);

					Files.move(origenPath, destinoPath, StandardCopyOption.REPLACE_EXISTING);
				}
				
			}
			

		} catch (FileNotFoundException e) {
			logger.error(">> ISA::method::readTest:::FileNotFoundException");
		} catch (IOException e) {
			logger.error(">> ISA::method::readTest:::IOException");
		}
	}

	private Double getValueDataRead(String cadena) throws IOException {

		String[] cadenaArray = cadena.split(" ");
		String cadenaTransform = cadenaArray[2].replaceAll("", ";");
		String[] cadenaConversion = cadenaTransform.split(";");
		String value = "";
		for (int i = 0; i < cadenaConversion.length; i++) {
			if (i % 2 == 0)
				value = value + cadenaConversion[i];
		}
		if(value.equalsIgnoreCase("---"))
			return null;
		else 
			return Double.parseDouble(value);
	}
	
	private LocalDateTime getDateTimeTestPF(String date, String hour) {
		
		String[] dateCovert = date.split(":");
		String cadenaTransform = dateCovert[1].replaceAll("", ";");
		String[] cadenaConversion = cadenaTransform.split(";");
		String value = "";
		for (int i = 4; i < cadenaConversion.length; i++) {
			if (i % 2 == 0)
				value = value + cadenaConversion[i];
		}
		String valueConvert=value.replaceAll("/", "-");
		String[] dateCovertHour = hour.split(" ");
		String hourTransform = dateCovertHour[3].replaceAll("", ";");
		String[] hourConversion = hourTransform.split(";");
		String valueHour = "";
		for (int i = 0; i < hourConversion.length; i++) {
			if (i % 2 == 0)
				valueHour = valueHour + hourConversion[i];
		}
		
		String [] vl= valueHour.split(":");
		if(vl[0].length()==1) {
			vl[0]=vl[0]+"0";
			valueHour="0"+valueHour;
		}
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); 
		LocalDateTime dateTime = LocalDateTime.parse(valueConvert+" "+valueHour, formatter);
		
		
		
		return dateTime;
	}

}
