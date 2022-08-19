package com.isacore.sgc.acta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "diary")
@Table(name = "DIARY")
public class Diary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DIA_IDDIARY", nullable = false)
	private int idDiary;
	
	@Column(name = "DIA_DESCRIPTION", nullable = false, length =128)
	private String description;
	
	@Column(name = "DIA_EXECUTED")
	private Boolean executed;

	public int getIdDiary() {
		return idDiary;
	}

	public void setIdDiary(int idDiary) {
		this.idDiary = idDiary;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getExecuted() {
		return executed;
	}

	public void setExecuted(Boolean executed) {
		this.executed = executed;
	}
	
	
	
}
