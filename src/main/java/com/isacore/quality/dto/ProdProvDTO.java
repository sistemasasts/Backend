package com.isacore.quality.dto;

import com.isacore.quality.model.ProviderStatus;

public class ProdProvDTO {

	private Integer idProduct;
	
	private Integer idProvider;
	
	private String nameProvider;

	private String action;
	
	private String asUser;
	
	private ProviderStatus status;
	
	private String typeProv;
	

	public ProdProvDTO(Integer idProduct, Integer idProvider, String nameProvider, String asUser,
			ProviderStatus status, String typeProv) {
		this.idProduct = idProduct;
		this.idProvider = idProvider;
		this.nameProvider = nameProvider;
		this.asUser = asUser;
		this.status = status;
		this.typeProv = typeProv;
	}

	public Integer getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	public Integer getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(Integer idProvider) {
		this.idProvider = idProvider;
	}

	public String getNameProvider() {
		return nameProvider;
	}

	public void setNameProvider(String nameProvider) {
		this.nameProvider = nameProvider;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProdProvDTO [idProduct=");
		builder.append(idProduct);
		builder.append(", idProvider=");
		builder.append(idProvider);
		builder.append(", nameProvider=");
		builder.append(nameProvider);
		builder.append(", action=");
		builder.append(action);
		builder.append(", asUser=");
		builder.append(asUser);
		builder.append(", status=");
		builder.append(status);
		builder.append(", typeProv=");
		builder.append(typeProv);
		builder.append("]");
		return builder.toString();
	}
		
	
}
