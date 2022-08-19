package com.isacore.quality.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InformationAditionalFileDTO implements Serializable {
	
	private long criteriaId;
	
	private long id;
	
	private String description;	
	
	private String nameGroupFile;
	
	private String nameFile;

	private String base64;
	
	public InformationAditionalFileDTO() {	}

	public InformationAditionalFileDTO(long id, String description, String nameGroupFile, String nameFile,
			String base64) {
		super();
		this.id = id;
		this.description = description;
		this.nameGroupFile = nameGroupFile;
		this.nameFile = nameFile;
		this.base64 = base64;
	}



	public long getCriteriaId() {
		return criteriaId;
	}

	public String getDescription() {
		return description;
	}

	public String getNameGroupFile() {
		return nameGroupFile;
	}

	public String getNameFile() {
		return nameFile;
	}

	public long getId() {
		return id;
	}

	public String getBase64() {
		return base64;
	}
		
}
