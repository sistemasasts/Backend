package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "family")
@Table(name = "FAMILY")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Family {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FAM_ID")
	private Integer familyId;
	
	@Column(name = "FAM_NAME", nullable = false, length = 1024)
	private String familyName;
	
	@Column(name = "FAM_DESCRIPTION", nullable = true, length = 2048)
	private String familyDescription;

	public Family() {
		super();
	}

	public Family(Integer familyId) {
		super();
		this.familyId = familyId;
	}

	public Family(Integer familyId, String familyName, String familyDescription) {
		super();
		this.familyId = familyId;
		this.familyName = familyName;
		this.familyDescription = familyDescription;
	}

	public Integer getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Integer familyId) {
		this.familyId = familyId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyDescription() {
		return familyDescription;
	}

	public void setFamilyDescription(String familyDescription) {
		this.familyDescription = familyDescription;
	}

	@Override
	public String toString() {
		return "Family [familyId=" + familyId + ", familyName=" + familyName + ", familyDescription="
				+ familyDescription + "]";
	}
	
	
	
}
