package com.isacore.quality.exception;

@SuppressWarnings("serial")
public class ConfigFormEntryPropertyExistsException extends QualityException {

	private String nameProperty;
	private String productTypeText;
	
	public ConfigFormEntryPropertyExistsException(String nameProperty, String productTypeText) {
		super();
		this.nameProperty = nameProperty;
		this.productTypeText = productTypeText;
	}


	@Override
	public String getMessage() {
		return String.format("La propiedad %s ya existe para el grupo %s", nameProperty, productTypeText);
	}
}
