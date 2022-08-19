package com.isacore.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CatalogDTO implements Serializable{

	private String label;
	
	private String value;

	public CatalogDTO(String text, String valor) {
		super();
		this.label = text;
		this.value = valor;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}
}
