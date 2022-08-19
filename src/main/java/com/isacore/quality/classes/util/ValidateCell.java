package com.isacore.quality.classes.util;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import com.isacore.util.UtilConvert;

public class ValidateCell {

//	public static Integer validateInteger(Cell cell) {
//		if (!(cell == null)) {
//			if (cell.getCellTypeEnum() == CellType.NUMERIC) {
//				System.out.println("<<< " + cell.getNumericCellValue() + " | ");
//				return ((int) cell.getNumericCellValue());
//			} else {
//				if (cell.getCellTypeEnum() == CellType.FORMULA) {
//					System.out.println("<<< " + cell.getNumericCellValue() + " | ");
//					return ((int) cell.getNumericCellValue());
//				}
//			}
//		}
//		return null;
//	}

	public static Integer validateInteger(Cell cell) {
		if (!(cell == null)) {
			if (cell.getCellTypeEnum() == CellType.NUMERIC) {
				System.out.println("<<< " + cell.getNumericCellValue() + " | ");
				return ((int) cell.getNumericCellValue());
			} else {
				if (cell.getCellTypeEnum() == CellType.FORMULA) {
					System.out.println("<<< " + cell.getNumericCellValue() + " | ");
					return ((int) cell.getNumericCellValue());
				}
			}
		}
		return null;
	}

	public static Double validateDouble(Cell cell) {
		if (!(cell == null)) {
			try {
				if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA) {
					System.out.println("<<< " + cell.getNumericCellValue() + " | ");
					return cell.getNumericCellValue();
				}
			} catch (RuntimeException e) {
				return null;
			}
		}
		return null;
	}

	public static String validateString(Cell cell) {
		if (!(cell == null)) {
			if (cell.getCellTypeEnum() == CellType.STRING) {
				System.out.println("<<< " + cell.getStringCellValue() + " | ");
				return cell.getStringCellValue();
			} else {
				if (cell.getCellTypeEnum() == CellType.FORMULA) {
					System.out.println("<<< " + cell.getStringCellValue() + " | ");
					return cell.getStringCellValue();
				}
			}
		}
		return null;
	}

	public static String toString(Cell cell) {
		if (!(cell == null)) {
			System.out.println("<<< " + cell.toString() + " | ");
			return cell.toString();
		}
		return null;
	}

	public static LocalDate validateLocalDate(Cell cell) {
		if (!(cell == null)) {
			try {
				System.out.println("<<< " + cell.getDateCellValue() + " | ");
				return UtilConvert.toLocalDate(cell.getDateCellValue());
			} catch (RuntimeException e) {
				return null;
			}
		}
		return null;
	}

	public static boolean isEnd(Row row) {
		Cell cell = row.getCell(0);
		return cell.toString().equalsIgnoreCase("FIN") ? true : false;
	}

}
