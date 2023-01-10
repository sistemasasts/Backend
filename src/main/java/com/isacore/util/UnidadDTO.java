package com.isacore.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class UnidadDTO implements Serializable{

	private String label;
	private long value;

	public UnidadDTO(String text, long valor) {
		super();
		this.label = text;
		this.value = valor;
	}
}
