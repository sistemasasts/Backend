package com.isacore.quality.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;


@Entity(name = "norm_product")
@IdClass(NormProductPK.class)
@Table(name = "NORM_PRODUCT")
public class NormProduct {

	@Id
	private Product product;
	
	@Id
	private Norm norm;
	
	@Column(name = "NP_STATE", nullable = true,length = 16 )
	private String state;
	
	@Column(name = "NP_DATE", nullable = true)
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate period;

	/*
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	*/

	public Norm getNorm() {
		return norm;
	}

	public void setNorm(Norm norm) {
		this.norm = norm;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public LocalDate getPeriod() {
		return period;
	}

	public void setPeriod(LocalDate period) {
		this.period = period;
	}

		
	
	
}
