package com.isacore.sgc.acta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "committee")
@Table(name = "COMMITTEE")
public class Committee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COM_IDCOMMITTEE")
	private Integer idCommittee;
	
	@Column(name = "COM_NAME", nullable = false, length = 256)
	private String name;
	
	@Column(name = "COM_DESCRIPTION", nullable = true, length = 256)
	private String description;

	public Integer getIdCommittee() {
		return idCommittee;
	}

	public void setIdCommittee(Integer idCommittee) {
		this.idCommittee = idCommittee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
