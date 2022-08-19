package com.isacore.quality.dto;

public class ProductPropertyDTO {

	private Integer idProduct;
	
	private String idPropertyList;
	
	private String nameProperty;

	private String action;
	
	private Double minProperty;
	
	private Double maxProperty;
	
	private String unitProperty;
	
	private String propertyNorm;
	
	private String asUser;
	
	private String typeProperty;
	
	private String viewProperty;

	public Integer getIdProduct() {
		return idProduct;
	}
	
	public ProductPropertyDTO() {}
	
	public ProductPropertyDTO(Integer idProduct, String idPropertyList, String nameProperty, Double min, Double max, String unit, String norm, String asUser,String type, String viewProperty) {
		this.idProduct = idProduct;
		this.idPropertyList = idPropertyList;
		this.nameProperty = nameProperty;
		this.minProperty = min;
		this.maxProperty = max;
		this.unitProperty = unit;
		this.propertyNorm = norm;
		this.asUser = asUser;
		this.typeProperty = type;
		this.viewProperty = viewProperty;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	public String getIdPropertyList() {
		return idPropertyList;
	}

	public void setIdPropertyList(String idPropertyList) {
		this.idPropertyList = idPropertyList;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double getMinProperty() {
		return minProperty;
	}

	public void setMinProperty(Double minProperty) {
		this.minProperty = minProperty;
	}

	public Double getMaxProperty() {
		return maxProperty;
	}

	public void setMaxProperty(Double maxProperty) {
		this.maxProperty = maxProperty;
	}

	public String getUnitProperty() {
		return unitProperty;
	}

	public void setUnitProperty(String unitProperty) {
		this.unitProperty = unitProperty;
	}

	public String getPropertyNorm() {
		return propertyNorm;
	}

	public void setPropertyNorm(String propertyNorm) {
		this.propertyNorm = propertyNorm;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
	}

	public String getTypeProperty() {
		return typeProperty;
	}

	public void setTypeProperty(String typeProperty) {
		this.typeProperty = typeProperty;
	}

	public String getViewProperty() {
		return viewProperty;
	}

	public void setViewProperty(String viewProperty) {
		this.viewProperty = viewProperty;
	}
	
	public String getNameProperty() {
		return nameProperty;
	}

	public void setNameProperty(String nameProperty) {
		this.nameProperty = nameProperty;
	}

	@Override
	public String toString() {
		return " [idProduct=" + idProduct + ", idPropertyList=" + idPropertyList + ", nameProperty="
				+ nameProperty + ", action=" + action + ", minProperty=" + minProperty + ", maxProperty=" + maxProperty
				+ ", unitProperty=" + unitProperty + ", propertyNorm=" + propertyNorm + ", asUser=" + asUser
				+ ", typeProperty=" + typeProperty + ", viewProperty=" + viewProperty + "]";
	}	
	
 
}
