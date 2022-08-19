package com.isacore.quality.read.specification.pt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.isacore.quality.model.Norm;
import com.isacore.quality.model.PropertyList;
import com.isacore.quality.model.PropertyPeriodicity;

public class ReadNorm {

	/*
	 * Lectura de la hoja de Normas Productos
	 */
	public static List<Norm> readNorms(XSSFSheet sheet) {

		System.out.println(">> Name sheet:::" + sheet.getSheetName() + ":::");

		List<Norm> listNorms = new ArrayList<>();

		Iterator<Row> rowIterator = sheet.iterator();

		Row row;

		row = rowIterator.next();

		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Cell cell;

			Norm norm = new Norm();

			for (int i = 0; i < 4; i++) {
				switch (i) {
				case 0:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getNumericCellValue() + " | ");
						norm.setIdNorm((int) cell.getNumericCellValue());
					}
					break;

				case 1:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						norm.setNameNorm(cell.getStringCellValue());
					}
					break;

				case 2:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						norm.setAplication(cell.getStringCellValue());
					}
					break;

				case 3:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						norm.setDescription(cell.getStringCellValue());
					}
					break;

				default:
					break;
				}
			}
			listNorms.add(norm);
		}
		return listNorms;

	}

	/*
	 * lectura de la hoja Normas de Ensayo
	 */
	public static List<PropertyList> readSpecificationText(XSSFSheet sheet) {

		System.out.println(">> Name sheet:::" + sheet.getSheetName() + ":::");

		List<PropertyList> listProperty = new ArrayList<>();

		Iterator<Row> rowIterator = sheet.iterator();

		Row row;

		row = rowIterator.next();

		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			Cell cell;

			PropertyList pl = new PropertyList();
			
			for(int i = 0; i < 7; i++) {
				switch (i) {
				case 0:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						pl.setIdProperty(cell.getStringCellValue());
					}
					break;
					
				case 1:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						pl.setNameProperty(cell.getStringCellValue());
					}
					break;
					
				case 2:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						//pl.setNormName(cell.getStringCellValue());
					}
					break;
					
				case 3:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						pl.setLineApplication(cell.getStringCellValue());
					}
					break;
					
				case 4:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						pl.setPeriodicity(PropertyPeriodicity.valueOf(cell.getStringCellValue()));
					}
					break;
					
				case 5:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						pl.setMachine(cell.getStringCellValue());
					}
					break;
					
				case 6:
					cell = row.getCell(i);
					if (!(cell == null)) {
						System.out.println("<<< " + cell.getStringCellValue() + " | ");
						pl.setTypeProperty(cell.getStringCellValue().equalsIgnoreCase("visual") ? "V" : "T");
					}
					break;

				default:
					break;
				}
			}
			listProperty.add(pl);
		}

		return listProperty;
	}
}
