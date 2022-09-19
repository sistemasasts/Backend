package com.isacore.util;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CatalogDTO implements Serializable{

	private String label;
	
	private String value;

	private Object adicional;

	public CatalogDTO(String text, String valor) {
		super();
		this.label = text;
		this.value = valor;
	}

	public CatalogDTO(String text, String valor, Object adicional) {
		super();
		this.label = text;
		this.value = valor;
		this.adicional = adicional;
	}

	public String getLabel() {
		return label;
	}

	public String getValue() {
		return value;
	}

	public Object getAdicional() {
		return adicional;
	}
}
