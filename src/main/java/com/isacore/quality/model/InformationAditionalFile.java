package com.isacore.quality.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InformationAditionalFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private String description;
	
	private String nameGroupFile;
	
	private String path;
	
	private LocalDateTime dateUpload;
	
	private String asUser;

	public InformationAditionalFile() {	}

	public InformationAditionalFile(String name, String nameGroupFile, String path, String description, String asUser) {
		super();
		this.name = name;
		this.nameGroupFile = nameGroupFile;
		this.path = path;
		this.description = description;
		this.dateUpload = LocalDateTime.now();
		this.asUser = asUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameGroupFile() {
		return nameGroupFile;
	}

	public void setNameGroupFile(String nameGroupFile) {
		this.nameGroupFile = nameGroupFile;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public LocalDateTime getDateUpload() {
		return dateUpload;
	}

	public void setDateUpload(LocalDateTime dateUpload) {
		this.dateUpload = dateUpload;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
