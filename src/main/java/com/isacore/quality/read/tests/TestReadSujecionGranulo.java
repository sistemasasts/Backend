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
public class TestReadSujecionGranulo {

	public static final String PROP_SUJECION_GRANULO = "PROP_SujecionGranulo";
	
	public static final String FILE_NAME = "FR-CDQ-00.05 Sujeción de Gránulos Diario.xlsx";

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

	private void readTest(File f) {

		String[] sheets = { "Formato" };

		ArrayList<Test> listTests = new ArrayList<>();

		XSSFWorkbook workbook = null;

		try {
			FileInputStream excelFile = new FileInputStream(f);

			workbook = new XSSFWorkbook(excelFile);

			for (int i = 0; i < sheets.length; i++)
				readSheet(sheets[i], listTests, workbook);

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

	private void readSheet(String sheetName, List<Test> listTests, XSSFWorkbook workbook) {

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

		CellType cellType;

		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Cell cell;

			Test test = new Test();
			test.setIdProperty(PROP_SUJECION_GRANULO);

			for (int i = 0; i < 20; i++) {
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
						logger.info("<<< " + cell.getStringCellValue() + " | ");
						test.setProductName(cell.getStringCellValue());
					}
					break;
					
				case 5:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getStringCellValue() + " | ");
						test.setColor(cell.getStringCellValue());
					}
					break;
					
				case 6:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setM1Ini(cell.getNumericCellValue());
					}
					break;
					
				case 7:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setM2Ini(cell.getNumericCellValue());
					}
					break;
					
				case 8:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setM3Ini(cell.getNumericCellValue());
					}
					break;
					
				case 9:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setM1End(cell.getNumericCellValue());
					}
					break;
					
				case 10:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setM2End(cell.getNumericCellValue());
					}
					break;
					
				case 11:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setM3End(cell.getNumericCellValue());
					}
					break;
					
				case 12:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setP1(cell.getNumericCellValue());
					}
					break;
					
				case 13:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setP2(cell.getNumericCellValue());
					}
					break;
					
				case 14:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setP3(cell.getNumericCellValue());
					}
					break;
					
				case 15:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getNumericCellValue() + " | ");
						test.setResultTest(cell.getNumericCellValue());
					}
					break;
					
				case 16:
					cell = row.getCell(i);
					if (!(cell == null)) {
						cellType = cell.getCellTypeEnum();
						if (cellType == CellType.NUMERIC) {
							logger.info("<<< " + cell.getNumericCellValue() + " | ");
							test.setMin(cell.getNumericCellValue());
						}
					}
					break;
					
				case 17:
					cell = row.getCell(i);
					if (!(cell == null)) {
						cellType = cell.getCellTypeEnum();
						if (cellType == CellType.NUMERIC) {
							logger.info("<<< " + cell.getNumericCellValue() + " | ");
							test.setMax(cell.getNumericCellValue());
						}
					}
					break;
					
				case 18:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getStringCellValue() + " | ");
						test.setPassTest(cell.getStringCellValue().equals("Cumple") ? true : false);
					}
					break;
					
				case 19:
					cell = row.getCell(i);
					if (!(cell == null)) {
						logger.info("<<< " + cell.getStringCellValue() + " | ");
						test.setOwner(cell.getStringCellValue());
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
