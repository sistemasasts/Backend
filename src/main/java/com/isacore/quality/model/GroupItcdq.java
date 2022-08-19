package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "groupitcdq")
@Table(name = "GROUP_ITCDQ")
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GroupItcdq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GRI_ID")
	private Integer idGroup;
	
	@Column(name = "GRI_NAME", nullable = false, length = 128)
	private String nameGroup;
	
	@Column(name = "GRI_DESCRIPTION", nullable = true, length = 128)
	private String descGroup;

	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public String getNameGroup() {
		return nameGroup;
	}

	public void setNameGroup(String nameGroup) {
		this.nameGroup = nameGroup;
	}

	public String getDescGroup() {
		return descGroup;
	}

	public void setDescGroup(String descGroup) {
		this.descGroup = descGroup;
	}	
	
}
