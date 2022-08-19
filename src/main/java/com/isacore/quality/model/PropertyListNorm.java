package com.isacore.quality.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity()
@IdClass(PropertyListNormPK.class)
public class PropertyListNorm {

	@Id
	private LaboratoryNorm laboratoryNorm;
	
	@Id
	@JsonIgnore
	private PropertyList propertyList;

	public LaboratoryNorm getLaboratoryNorm() {
		return laboratoryNorm;
	}

	public void setLaboratoryNorm(LaboratoryNorm laboratoryNorm) {
		this.laboratoryNorm = laboratoryNorm;
	}

	public PropertyList getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(PropertyList propertyList) {
		this.propertyList = propertyList;
	}
	
	
}
