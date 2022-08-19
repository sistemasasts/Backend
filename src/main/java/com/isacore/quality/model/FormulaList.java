package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "formulalist")
public class FormulaList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FORM_ID")
	private Integer idFormula;
	
	@Column(name = "FORM_DESCRIPTION", nullable = false, length = 2048)
	private String description;

	public Integer getIdFormula() {
		return idFormula;
	}

	public void setIdFormula(Integer idFormula) {
		this.idFormula = idFormula;
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
		result = prime * result + ((idFormula == null) ? 0 : idFormula.hashCode());
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
		FormulaList other = (FormulaList) obj;
		if (idFormula == null) {
			if (other.idFormula != null)
				return false;
		} else if (!idFormula.equals(other.idFormula))
			return false;
		return true;
	}

	
	
}
