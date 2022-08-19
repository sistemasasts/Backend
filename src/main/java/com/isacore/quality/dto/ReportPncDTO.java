package com.isacore.quality.dto;

import java.time.LocalDate;
import java.util.List;

import com.isacore.quality.model.ConcessionRequest;
import com.isacore.quality.model.ExitMaterialHistory;

public class ReportPncDTO {
	
	private String nombreProducto;
	
	private double cantidad;
	
	private String unidad;
	
	private Integer Id;
	
	private LocalDate fechaProduccion;
	
	private LocalDate fechaDeteccion;
	
	private String lote;
	
	private String entradaTraspaso;
	
	private String areaInvolucrada;
	
	private double pesoTotalNC;
	
	private String defectos;
	
	private String observaciones5m;
	
	private String elaboradoPor;
	
	private double valdidezPorcentaje;
	
	private String ordenProduccion;
	
	private String procedenciaProducto;
	
		
	public String getProcedenciaProducto() {
		if(procedenciaProducto.equals("PT"))
		return "x";
		else
		return "-";
	}
	
	public String getProcedenciaProductoPP() {
		if(procedenciaProducto.equals("PP"))
		return "x";
		else
		return "-";
	}

	public void setProcedenciaProducto(String procedenciaProducto) {
		this.procedenciaProducto = procedenciaProducto;
	}

	public String getOrdenProduccion() {
		return ordenProduccion;
	}

	public void setOrdenProduccion(String ordenProduccion) {
		this.ordenProduccion = ordenProduccion;
	}

	public double getValdidezPorcentaje() {
		return valdidezPorcentaje;
	}

	public void setValdidezPorcentaje(double valdidezPorcentaje) {
		this.valdidezPorcentaje = valdidezPorcentaje;
	}

	private List<ExitMaterialHistory> listaSalidaMaterial;
	
	private ConcessionRequest salidaConcesion;

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public LocalDate getFechaProduccion() {
		return fechaProduccion;
	}

	public void setFechaProduccion(LocalDate fechaProduccion) {
		this.fechaProduccion = fechaProduccion;
	}

	public LocalDate getFechaDeteccion() {
		return fechaDeteccion;
	}

	public void setFechaDeteccion(LocalDate fechaDeteccion) {
		this.fechaDeteccion = fechaDeteccion;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getEntradaTraspaso() {
		return entradaTraspaso;
	}

	public void setEntradaTraspaso(String entradaTraspaso) {
		this.entradaTraspaso = entradaTraspaso;
	}

	public String getAreaInvolucrada() {
		return areaInvolucrada;
	}

	public void setAreaInvolucrada(String areaInvolucrada) {
		this.areaInvolucrada = areaInvolucrada;
	}

	public double getPesoTotalNC() {
		return pesoTotalNC;
	}

	public void setPesoTotalNC(double pesoTotalNC) {
		this.pesoTotalNC = pesoTotalNC;
	}

	public String getDefectos() {
		return defectos;
	}

	public void setDefectos(String defectos) {
		this.defectos = defectos;
	}

	public String getObservaciones5m() {
		return observaciones5m;
	}

	public void setObservaciones5m(String observaciones5m) {
		this.observaciones5m = observaciones5m;
	}

	public String getElaboradoPor() {
		return elaboradoPor;
	}

	public void setElaboradoPor(String elaboradoPor) {
		this.elaboradoPor = elaboradoPor;
	}

	public List<ExitMaterialHistory> getListaSalidaMaterial() {
		return listaSalidaMaterial;
	}

	public void setListaSalidaMaterial(List<ExitMaterialHistory> listaSalidaMaterial) {
		this.listaSalidaMaterial = listaSalidaMaterial;
	}

	public ConcessionRequest getSalidaConcesion() {
		return salidaConcesion;
	}

	public void setSalidaConcesion(ConcessionRequest salidaConcesion) {
		this.salidaConcesion = salidaConcesion;
	}

}
