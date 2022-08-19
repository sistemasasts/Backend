package com.isacore.quality.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class InformationAditional {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = true, columnDefinition = "varchar(MAX)")
	private String description;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "information_aditional_id", nullable = false)
	private List<InformationAditionalFile> detailFile = new ArrayList<>();

	@NotNull
	@Enumerated(EnumType.STRING)
	private InformationAditionalType type;
	
	public InformationAditional() {	}

	public InformationAditional(String description) {
		super();
		this.description = description;
	}
	
	public void addFile(InformationAditionalFile file) {
		this.detailFile.add(file);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<InformationAditionalFile> getDetailFile() {
		return detailFile;
	}

	public void setDetailFile(List<InformationAditionalFile> detailFile) {
		this.detailFile = detailFile;
	}

	public InformationAditionalType getType() {
		return type;
	}

	public void setType(InformationAditionalType type) {
		this.type = type;
	}
	
	
	
}
