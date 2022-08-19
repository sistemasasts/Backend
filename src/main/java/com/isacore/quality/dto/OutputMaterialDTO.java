package com.isacore.quality.dto;

import java.util.List;

import com.isacore.quality.model.ExitMaterialHistory;

public class OutputMaterialDTO {

	private List<ExitMaterialHistory> salidaMaterialHistorial;
	
	private Integer idPNC;
	
	private String nombreProducto;
	
	private String tipoProducto;
	
	private double cantidadInicial;
	
	private double cantidadStock;
	
	private String unidad;
	
	public OutputMaterialDTO() {};
	
	public OutputMaterialDTO( Integer idPNC, String nombreProducto, String tipoProducto, double cantidadInicial, double cantidadStock, String unidad , List<ExitMaterialHistory> salidaMaterialHistorial ) {
		this.idPNC= idPNC;
		this.nombreProducto = nombreProducto;
		this.tipoProducto= tipoProducto;
		this.cantidadInicial = cantidadInicial;
		this.cantidadStock = cantidadStock;
		this.unidad = unidad;
		this.salidaMaterialHistorial= salidaMaterialHistorial;
	}

	public List<ExitMaterialHistory> getSalidaMaterialHistorial() {
		return salidaMaterialHistorial;
	}
	

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public void setSalidaMaterialHistorial(List<ExitMaterialHistory> salidaMaterialHistorial) {
		this.salidaMaterialHistorial = salidaMaterialHistorial;
	}

	public Integer getIdPNC() {
		return idPNC;
	}

	public void setIdPNC(Integer idPNC) {
		this.idPNC = idPNC;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public double getCantidadInicial() {
		return cantidadInicial;
	}

	public void setCantidadInicial(double cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}

	public double getCantidadStock() {
		return cantidadStock;
	}

	public void setCantidadStock(double cantidadStock) {
		this.cantidadStock = cantidadStock;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}
	
	
}
