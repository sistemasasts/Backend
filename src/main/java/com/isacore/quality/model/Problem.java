package com.isacore.quality.model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "problem")
@Table(name = "PROBLEM")
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROBLEM_ID")
	private Integer idProblem;
	
	@Column(name = "PROBLEM_DESCRIPTION", nullable = true, length = 1024)
	private String description;
		
	@Column(name = "PROBLEM_PICTURE", nullable = true)
	private String pictureStringB64;

	@Column(name = "PROBLEM_NAME_FILE", nullable = true, length = 128)
	private String nameFileP;

	@Column(name = "PROBLEM_EXTEN_FILE", nullable = true, length = 8)
	private String extensionFileP;
	
	public Integer getIdProblem() {
		return idProblem;
	}

	public void setIdProblem(Integer idProblem) {
		this.idProblem = idProblem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPictureStringB64() {
		return pictureStringB64;
	}

	public void setPictureStringB64(String pictureStringB64) {
		this.pictureStringB64 = pictureStringB64;
	}

	public String getNameFileP() {
		return nameFileP;
	}

	public void setNameFileP(String nameFileP) {
		this.nameFileP = nameFileP;
	}

	public String getExtensionFileP() {
		return extensionFileP;
	}

	public void setExtensionFileP(String extensionFileP) {
		this.extensionFileP = extensionFileP;
	}

	
	
	
}
