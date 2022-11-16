/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isacore.util.reportes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author root
 */
public class EstructuraReporte implements Serializable{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
    //public static final String CARGADO= "Cargado";
    //public static final String ELIMINADO= "Eliminado";



    /**
     * Nombre reportes.
     */

    public static final String NOMBRE_REPORTE_PROFESORES_CARGADOS = "Profesores_cargados";
    public static final String NOMBRE_REPORTE_PROFESORES_ELIMINADOS = "Profesores_eliminados";
    public static final String NOMBRE_REPORTE_PROFESORES_NO_ENCONTRADOS = "No_encontrados";

    public static final String NOMBRE_REPORTE_CENTRO_COSTOS = "Centros_de_costo";

    /**
     * Métodos para generar reporte de Carga académica.
     */
    public static final ArrayList<Integer> obtenerFilasNumericasReporteCargaAcademica() {
     ArrayList<Integer> lista = new ArrayList<>();
     lista.add(2);
     return lista;
     }
    public static final List<String> obtenerEncabezadoReporteCargaAcademica() {
        List<String> lista = new ArrayList<>();
        lista.add("Código banner");
        lista.add("Nombre del profesor");
        lista.add("Créditos");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReporteCargaAcademica() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 60);
        anchos.put(2, 256 * 20);
        return anchos;
    }

    /**
     * =================================================================================================
     * */


    /**
     * Métodos para generar reporte de Centros de costo.
     */
    /**public static final ArrayList<Integer> obtenerFilasNumericasReporteCentrosCosto() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(6);
        lista.add(7);
        lista.add(8);
        return lista;
    }**/

    public static final List<String> obtenerEncabezadoReporteCentroCosto() {
        List<String> lista = new ArrayList<>();
        lista.add("Código Carrera");
        lista.add("Nombre");
        lista.add("Código Analítica Carrera");
        lista.add("Nombre Analítica Carrera");
        lista.add("Código Analítica Proyecto");
        lista.add("Estado");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReporteCentroCosto() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 40);
        anchos.put(2, 256 * 20);
        anchos.put(3, 256 * 60);
        anchos.put(4, 256 * 30);
        anchos.put(5, 256 * 20);
        return anchos;
    }

    /**
     * Metodos para reporte pagos
     */
    public static final List<String> obtenerEncabezadoReportePagosFuncionarios() {
        List<String> lista = new ArrayList<>();
        lista.add("Código Banner");
        lista.add("Id. Proveedor");
        lista.add("Nombre Proveedor");
        lista.add("Tipo");
        lista.add("Fecha del documento");
        lista.add("Número de documento");
        lista.add("Número de lote");
        lista.add("Periodo Acad.");
        lista.add("Monto trans. actual");
        lista.add("% Retención");
        lista.add("Monto del documento");
        lista.add("Pagar");
        lista.add("Saldo");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReportePagosFuncionarios() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 30);
        anchos.put(2, 256 * 40);
        anchos.put(3, 256 * 10);
        anchos.put(4, 256 * 20);
        anchos.put(5, 256 * 20);
        anchos.put(6, 256 * 30);
        anchos.put(7, 256 * 30);
        anchos.put(8, 256 * 30);
        anchos.put(9, 256 * 30);
        anchos.put(10, 256 * 30);
        anchos.put(11, 256 * 30);
        anchos.put(12, 256 * 30);
        return anchos;
    }

    public static final ArrayList<Integer> obtenerFilasNumericasReportePagosFuncionarios() {
     ArrayList<Integer> lista = new ArrayList<>();
     lista.add(8);
     lista.add(9);
     lista.add(10);
     lista.add(11);
     lista.add(12);
     return lista;
     }

    /**public static final ArrayList<Integer> obtenerFilasFechaReportePagosFuncionarios() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(4);
        return lista;
    }**/

    /**
     * Metodos para reporte ordenes compra
     */
    public static final List<String> obtenerEncabezadoReporteOrdenesCompra() {
        List<String> lista = new ArrayList<>();
        lista.add("ORDEN DE COMPRA");
        lista.add("CODIGO");
        lista.add("NOMBRE");
        lista.add("ESTADO");
        lista.add("CENTRO DE COSTO");
        lista.add("DESC. CENTRO DE COSTO");
        lista.add("CARGA");
        lista.add("SEMESTRE");
        lista.add("CUOTAS");
        lista.add("CUOTA PAGADA");
        lista.add("CUOTA PAGADA GP");
        lista.add("VALOR");
        lista.add("RETENCIÓN");
        lista.add("A PAGAR");
        lista.add("N. DOC. GP");
        lista.add("N. FACT.");
        lista.add("VAL. FACT");
        lista.add("FECHA ING.");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReporteOrdenesCompra() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 20);
        anchos.put(2, 256 * 50);
        anchos.put(3, 256 * 20);
        anchos.put(4, 256 * 20);
        anchos.put(5, 256 * 50);
        anchos.put(6, 256 * 20);
        anchos.put(7, 256 * 20);
        anchos.put(8, 256 * 20);
        anchos.put(9, 256 * 20);
        anchos.put(10, 256 * 20);
        anchos.put(11, 256 * 20);
        anchos.put(12, 256 * 20);
        anchos.put(13, 256 * 20);
        anchos.put(14, 256 * 20);
        anchos.put(15, 256 * 20);
        anchos.put(16, 256 * 20);
        anchos.put(17, 256 * 20);
        return anchos;
    }

    public static final ArrayList<Integer> obtenerFilasNumericasReporteOrdenesCompra() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(8);
        lista.add(11);
        lista.add(12);
        lista.add(13);
        lista.add(16);
        return lista;
    }

    public static final List<String> obtenerEncabezadoReporteOrdenesCompraNrcs() {
        List<String> lista = new ArrayList<>();
        lista.add("ORDEN DE COMPRA");
        lista.add("CÓDIGO");
        lista.add("NOMBRE");
        lista.add("ESTADO");
        lista.add("NRC");
        lista.add("CENTRO DE COSTO");
        lista.add("DESC. CENTRO DE COSTO");
        lista.add("VALOR FACTURA");
        lista.add("N. FACT.");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReporteOrdenesCompraNrcs() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 20);
        anchos.put(2, 256 * 50);
        anchos.put(3, 256 * 20);
        anchos.put(4, 256 * 20);
        anchos.put(5, 256 * 20);
        anchos.put(6, 256 * 50);
        anchos.put(7, 256 * 20);
        anchos.put(8, 256 * 20);
        return anchos;
    }

    public static final ArrayList<Integer> obtenerFilasNumericasReporteOrdenesCompraNrcs() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(7);
        return lista;
    }

    /**
     * Metodos para reporte de descuentos
     */
    public static final List<String> obtenerEncabezadoReporteDescuentos() {
        List<String> lista = new ArrayList<>();
        lista.add("CODIGO");
        lista.add("IDENTIFICACIÓN");
        lista.add("NOMBRE");
        lista.add("TIPO DESC.");
        lista.add("FECHA DESC.");
        lista.add("ESTADO");
        lista.add("N. CUOTA");
        lista.add("APLICADA");
        lista.add("FECHA APLICACIÓN");
        lista.add("NUMERO DOCUMENTO GP");
        lista.add("VALOR CUOTA");
        lista.add("VALOR TOTAL");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReporteDescuento() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 20);
        anchos.put(2, 256 * 50);
        anchos.put(3, 256 * 20);
        anchos.put(4, 256 * 20);
        anchos.put(5, 256 * 20);
        anchos.put(6, 256 * 20);
        anchos.put(7, 256 * 20);
        anchos.put(8, 256 * 20);
        anchos.put(9, 256 * 20);
        anchos.put(10, 256 * 20);
        anchos.put(11, 256 * 20);
        return anchos;
    }

    public static final ArrayList<Integer> obtenerFilasNumericasReporteDescuento() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(10);
        lista.add(11);
        return lista;
    }

    /**
     * Metodos para reporte de facturas
     */
    public static final List<String> obtenerEncabezadoReporteFacturas() {
        List<String> lista = new ArrayList<>();
        lista.add("Código Banner");
        lista.add("Identificación Proveedor");
        lista.add("Nombre Proveedor");
        lista.add("Fecha");
        lista.add("Número GP");
        lista.add("Número Factura");
        lista.add("Lote");
        lista.add("Número OC");
        lista.add("Valor");
        lista.add("% Retención");
        lista.add("Total");
        lista.add("Estado");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReporteFacturas() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 20);
        anchos.put(2, 256 * 60);
        anchos.put(3, 256 * 20);
        anchos.put(4, 256 * 25);
        anchos.put(5, 256 * 25);
        anchos.put(6, 256 * 25);
        anchos.put(7, 256 * 25);
        anchos.put(8, 256 * 20);
        anchos.put(9, 256 * 20);
        anchos.put(10, 256 * 20);
        anchos.put(11, 256 * 30);
        return anchos;
    }

    public static final ArrayList<Integer> obtenerFilasNumericasReporteFacturas() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(8);
        lista.add(9);
        lista.add(10);
        return lista;
    }


    /**
     * Metodos para reporte de solicitudes de pago
     */
    public static final List<String> obtenerEncabezadoReporteSolicitudes() {
        List<String> lista = new ArrayList<>();
        lista.add("Solicitud");
        lista.add("Fecha solicitud");
        lista.add("Días transcurridos");
        lista.add("Solicitado para");
        lista.add("Código Banner");
        lista.add("Identificación");
        lista.add("Período");
        lista.add("Nivel académico");
        lista.add("Carrera");
        lista.add("Orden de compra");
        lista.add("Número de factura");
        lista.add("Valor factura");
        lista.add("Estado");
        lista.add("Usuario revisor");
        lista.add("Usuario contabilidad");
        lista.add("Usuario RR.HH");
        return lista;
    }

    public static final Map<Integer, Integer> configurarAnchosDeCeldaReporteSolicitudes() {
        Map<Integer, Integer> anchos = new HashMap<>();
        anchos.put(0, 256 * 20);
        anchos.put(1, 256 * 20);
        anchos.put(2, 256 * 20);
        anchos.put(3, 256 * 60);
        anchos.put(4, 256 * 20);
        anchos.put(5, 256 * 25);
        anchos.put(6, 256 * 20);
        anchos.put(7, 256 * 25);
        anchos.put(8, 256 * 60);
        anchos.put(9, 256 * 25);
        anchos.put(10, 256 * 25);
        anchos.put(11, 256 * 20);
        anchos.put(12, 256 * 50);
        anchos.put(13, 256 * 60);
        anchos.put(14, 256 * 60);
        anchos.put(15, 256 * 60);
        return anchos;
    }

    public static final ArrayList<Integer> obtenerFilasNumericasReporteSolicitudes() {
        ArrayList<Integer> lista = new ArrayList<>();
        lista.add(2);
        lista.add(11);
        return lista;
    }

}
