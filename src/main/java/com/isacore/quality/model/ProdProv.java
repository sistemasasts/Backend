package com.isacore.quality.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.isacore.localdate.converter.LocalDateTimeConverter;

@Entity(name = "prodprov")
@IdClass(ProdProvPK.class)
@Table(name = "PROD_PROV")
public class ProdProv {

	@Id
	private Product product;
	
	@Id
	private Provider provider;
	
	@Column(name = "PP_STATUS", nullable = true, length = 32)
	@Enumerated(EnumType.STRING)
	private ProviderStatus status;
	
	@Column(name = "PP_Type", nullable = true, length = 32)
	private String typeProv;
	
	@Column(name = "PP_ASUSER", nullable = true, length = 64)
	private String asUser;
	
	@Column(name = "PP_UPDATE", nullable = true)
	@Convert(converter = LocalDateTimeConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDateTime dateUpdate;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public ProviderStatus getStatus() {
		return status;
	}

	public void setStatus(ProviderStatus status) {
		this.status = status;
	}

	public String getTypeProv() {
		return typeProv;
	}

	public void setTypeProv(String typeProv) {
		this.typeProv = typeProv;
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
	
}
