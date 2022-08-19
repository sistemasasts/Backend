package com.isacore.quality.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ConfigurationFormTestEntryMP {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String productTypeText;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "propertyList_id", referencedColumnName = "propl_id", insertable = true, updatable = false)
	private PropertyList property;
	
	public ConfigurationFormTestEntryMP() {	}

	public ConfigurationFormTestEntryMP(String productTypeText, PropertyList property) {
		super();
		this.productTypeText = productTypeText;
		this.property = property;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductTypeText() {
		return productTypeText;
	}

	public void setProductTypeText(String productTypeText) {
		this.productTypeText = productTypeText;
	}

	public PropertyList getProperty() {
		return property;
	}

	public void setProperty(PropertyList property) {
		this.property = property;
	}
	
	
	
}
