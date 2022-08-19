package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "formulationitem")
public class FormulationItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FI_ID")
	private Integer idFItem;
	
	@Column(name = "FI_DESCRIPTION", nullable = false, length = 256)
	private String description;

	public Integer getIdFItem() {
		return idFItem;
	}

	public void setIdFItem(Integer idFItem) {
		this.idFItem = idFItem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idFItem == null) ? 0 : idFItem.hashCode());
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
		FormulationItem other = (FormulationItem) obj;
		if (idFItem == null) {
			if (other.idFItem != null)
				return false;
		} else if (!idFItem.equals(other.idFItem))
			return false;
		return true;
	}
	
	
	
}
