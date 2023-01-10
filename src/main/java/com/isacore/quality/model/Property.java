package com.isacore.quality.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.localdate.converter.LocalDateTimeConverter;
import com.isacore.util.PropertyText;

@Entity(name = "property")
@IdClass(PropertyPK.class)
@Table(name = "PROPERTY")
public class Property {

	/*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	private Product product;
	*/

    @Id
    private Product product;

    @Id
    private PropertyList propertyList;


    @Column(name = "PROPERTY_MIN", nullable = true, columnDefinition = "decimal(9,3)")
    private Double minProperty;

    @Column(name = "PROPERTY_MAX", nullable = true, columnDefinition = "decimal(9,3)")
    private Double maxProperty;

    @Column(name = "PROPERTY_UNIT", nullable = true, length = 64)
    private String unitProperty;

    @ManyToOne(fetch = FetchType.EAGER)
    private UnidadMedida unit;

    @Column(name = "PROPERTY_VIEW", nullable = true, columnDefinition = "varchar(Max)")
    private String viewProperty;

    @Column(name = "PROPERTY_VIEW_HCC", nullable = true)
    private Boolean viewPropertyOnHcc;

    @Column(name = "PROPERTY_UPDATE", nullable = false)
    @Convert(converter = LocalDateTimeConverter.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime dateUpdate;

    @Column(name = "PROPERTY_UPDATE_EXCEL", nullable = true)
    @Convert(converter = LocalDateConverter.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDate dateUpdateExcel;

    @Column(name = "PROPERTY_TYPE", nullable = true, length = 32)
    private String typeProperty;

    @Column(name = "PROPERTY_ASUSER", nullable = true, length = 64)
    private String asUser;

    @Column(name = "PROPERTY_NORM", nullable = true, length = 1024)
    private String propertyNorm;

    /*
        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    */
    public Property() {
    }

    public Property(Product product, PropertyList propertyList, UnidadMedida unit, Double minValue, Double maxValue, String norm, String typeProperty, String viewProperty, String asUser) {
        this.product = product;
        this.propertyList = propertyList;
        this.unit = unit;
        this.minProperty = minValue;
        this.maxProperty = maxValue;
        this.propertyNorm = norm;
        this.typeProperty = typeProperty;
        this.viewProperty = viewProperty;
        this.asUser = asUser;
    }

    public boolean isEmpty() {

        if (this.typeProperty.equals(PropertyText.PROP_TECHNICAL))
            return (this.minProperty == null &&
                    this.maxProperty == null)
                    ? true : false;
        else
            return (this.viewProperty.equals("-"))
                    ? true : false;
    }

    public void setPropertyListId(String idPL) {
        this.propertyList = new PropertyList();
        this.propertyList.setIdProperty(idPL);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public PropertyList getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(PropertyList propertyList) {
        this.propertyList = propertyList;
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

    public UnidadMedida getUnit() {
        return unit;
    }

    public void setUnit(UnidadMedida unit) {
        this.unit = unit;
    }

    public String getUnitProperty() {
        return this.unit != null ? this.unit.getAbreviatura() : "";
    }

    public void setUnitProperty(String unitProperty) {
        this.unitProperty = unitProperty;
    }

    public String getViewProperty() {
        return viewProperty;
    }

    public void setViewProperty(String viewProperty) {
        this.viewProperty = viewProperty;
    }

    public Boolean isViewPropertyOnHcc() {
        return viewPropertyOnHcc;
    }

    public void setViewPropertyOnHcc(Boolean viewPropertyOnHcc) {
        this.viewPropertyOnHcc = viewPropertyOnHcc;
    }

    public String getAsUser() {
        return asUser;
    }

    public void setAsUser(String asUser) {
        this.asUser = asUser;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDateTime dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getTypeProperty() {
        return typeProperty;
    }

    public void setTypeProperty(String typeProperty) {
        this.typeProperty = typeProperty;
    }

    public String getPropertyNorm() {
        //propertyNorm = getPropertyList().getNorms() == null ? "": getPropertyList().getNorms().stream().map(x ->x.getLaboratoryNorm().getName()).collect(Collectors.joining("/"));
        return propertyNorm;
    }

    public void setPropertyNorm(String propertyNorm) {
        this.propertyNorm = propertyNorm;
    }

    public LocalDate getDateUpdateExcel() {
        return dateUpdateExcel;
    }

    public void setDateUpdateExcel(LocalDate dateUpdateExcel) {
        this.dateUpdateExcel = dateUpdateExcel;
    }

    public Boolean getViewPropertyOnHcc() {
        return viewPropertyOnHcc;
    }


    public void setIdPropNorm(String s) {
        String[] data = s.split(";");
        this.propertyList = new PropertyList();
        this.propertyList.setIdProperty(data.length >= 1 ? data[0] : "");
        this.setPropertyNorm(data.length == 2 ? data[1] : "");
        System.out.println("****------------------------> " + this.propertyList.getIdProperty());
        System.out.println("****------------------------> " + this.getPropertyNorm());
    }

    @Override
    public String toString() {
        return "Property [product=" + product + ", propertyList=" + propertyList + ", minProperty=" + minProperty
                + ", maxProperty=" + maxProperty + ", unitProperty=" + unitProperty + ", viewProperty=" + viewProperty
                + ", viewPropertyOnHcc=" + viewPropertyOnHcc + ", dateUpdate=" + dateUpdate + ", typeProperty="
                + typeProperty + ", asUser=" + asUser + ", propertyNorm=" + propertyNorm + "]";
    }


}
