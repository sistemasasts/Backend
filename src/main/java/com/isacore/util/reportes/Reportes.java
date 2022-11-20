/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isacore.util.reportes;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class Reportes implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> encabezado;
    private String nombre;
    private String ruta;
    private Map<Integer, Integer> anchosDeCelda;
    private List<ArrayList<String>> datosLista;
    private ArrayList<Integer> filasNumericas;
    private ArrayList<Integer> filasFechas;
    private static String extensionExcel;
    private static String extensionArchivoTxt;

    public static String getExtensionArchivoTxt() {
        return extensionArchivoTxt;
    }

    public Reportes() {
        extensionArchivoTxt = ".txt";
        filasFechas = new ArrayList<>();
        filasNumericas = new ArrayList<>();
        anchosDeCelda = new LinkedHashMap<>();
        extensionExcel = ".xlsx";

    }

    public List<String> getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(List<String> encabezado) {
        this.encabezado = encabezado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public List<ArrayList<String>> getDatosLista() {
        return datosLista;
    }

    public void setDatosLista(List<ArrayList<String>> datosLista) {
        this.datosLista = datosLista;
    }

    public Map<Integer, Integer> getAnchosDeCelda() {
        return anchosDeCelda;
    }

    public ArrayList<Integer> getFilasFechas() {
        return filasFechas;
    }

    public void setFilasFechas(ArrayList<Integer> filasFechas) {
        this.filasFechas = filasFechas;
    }

    public void setAnchosDeCelda(Map<Integer, Integer> anchosDeCelda) {
        this.anchosDeCelda = anchosDeCelda;
    }

    public ArrayList<Integer> getFilasNumericas() {
        return filasNumericas;
    }

    public void setFilasNumericas(ArrayList<Integer> filasNumericas) {
        this.filasNumericas = filasNumericas;
    }

    public static String getExtensionExcel() {
        return extensionExcel;
    }

    public static void setExtensionExcel(String extensionExcel) {
        Reportes.extensionExcel = extensionExcel;
    }

    public static byte[] generarReporte(List<Reportes> reportes) throws IOException {
        byte[] archivo = null;
        XSSFWorkbook libro = new XSSFWorkbook();
        for (Reportes reporte : reportes) {
            if (reporte.getDatosLista() != null && reporte.getEncabezado() != null) {
                String nombre_hoja = reporte.getNombre();
//                XSSFWorkbook libro = new XSSFWorkbook();
                XSSFSheet hoja = libro.createSheet(nombre_hoja);
                Map<String, CellStyle> estilos = obtenerEstilos(libro);
                if (reporte.getAnchosDeCelda() != null && !reporte.getAnchosDeCelda().isEmpty()) {
                    hoja = configurarAnchos(hoja, reporte.getAnchosDeCelda(), reporte.getEncabezado().size());
                }
                for (int i = 0; i <= reporte.getDatosLista().size(); i++) {
                    XSSFRow row = hoja.createRow(i);// se crea las filas
                    for (int j = 0; j < reporte.getEncabezado().size(); j++) {

                        if (i == 0) {// para la cabecera
                            XSSFCell cell = row.createCell(j);// se crea las celdas para la cabecera, junto con la posición
                            cell.setCellStyle(estilos.get("cabecera")); // se añade el estilo
                            cell.setCellValue(reporte.getEncabezado().get(j));// se añade el contenido
                        } else {// para el contenido
                            XSSFCell cell = row.createCell(j);// se crea las celdas para la contenido, junto con la posición
                            if (reporte.getFilasNumericas().contains(j)) {// Si es una celda numerica, se pondra como numer
                                // y no como texto
                                BigDecimal aux = new BigDecimal(reporte.getDatosLista().get(i - 1).get(j));
                                cell.setCellValue(aux.doubleValue());
                            } else {
                                cell.setCellValue(reporte.getDatosLista().get(i - 1).get(j)); // se añade el contenido
                            }
                        }
                    }

                }
                hoja.setAutoFilter(
                        new CellRangeAddress(0, reporte.getDatosLista().size() - 1, 0, reporte.getEncabezado().size() - 1));
            }
        }
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        try {
            libro.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {

        } finally {
            fileOut.close();
        }
        archivo = fileOut.toByteArray();

        return archivo;
    }

    /**
     * Libreria con algunos estilos.
     */
    private static Map<String, CellStyle> obtenerEstilos(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();
        DataFormat df = wb.createDataFormat();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeight((short) (9.5 * 20));
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(headerFont);

        styles.put("cabecera", style);

        Font font1 = wb.createFont();
        font1.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font1);
        styles.put("cell_b", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font1);
        styles.put("cell_b_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setFont(font1);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_g", style);

        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        Font font3 = wb.createFont();
        font3.setFontHeightInPoints((short) 14);
        font3.setColor(IndexedColors.DARK_BLUE.getIndex());
        font3.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setFont(font3);
        style.setWrapText(true);
        styles.put("cell_h", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(true);
        styles.put("cell_normal", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        styles.put("cell_normal_centered", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setWrapText(true);
        style.setDataFormat(df.getFormat("d-mmm"));
        styles.put("cell_normal_date", style);

        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setIndention((short) 1);
        style.setWrapText(true);
        styles.put("cell_indented", style);

        style = createBorderedStyle(wb);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("cell_blue", style);

        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        return style;
    }

    private static XSSFSheet configurarAnchos(XSSFSheet hoja, Map<Integer, Integer> anchosDeCelda, Integer contador) {
        for (int i = 0; i < contador; i++) {
            if (anchosDeCelda.containsKey(i)) {
                hoja.setColumnWidth(i, anchosDeCelda.get(i));
            }
        }
        hoja.setZoom(90);
        return hoja;
    }

}
