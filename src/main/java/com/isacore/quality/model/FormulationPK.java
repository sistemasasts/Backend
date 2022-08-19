package com.isacore.quality.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class FormulationPK implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "FORM_ID", nullable = false)
	private FormulaList formulationList;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "FI_ID", nullable = false)
	private FormulationItem formulationItem;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public FormulaList getFormulationList() {
		return formulationList;
	}

	public void setFormulationList(FormulaList formulationList) {
		this.formulationList = formulationList;
	}

	public FormulationItem getFormulationItem() {
		return formulationItem;
	}

	public void setFormularionItem(FormulationItem formulationItem) {
		this.formulationItem = formulationItem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formulationItem == null) ? 0 : formulationItem.hashCode());
		result = prime * result + ((formulationList == null) ? 0 : formulationList.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		FormulationPK other = (FormulationPK) obj;
		if (formulationItem == null) {
			if (other.formulationItem != null)
				return false;
		} else if (!formulationItem.equals(other.formulationItem))
			return false;
		if (formulationList == null) {
			if (other.formulationList != null)
				return false;
		} else if (!formulationList.equals(other.formulationList))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
	
	
}
