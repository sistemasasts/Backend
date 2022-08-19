package com.isacore.quality.dto;

import java.io.Serializable;

import com.isacore.quality.model.PropertyPeriodicity;

@SuppressWarnings("serial")
public class ReportProductSpecificationDTO implements Serializable {

	private String name;
	
	private String unit;
	
	private String norm;
	
	private String machine;
	
	private String method;
	
	private String laboratory;
	
	private String samplingPlan;
	
	private Double min;
	
	private Double max;
	
	private String type;
	
	private String viewProperty;
	
	private PropertyPeriodicity periodicity;

	public ReportProductSpecificationDTO() {	}

	public ReportProductSpecificationDTO(String name, String unit, String norm, String machine, String method,
			String laboratory, String samplingPlan, Double min, Double max, String type, String viewProperty,
			PropertyPeriodicity periodicity) {
		super();
		this.name = name;
		this.unit = unit;
		this.norm = norm;
		this.machine = machine;
		this.method = method;
		this.laboratory = laboratory;
		this.samplingPlan = samplingPlan;
		this.min = min;
		this.max = max;
		this.type = type;
		this.viewProperty = viewProperty;
		this.periodicity = periodicity;
	}

	public String getName() {
		return name;
	}

	public String getUnit() {
		return unit;
	}

	public String getNorm() {
		return norm;
	}

	public String getMachine() {
		return machine;
	}

	public String getMethod() {
		return method;
	}

	public String getLaboratory() {
		return laboratory;
	}

	public String getSamplingPlan() {
		return samplingPlan;
	}

	public Double getMin() {
		return min;
	}

	public Double getMax() {
		return max;
	}

	public String getType() {
		return type;
	}

	public String getViewProperty() {
		return viewProperty;
	}

	public PropertyPeriodicity getPeriodicity() {
		return periodicity;
	}
	
	public String getPeriodicityCadena() {
		return periodicity == null? "": periodicity.getDescripcion();
	}
	
}
