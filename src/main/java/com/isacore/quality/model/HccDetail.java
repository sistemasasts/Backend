package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "hccdetail")
@Table(name = "HCCDETAIL")
public class HccDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HCCD_ID")
	private Integer idHccd;
	
	//@Column(name = "HCCH_SAPCODE", nullable = false, length = 32)
	//private String sapCode;
	
	@Column(name = "HCCD_NORM_NAME", nullable = false, length = 128)
	private String nameNorm;
	
	@Column(name = "HCCD_PROP_ID", nullable = false, length = 16)
	private String idProperty;
	
	@Column(name = "HCCD_PROP_NAME", nullable = true, length = 1024)
	private String nameProperty;
	
	@Column(name = "HCCD_PROP_TYPE", nullable = false, length = 4)
	private String typeProperty;
	
	@Column(name = "HCCD_PROP_UNIT", nullable = true, length = 64)
	private String unit;
	
	@Column(name = "HCCD_PROP_MIN", nullable = true, columnDefinition = "decimal(10,4)")
	private Double min;
	
	@Column(name = "HCCD_PROP_MAX", nullable = true, columnDefinition = "decimal(10,4)")
	private Double max;
	
	@Column(name = "HCCD_SPECIFICATIONS", nullable = true, length = 2048)
	private String specifications;
	
	@Column(name = "HCCD_PROP_VIEW", nullable = true, length = 1024)
	private String view;
	
	@Column(name = "HCCD_PROP_VIEW_ON_HCC", nullable = true)
	private Boolean viewOnHcc;
	
	@Column(name = "HCCD_TEST_RESULT", nullable = true, columnDefinition = "decimal(8,2)")
	private Double result;
	
	@Column(name = "HCCD_TEST_RESULT_VIEW", nullable = true, length = 1024)
	private String resultText;
	
	@Column(name = "HCCD_PASS_TEST", nullable = true)
	private Boolean passTest;
	
	public void generateSpecifications() {
		if(this.typeProperty.equals("T")) {	
			if(this.min == null && this.max == null)
				this.specifications = " - ";
			else {
				//if((this.min != null || this.min != 0) && (this.max == null || this.max == 0))
				if(this.min != null && this.max == null)
					this.specifications = "> " + this.min;
				else {
					//if((this.max != null || this.max != 0) && (this.min == null || this.min == 0))
					if(this.max != null && this.min == null)
						this.specifications = "< " + this.max;
					else 
						this.specifications  = this.min + " - " + this.max;
				}	
			}	
		}else {
			this.specifications = this.view;
		}
	}
	
	public void evaluateResult() {
		this.passTest = false;
		if(this.typeProperty.equals("T")) {
			//if((this.min != null || this.min != 0) && (this.max == null || this.max == 0))
			if(this.min != null && this.max == null)	
				this.passTest = this.result >= this.min ? true : false;
			else 
				if(this.max != null && this.min == null)
					this.passTest = this.result <= this.max ? true : false;
				else 
					this.passTest = (this.result >= this.min) && (this.result <= this.max) ? true : false;
		}
	}

	public Integer getIdHccd() {
		return idHccd;
	}

	public void setIdHccd(Integer idHccd) {
		this.idHccd = idHccd;
	}

	public String getNameNorm() {
		return nameNorm;
	}

	public void setNameNorm(String nameNorm) {
		this.nameNorm = nameNorm;
	}

	public String getIdProperty() {
		return idProperty;
	}

	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}

	public String getNameProperty() {
		return nameProperty;
	}

	public void setNameProperty(String nameProperty) {
		this.nameProperty = nameProperty;
	}

	public String getTypeProperty() {
		return typeProperty;
	}

	public void setTypeProperty(String typeProperty) {
		this.typeProperty = typeProperty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
/*
	public Boolean isPassTest() {
		return passTest; 
	}*/
	
	public Boolean getPassTest() {
		return passTest;
	}

	public void setPassTest(Boolean passTest) {
		this.passTest = passTest;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}
	
}
