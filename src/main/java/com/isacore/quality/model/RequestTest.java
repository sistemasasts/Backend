package com.isacore.quality.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;

@Entity(name = "request_test")
@Table(name = "REQUEST_TEST")
public class RequestTest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "REQ_ID")
	private Integer idRequestTest;
	
	@Column(name = "REQ_PROVIDER", nullable = true)
	private String providerName;
	
	@Column(name = "REQ_PROVIDER_ID", nullable = true)
	private Integer idProvider;
	
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	@Column(name = "REQ_DELIVER_DATE", nullable = true)
	private LocalDate deliverDate;
	
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	@Column(name = "REQ_DELIVER_DATE_VALIDATION", nullable = true)
	private LocalDate deliverDateValidation;
	
	@Column(name = "REQ_OBJECTIVE", nullable = true, length=2048)
	private String objective;
	
	@Column(name = "REQ_DELIVER_TYPE", nullable = true, length=32)
	private String deliverType;
	
	@Column(name = "REQ_MATERIAL_DELIVER_DETAIL", nullable = true, length=2048)
	private String materialDetail;
	
	@Column(name = "REQ_APLICATION_LINE", nullable = true)
	private String aplicationLine;
	
	@Column(name = "REQ_USED", nullable = true, length=2048)
	private String use;
	
	@Column(name = "REQ_DELIVER_QUANTITY", nullable = true)
	private Double quantity;
	
	@Column(name = "REQ_DELIVER_UNIT", nullable = true,  length=16)
	private String unit;
	
	@Column(name = "REQ_DATA_SHEET", nullable = true)
	private Boolean dataSheet;
	
	@Column(name = "REQ_MSDS", nullable = true)
	private Boolean msds;
	
	@Column(name = "REQ_ASUSER", nullable = true)
	private String asUser;
	
	
	@Column(name = "REQ_DATE_REG", nullable = true)
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime dateRegistration;
	
	@Column(name = "REQ_U_NAME", nullable = true)
	private String nameUser;
	
	@Enumerated(EnumType.STRING)
	private PriorityLevel priorityLevel;
	
	private Boolean specialCaseJustification;
	
	/*@OneToOne(mappedBy = "request_test")
    private ProcessFlow processFlow;*/

	public Integer getIdRequestTest() {
		return idRequestTest;
	}

	public void setIdRequestTest(Integer idRequestTest) {
		this.idRequestTest = idRequestTest;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public Integer getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(Integer idProvider) {
		this.idProvider = idProvider;
	}

	public LocalDate getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(LocalDate deliverDate) {
		this.deliverDate = deliverDate;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}

	public String getMaterialDetail() {
		return materialDetail;
	}

	public void setMaterialDetail(String materialDetail) {
		this.materialDetail = materialDetail;
	}

	public String getAplicationLine() {
		return aplicationLine;
	}

	public void setAplicationLine(String aplicationLine) {
		this.aplicationLine = aplicationLine;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getDataSheet() {
		return dataSheet;
	}

	public void setDataSheet(Boolean dataSheet) {
		this.dataSheet = dataSheet;
	}

	public Boolean getMsds() {
		return msds;
	}

	public void setMsds(Boolean msds) {
		this.msds = msds;
	}

	public LocalDate getDeliverDateValidation() {
		return deliverDateValidation;
	}

	public void setDeliverDateValidation(LocalDate deliverDateValidation) {
		this.deliverDateValidation = deliverDateValidation;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
	}

	public LocalDateTime getDateRegistration() {
		return dateRegistration;
	}

	public void setDateRegistration(LocalDateTime dateRegistration) {
		this.dateRegistration = dateRegistration;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public PriorityLevel getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(PriorityLevel priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public Boolean getSpecialCaseJustification() {
		return specialCaseJustification;
	}

	public void setSpecialCaseJustification(Boolean specialCaseJustification) {
		this.specialCaseJustification = specialCaseJustification;
	}

}
