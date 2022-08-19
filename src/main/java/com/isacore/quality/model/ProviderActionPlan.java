package com.isacore.quality.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;

@Entity(name = "provider_actionplan")
@Table(name = "PROVIDER_ACTIONPLAN")
public class ProviderActionPlan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PAP_ID")
	private Integer idPrviderActionPlan;
	
	@Column(name = "PAP_DESCRIPTION", nullable = true, length = 1024)
	private String description;
	
	@Column(name = "PAP_DATE_LIMIT", nullable = true)
	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate dateLimit;
	
	@Column(name = "PAP_RESPONSABLE", nullable = true, length = 1024)
	private String responsable;

	public Integer getIdPrviderActionPlan() {
		return idPrviderActionPlan;
	}

	public void setIdPrviderActionPlan(Integer idPrviderActionPlan) {
		this.idPrviderActionPlan = idPrviderActionPlan;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDateLimit() {
		return dateLimit;
	}

	public void setDateLimit(LocalDate dateLimit) {
		this.dateLimit = dateLimit;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	
	
	
	
}
