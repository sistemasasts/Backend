package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "provider")
@Table(name = "PROVIDER")
public class Provider {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROV_ID")
	private Integer idProvider;
	
	@Column(name = "PROV_SAP_CODE", nullable = true, length = 64)
	private String sapProviderCode;
	
	@Column(name = "PROV_NAME", nullable = false, length = 2048)
	private String nameProvider;
	
	@Column(name = "PROV_DESCRIPTION", nullable = true, columnDefinition = "varchar(max)")
	private String descProvider;

	@Column(name = "PROV_TYPE", nullable = true, length = 512)
	private String typeProvider;

	public Provider() {
		super();
	}

	public Provider(Integer idProvider) {
		super();
		this.idProvider = idProvider;
	}

	public Integer getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(Integer idProvider) {
		this.idProvider = idProvider;
	}

	public String getSapProviderCode() {
		return sapProviderCode;
	}

	public void setSapProviderCode(String sapProviderCode) {
		this.sapProviderCode = sapProviderCode;
	}

	public String getNameProvider() {
		return nameProvider;
	}

	public void setNameProvider(String nameProvider) {
		this.nameProvider = nameProvider;
	}

	public String getDescProvider() {
		return descProvider;
	}

	public void setDescProvider(String descProvider) {
		this.descProvider = descProvider;
	}

	public String getTypeProvider() {
		return typeProvider;
	}

	public void setTypeProvider(String typeProvider) {
		this.typeProvider = typeProvider;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProvider == null) ? 0 : idProvider.hashCode());
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
		Provider other = (Provider) obj;
		if (idProvider == null) {
			if (other.idProvider != null)
				return false;
		} else if (!idProvider.equals(other.idProvider))
			return false;
		return true;
	}
	
	
	
}
