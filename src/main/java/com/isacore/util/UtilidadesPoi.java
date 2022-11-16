package com.isacore.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import static com.isacore.util.UtilidadesCadena.noEsNuloNiBlanco;

public class UtilidadesPoi {

	public static final void altoParaNumeroDeFilas(Row row, int numeroFilas) {
		row.setHeightInPoints(numeroFilas * row.getSheet().getDefaultRowHeightInPoints());
	}

	public static final void anchoAutomatico(Sheet sheet, final int indiceUltimaColumna) {
		for (int i = 0; i <= indiceUltimaColumna; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	public static final void anchoColumna(Sheet sheet, int indiceColumna, int anchoColumnaPixelesAprox) {
		sheet.setColumnWidth(indiceColumna, anchoColumnaPixelesAprox * 36);
	}

	public static final void anchoParaCaracteres(Sheet sheet, int indiceColumna, byte caracteres) {
		  sheet.setColumnWidth(indiceColumna, caracteres * 256);
	}

	public static final void autoFiltro(Row row, int columnaInicial, int columnaFinal) {
		autoFiltro(row.getSheet(), row.getRowNum(), columnaInicial, columnaFinal);
	}

	public static final void autoFiltro(Sheet sheet, int fila, int columnaInicial, int columnaFinal) {
		sheet.setAutoFilter(new CellRangeAddress(fila, fila, columnaInicial, columnaFinal));
	}

	public static final void autoFiltroPrimeraFila(Sheet sheet, int columnaInicial, int columnaFinal) {
		autoFiltro(sheet, 0, columnaInicial, columnaFinal);
	}

	public static final void combinarCeldasDeFila(Row row, int indiceColumnaInicial, int indiceColumnaFinal) {

		final CellRangeAddress cellRange = new CellRangeAddress(
				row.getRowNum(),
				row.getRowNum(),
				indiceColumnaInicial,
				indiceColumnaFinal);

		row.getSheet().addMergedRegion(cellRange);
	}

	public static final void combinarCeldasDeFilaConColumna(Row row,int indiceFilaInicial, int indiceFilaFinal, int indiceColumnaInicial, int indiceColumnaFinal) {

		final CellRangeAddress cellRange = new CellRangeAddress(
				indiceFilaInicial,
				indiceFilaFinal,
				indiceColumnaInicial,
				indiceColumnaFinal);

		row.getSheet().addMergedRegion(cellRange);
	}

	/**
	 * Los parámetros reciben el número de columnas o filas, no sus índices.
	 *
	 * Si lo que se tiene a mano son los índices, se debe recordar que los índices están expresados
	 * en Base 0, por lo que hay que sumar 1 para obtener el número en Base 1 que requiere este método.
	 */
	public static final void congelar(final Sheet sheet, final int numeroColumnas, final int numeroFilas) {
		sheet.createFreezePane(numeroColumnas, numeroFilas);
	}

	public static Cell crearCeldaConEstilo(Row row, int indiceColumna, CellStyle estilo) {
		final Cell cell = row.createCell(indiceColumna);
		cell.setCellStyle(estilo);
		return cell;
	}

	public static final Cell crearCeldaConEstilo(Row row, int indiceColumna, CellStyle estilo, BigDecimal valor) {
		final Cell cell = crearCeldaConEstilo(row, indiceColumna, estilo);
		cell.setCellValue(valor.floatValue());
		return cell;
	}

    public static final Cell crearCeldaConEstilo(Row row, int indiceColumna, CellStyle estilo, Double valor) {
        final Cell cell = crearCeldaConEstilo(row, indiceColumna, estilo);
        cell.setCellValue(valor.floatValue());
        return cell;
    }

	public static final Cell crearCeldaConEstilo(final Row row, final int indiceColumna, final CellStyle estilo, final Date valor) {
		final Cell cell = crearCeldaConEstilo(row, indiceColumna, estilo);
		cell.setCellValue(valor);
		return cell;
	}

	public static final Cell crearCeldaConEstilo(Row row, int indiceColumna, CellStyle estilo, long valor) {
		final Cell cell = crearCeldaConEstilo(row, indiceColumna, estilo);
		cell.setCellValue(valor);
		return cell;
	}

	public static final Cell crearCeldaConEstilo(Row row, int indiceColumna, CellStyle estilo, String valor) {
		final Cell cell = crearCeldaConEstilo(row, indiceColumna, estilo);
		cell.setCellValue(valor);
		return cell;
	}

	public static final CellStyle crearEstiloFormatoMonto(Workbook workbook) {

		final CellStyle cellStyle = workbook.createCellStyle();

		final DataFormat dataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(dataFormat.getFormat("0.00"));

		return cellStyle;
	}

	public static final BigDecimal leerBigDecimal(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return new BigDecimal(cell.getNumericCellValue());
			case STRING:
				return new BigDecimal(cell.getStringCellValue());
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	public static final Date leerDate(Row row, int indiceColumna, boolean convertirVacioANull) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {

			case NUMERIC:

				final double valorNumerico = cell.getNumericCellValue();

				if (valorNumerico != 0) {
					return cell.getDateCellValue();
				}

				break;
			}
		}

		return null;
	}

	public static final long leerLong(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return (long)cell.getNumericCellValue();
			case STRING:
				return Long.parseLong(cell.getStringCellValue());
			default:
				return -1;
			}
		} else {
			return -1;
		}
	}

	public static final short leerShort(Row row, int indiceColumna) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case NUMERIC:
				return (short)cell.getNumericCellValue();
			case STRING:
				return Short.parseShort(cell.getStringCellValue());
			default:
				return -1;
			}
		} else {
			return -1;
		}
	}

	public static final String leerString(Row row, int indiceColumna, boolean convertirVacioANull) {

		final Cell cell = row.getCell(indiceColumna);

		if (cell != null) {
			switch (cell.getCellTypeEnum()) {
			case STRING:
				final String valor = cell.getStringCellValue();
				if (noEsNuloNiBlanco(valor)) {
					return valor.trim();
				} else {
					if (convertirVacioANull) {
						return null;
					} else {
						return valor;
					}
				}
			case NUMERIC:
				return String.valueOf((int)cell.getNumericCellValue());
			}
		}

		return null;
	}

	public static final InputStream obtenerInputStream(Workbook workbook) throws IOException {
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			workbook.write(output);
			return new ByteArrayInputStream(output.toByteArray());
		}
	}

	private UtilidadesPoi() {}
}
