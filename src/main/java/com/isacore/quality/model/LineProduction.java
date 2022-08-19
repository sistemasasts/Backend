package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "lineproduction")
@Table(name = "LINE_PRODUCTION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LineProduction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LP_ID")
	private Integer idLineP;
	
	@Column(name = "LP_NAME", nullable = false, length = 512)
	private String lineName;
	
	@Column(name = "LP_DESCRIPTION", nullable = true, length = 2048)
	private String lineDescription;
	
	
	public LineProduction(Integer idLineP) {
		super();
		this.idLineP = idLineP;
	}

	public LineProduction() {
		super();
	}

	public Integer getIdLineP() {
		return idLineP;
	}

	public void setIdLineP(Integer idLineP) {
		this.idLineP = idLineP;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineDescription() {
		return lineDescription;
	}

	public void setLineDescription(String lineDescription) {
		this.lineDescription = lineDescription;
	}
	
	
	
}
