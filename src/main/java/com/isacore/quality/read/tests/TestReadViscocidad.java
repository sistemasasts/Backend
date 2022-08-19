package com.isacore.quality.read.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.isacore.quality.model.Test;
import com.isacore.quality.service.ITestService;

@Component
public class TestReadViscocidad {

	public static final String PROP_VISCOCIDAD = "PROP_Viscocidad";
	
	public static final String FILE_NAME = "Bitacora ingreso Viscosidad.xlsx";

	public static final String PATH_TESTS_TEMPLATE = "C:\\CRIMPTEK\\Calidad\\Tests\\BitacorasEnsayos\\" + FILE_NAME;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ITestService serviceTest;

	@Async
	public void run() {
		File excelFile = new File(PATH_TESTS_TEMPLATE);

		if (excelFile.exists()) {
			readTest(excelFile);
		} else {
			logger.error(">> El directorio:::" + PATH_TESTS_TEMPLATE + ":::no existe");
		}
	}

	public void readTest(File f) {

		String[] sheets = { "Cemento asfaltico", "Imperlastic", "Sol. Bentonita", "Superacryl", "Pintagro",
				"Sol. Alcohol Polivinilico", "Resina 445", "Resina 562", "Resina 554", "Resina 114 AD30I",
				"Resina 1570", "Resina W992" };

		ArrayList<Test> listTests = new ArrayList<>();

		XSSFWorkbook workbook = null;

		try {
			FileInputStream excelFile = new FileInputStream(f);

			workbook = new XSSFWorkbook(excelFile);

			for (int i = 0; i < sheets.length; i++)
				readSheet(sheets[i], listTests, workbook, i < 5 ? true : false);

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(">> No se ha podido leer el libro de excel:::" + FILE_NAME);
		} finally {
			try {
				workbook.close();
				logger.info(">> Cerrando libro:::" + FILE_NAME);
			} catch (IOException e) {
				logger.info(">> Cerrando libro:::" + FILE_NAME);
			}
		}

		if (!listTests.isEmpty()) {
			saveTest(listTests);
			logger.info(">>>> Valores del archivo: " + FILE_NAME + " Guardados:::::");
		}

	}

	public void readSheet(String sheetName, List<Test> listTests, XSSFWorkbook workbook, boolean flag) {

		XSSFSheet sheet = workbook.getSheet(sheetName);

		logger.info(">> Name sheet:::" + sheet.getSheetName() + ":::");

		Iterator<Row> rowIterator = sheet.iterator();

		Row row;

		row = rowIterator.next();
		row = rowIterator.next();
		row = rowIterator.next();
		row = rowIterator.next();
		row = rowIterator.next();
		row = rowIterator.next();

		int limit = sheet.getSheetName().equals("Cemento asfaltico") ? 22 : 16;

		CellType cellType;

		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Cell cell;

			Test test = new Test();
			test.setIdProperty(PROP_VISCOCIDAD);

			for (int i = 0; i < limit; i++) {
				switch (i) {
				case 0:
					cell = row.getCell(i);
					if (!(cell == null))
						logger.info("<<< " + cell.getNumericCellValue() + " | ");

					break;

				case 1:
					cell = row.getCell(i);
					if (!(cell == null)) {
						cellType = cell.getCellTypeEnum();
						if (cellType != CellType.STRING && cellType != CellType.NUMERIC) {
							logger.info("<<< " + cell.getDateCellValue() + " | ");
							//test.setDateLog(cell.getDateCellValue());
						}
					}
					break;

				case 2:
					cell = row.getCell(i);
					if (!(cell == null)) {
						cellType = cell.getCellTypeEnum();
						if (cellType != CellType.STRING && cellType != CellType.NUMERIC) {
							logger.info("<<< " + cell.getDateCellValue() + " | ");
							test.setTimeLog(cell.getDateCellValue());
						}
					}
					break;

				case 3:
					cell = row.getCell(i);
					if (!(cell == null)) {
						cellType = cell.getCellTypeEnum();
						if (cellType == CellType.STRING) {
							logger.info("<<< " + cell.toString() + " | ");
							test.setBatchTest(cell.getStringCellValue());
						} else if (cellType == CellType.NUMERIC) {
							logger.info("<<< " + cell.toString() + " | ");
							test.setBatchTest(Integer.toString((int) cell.getNumericCellValue()));
						}
					}
					break;

				case 4:
					cell = row.getCell(i);
					if (!(cell == null)) {
						if (flag) {
							logger.info("<<< " + cell.getStringCellValue() + " | ");
							test.setPresentation(cell.getStringCellValue());
						} else {
							logger.info("<<< " + cell.getStringCellValue() + " | ");
							test.setProvider(cell.getStringCellValue());
						}
					}
					break;

				case 5:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setSpindle(Double.toString(cell.getNumericCellValue()));
					}
					break;

				case 6:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setSpeedRPM(cell.getNumericCellValue());
					}
					break;

				case 7:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setTimeViscocidad((int) cell.getNumericCellValue());
					}
					break;

				case 8:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setTorque(cell.getNumericCellValue());
					}
					break;

				case 9:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setTemperature(cell.getNumericCellValue());
					}
					break;

				case 10:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setResultTest(cell.getNumericCellValue());
					}
					break;

				case 11:
					cell = row.getCell(i);
					if (!(cell == null)) {
						cellType = cell.getCellTypeEnum();
						if (cellType == CellType.NUMERIC) {
							logger.info("<<< " + cell.getNumericCellValue() + " | ");
							test.setMin(cell.getNumericCellValue());
						}
					}
					break;

				case 12:
					cell = row.getCell(i);
					if (!(cell == null)) {
						cellType = cell.getCellTypeEnum();
						if (cellType == CellType.NUMERIC) {
							logger.info("<<< " + cell.getNumericCellValue() + " | ");
							test.setMax(cell.getNumericCellValue());
						}
					}
					break;

				case 13:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getStringCellValue() + " | ");
						test.setPassTest(cell.getStringCellValue().equals("Cumple") ? true : false);
					}
					break;

				case 14:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getStringCellValue() + " | ");
						test.setOwner(cell.getStringCellValue());
					}
					break;

				case 15:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.toString() + " | ");
						test.setComment(cell.toString());
					}
					break;

				case 16:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.toString() + " | ");
						test.setRes114Provider(cell.toString());
					}
					break;

				case 17:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.toString() + " | ");
						test.setRes114Batch(cell.toString());
					}
					break;

				case 18:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.toString() + " | ");
						test.setResAs45Provider(cell.toString());
					}
					break;

				case 19:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.toString() + " | ");
						test.setResAs45Batch(cell.toString());
					}
					break;

				case 20:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.toString() + " | ");
						test.setOperator(cell.toString());
					}
					break;

				default:
					System.out.println("Valor por defecto:: " + i);
					break;
				}
			}
			listTests.add(test);
		}

	}

	@Transactional
	public void saveTest(List<Test> tests) {
		tests.forEach(x -> this.serviceTest.create(x));
	}

}
