package com.isacore.quality.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;

@Entity(name = "action_process")
@Table(name = "ACTION_PROCESS")
public class ActionProcess {

	@Id
	@Column(name = "ACP_ID")
	private String idactionProcess;
	
	@Column(name = "ACP_DETAIL", nullable = true)
	private String detail;
	
	@Column(name = "ACP_STATE", nullable = true)
	private String state;
	
	@Column(name = "ACP_STATE_PASS", nullable = true)
	private String statePass;
	
	@Column(name = "ACP_COMMENT_PASS", nullable = true)
	private String comment;
	
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	@Column(name = "ACP_DATE", nullable = true)
	private LocalDateTime dateAP;
	
	@OneToMany(cascade={CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "ACP_ID", nullable = false)
	private List<FileDocument> listFileDocument;
	
	/*
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDUSER", nullable = false)
	private UserImptek userImptek;*/

	@Column(name = "ACP_AS_USER", nullable = true, length=64)
	private String userImptek;
	
	@Column(name = "ACP_TIME_RESPOND", nullable = true)
	private Integer timeRespond;
	
	@Column(name = "ACP_AMOUNT_ORDER", nullable = true)
	private Double amountOrder;
	
	@Column(name = "ACP_MATERIAL_TYPE", nullable = true, length=512)
	private String materialType;
	
	@Column(name = "ACP_UNIT", nullable = true, length=16)
	private String materialUnit;
	
	
	public String getIdactionProcess() {
		return idactionProcess;
	}

	public void setIdactionProcess(String idactionProcess) {
		this.idactionProcess = idactionProcess;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatePass() {
		return statePass;
	}

	public void setStatePass(String statePass) {
		this.statePass = statePass;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getDateAP() {
		return dateAP;
	}

	public void setDateAP(LocalDateTime dateAP) {
		this.dateAP = dateAP;
	}

	public List<FileDocument> getListFileDocument() {
		return listFileDocument;
	}

	public void setListFileDocument(List<FileDocument> listFileDocument) {
		this.listFileDocument = listFileDocument;
	}

	public String getUserImptek() {
		return userImptek;
	}

	public void setUserImptek(String userImptek) {
		this.userImptek = userImptek;
	}

	public Integer getTimeRespond() {
		return timeRespond;
	}

	public void setTimeRespond(Integer timeRespond) {
		this.timeRespond = timeRespond;
	}

	public Double getAmountOrder() {
		return amountOrder;
	}

	public void setAmountOrder(Double amountOrder) {
		this.amountOrder = amountOrder;
	}

	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMaterialUnit() {
		return materialUnit;
	}

	public void setMaterialUnit(String materialUnit) {
		this.materialUnit = materialUnit;
	}
	
	
}
