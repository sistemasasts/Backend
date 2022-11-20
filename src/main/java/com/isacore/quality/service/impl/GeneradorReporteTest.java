package com.isacore.quality.service.impl;

import static com.isacore.util.UtilidadesPoi.*;
import static com.isacore.util.UtilidadesFecha.*;

import com.isacore.quality.model.VistaTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@Component
public class GeneradorReporteTest {
    private static final String HOJA_DATOS_SOCIOS = "Datos Ensayos";
    private static final DecimalFormat df = new DecimalFormat("0.0000");

    private static final int INDICE_COLUMNA_FECHA = 0;
    private static final int INDICE_COLUMNA_LOTE = 1;
    private static final int INDICE_COLUMNA_PRODUCTO = 2;
    private static final int INDICE_COLUMNA_PROPIEDAD = 3;
    private static final int INDICE_COLUMNA_RESULTADO = 4;
    private static final int INDICE_COLUMNA_RESPONSABLE = 5;
    private static final int INDICE_COLUMNA_OBSERVACION = 6;
    private static final int INDICE_COLUMNA_M1_INI = 7;
    private static final int INDICE_COLUMNA_M1_END = 8;
    private static final int INDICE_COLUMNA_M2_INI = 9;
    private static final int INDICE_COLUMNA_M2_END = 10;
    private static final int INDICE_COLUMNA_M3_INI = 11;
    private static final int INDICE_COLUMNA_M3_END = 12;
    private static final int INDICE_COLUMNA_P1 = 13;
    private static final int INDICE_COLUMNA_P2 = 14;
    private static final int INDICE_COLUMNA_P3 = 15;

    private void crearCabecera(Sheet sheet, CellStyle style, final int indiceFila) {

        final Row rowCabecera = sheet.createRow(indiceFila);
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_FECHA, style, "FECHA");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_LOTE, style, "LOTE");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_PRODUCTO, style, "PRODUCTO");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_PROPIEDAD, style, "PROPIEDAD");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_RESULTADO, style, "RESULTADO");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_RESPONSABLE, style, "RESPONSABLE");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_OBSERVACION, style, "OBSERVACIÓN");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_M1_INI, style, "M1 INI");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_M1_END, style, "M1_END");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_M2_INI, style, "M2 INI");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_M2_END, style, "M2 END");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_M3_INI, style, "M3 INI");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_M3_END, style, "M3 END");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_P1, style, "P1");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_P2, style, "P2");
        crearCeldaConEstilo(rowCabecera, INDICE_COLUMNA_P3, style, "P3");
    }

    private void crearFilaDatosEnsayo(Sheet sheet, CellStyle style, final int indiceFila, VistaTest test) {

        final Row rowFilaDatoSocio = sheet.createRow(indiceFila);

        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_FECHA, style, formatear(test.getFecha(), "yyyy-MM-dd"));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_LOTE, style, test.getLote());
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_PRODUCTO, style, test.getNombreProducto());
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_PROPIEDAD, style, test.getPropiedadId());
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_RESULTADO, style, test.getResultado() == null ? "0" : df.format(test.getResultado()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_RESPONSABLE, style, test.getUsuario());
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_OBSERVACION, style, test.getComentario() == null ? "" : test.getComentario());
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_M2_INI, style, test.getM2Ini() == null ? "0" : df.format(test.getM2Ini()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_M2_END, style, test.getM2End() == null ? "0" : df.format(test.getM2End()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_M1_END, style, test.getM1End() == null ? "0" : df.format(test.getM1End()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_M3_INI, style, test.getM3Ini() == null ? "0" : df.format(test.getM3Ini()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_M3_END, style, test.getM3End() == null ? "0" : df.format(test.getM3End()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_M1_INI, style, test.getM1Ini() == null ? "0" : df.format(test.getM1Ini()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_P1, style, test.getP1() == null ? "0" : df.format(test.getP1()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_P2, style, test.getP2() == null ? "0" : df.format(test.getP2()));
        crearCeldaConEstilo(rowFilaDatoSocio, INDICE_COLUMNA_P3, style, test.getP3() == null ? "0" : df.format(test.getP3()));
    }

    private void crearHojaDatosEnsayo(Sheet sheet, CellStyle style, List<VistaTest> personasTitulares) {
        int indiceFila = -1;

        crearCabecera(sheet, style, ++indiceFila);
        for (VistaTest detalle : personasTitulares) {

            crearFilaDatosEnsayo(sheet, style, ++indiceFila, detalle);
        }
        anchoAutomatico(sheet, INDICE_COLUMNA_FECHA);
        anchoAutomatico(sheet, INDICE_COLUMNA_LOTE);
        anchoAutomatico(sheet, INDICE_COLUMNA_PRODUCTO);
        anchoAutomatico(sheet, INDICE_COLUMNA_PROPIEDAD);
        anchoAutomatico(sheet, INDICE_COLUMNA_RESULTADO);
        anchoAutomatico(sheet, INDICE_COLUMNA_RESPONSABLE);
        anchoAutomatico(sheet, INDICE_COLUMNA_OBSERVACION);
        anchoAutomatico(sheet, INDICE_COLUMNA_M1_INI);
        anchoAutomatico(sheet, INDICE_COLUMNA_M1_END);
        anchoAutomatico(sheet, INDICE_COLUMNA_M2_INI);
        anchoAutomatico(sheet, INDICE_COLUMNA_M2_END);
        anchoAutomatico(sheet, INDICE_COLUMNA_M3_INI);
        anchoAutomatico(sheet, INDICE_COLUMNA_M3_END);
        anchoAutomatico(sheet, INDICE_COLUMNA_P1);
        anchoAutomatico(sheet, INDICE_COLUMNA_P2);
        anchoAutomatico(sheet, INDICE_COLUMNA_P3);
    }

    public Workbook generar(List<VistaTest> membresias) {
        final Workbook workbook = new XSSFWorkbook();
        final CellStyle style = workbook.createCellStyle();
        crearHojaDatosEnsayo(workbook.createSheet(HOJA_DATOS_SOCIOS), style, membresias);
        return workbook;
    }
}
