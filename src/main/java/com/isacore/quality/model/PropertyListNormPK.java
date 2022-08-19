package com.isacore.quality.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class PropertyListNormPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "laboratory_norm_id", nullable = false)
	private LaboratoryNorm laboratoryNorm;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(name = "propl_id", nullable = false)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((laboratoryNorm == null) ? 0 : laboratoryNorm.hashCode());
		result = prime * result + ((propertyList == null) ? 0 : propertyList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyListNormPK other = (PropertyListNormPK) obj;
		if (laboratoryNorm == null) {
			if (other.laboratoryNorm != null)
				return false;
		} else if (!laboratoryNorm.equals(other.laboratoryNorm))
			return false;
		if (propertyList == null) {
			if (other.propertyList != null)
				return false;
		} else if (!propertyList.equals(other.propertyList))
			return false;
		return true;
	}

	
}
