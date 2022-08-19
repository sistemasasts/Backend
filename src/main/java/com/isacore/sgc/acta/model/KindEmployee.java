package com.isacore.sgc.acta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "kindEmployee")
@Table(name = "KINDEMPLOYEE")
public class KindEmployee {

	@Id
	@Column(name = "KIN_IDKINDEMPLOYEE", nullable = false, length = 64)
	private String kindEmp;
	
	@Column(name = "KIN_DESC", nullable = false, columnDefinition = "varchar(max)")
	private String desc;

	public String getKindEmp() {
		return kindEmp;
	}

	public void setKindEmp(String kindEmp) {
		this.kindEmp = kindEmp;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
