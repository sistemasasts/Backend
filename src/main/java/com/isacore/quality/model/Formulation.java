package com.isacore.quality.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.localdate.converter.LocalDateTimeConverter;

@Entity
@IdClass(FormulationPK.class)
@Table(name = "formulation")
@JsonInclude(Include.NON_NULL)
public class Formulation {

	@Id
	private Product product;
	
	@Id
	private FormulaList formulationList;
	
	@Id
	private FormulationItem formulationItem;
	
	@Column(name = "FORM_VALUE", nullable = false, columnDefinition = ("Decimal(10,8)"))
	private Double value;
	
	@Column(name = "FORM_TYPE", nullable = true, length = 4)
	private String type;
	
	@Column(name = "FORM_VEA", nullable = true, columnDefinition = ("Decimal(10,8)"))
	private Double vea;
	
	@Column(name = "FORM_UNIT", nullable = true, length = 8)
	private String unit;
	
	//carga
	@Column(name = "FORM_MULTIFACTOR", nullable = false)
	private Integer multiFactor;
	
	@Column(name = "FORM_UPDATE", nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDateTime dateUpdate;
	
	@Column(name = "FORM_UPDATE_EXCEL", nullable = true)
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDate dateUpdateExcel; 
	
	@Column(name = "FORM_ASUSER", nullable = true, length = 64)
	private String asUser;
	
	@Transient
	private String component;
	
	@Transient
	private Double amount;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public FormulaList getFormulationList() {
		return formulationList;
	}

	public void setFormulationList(FormulaList formulationList) {
		this.formulationList = formulationList;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getVea() {
		return vea;
	}

	public void setVea(Double vea) {
		this.vea = vea;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getMultiFactor() {
		return multiFactor;
	}

	public void setMultiFactor(Integer multiFactor) {
		this.multiFactor = multiFactor;
	}

	public FormulationItem getFormulationItem() {
		return formulationItem;
	}

	public void setFormulationItem(FormulationItem formulationItem) {
		this.formulationItem = formulationItem;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(int load) {
		this.amount = this.multiFactor * (double)load * this.value;
	}

	public LocalDateTime getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(LocalDateTime dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public LocalDate getDateUpdateExcel() {
		return dateUpdateExcel;
	}

	public void setDateUpdateExcel(LocalDate dateUpdateExcel) {
		this.dateUpdateExcel = dateUpdateExcel;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

}
