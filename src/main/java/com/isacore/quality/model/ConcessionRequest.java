package com.isacore.quality.model;

import java.time.LocalDate;

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

@Entity(name = "concessionRequest")
@Table(name = "CONCESSION_REQUEST")
public class ConcessionRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CR_ID")
	private Integer idCR;
	
	@Column(name = "REP_TITLE", nullable = true, length = 2048)
	private String repTitle;
	
	@Column(name = "REP_SUBTITLE", nullable = true, length = 2048)
	private String repSubtitle;
	
	@Column(name = "REP_REFERENCE", nullable = true, length = 2048)
	private String repReference;
	
	@Column(name = "REP_REVIEW", nullable = true, length = 64)
	private String repReview;
	
	@Column(name = "REP_REGISTER", nullable = true, length = 64)
	private String repRegister;
	
	@Column(name = "REP_CODE", nullable = true, length = 64)
	private String repCode;
	
	@Column(name = "CR_DATE", nullable = false)
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate date;
	
	@Column(name = "CR_ID_MATERIAL_PNC01", nullable = true, length = 256)
	private String codeMaterialPnc01;
	
	@Column(name = "CR_CLIENT", nullable = true, length = 256)
	private String client;
	
	@Column(name = "CR_SELLER", nullable = true, length = 256)
	private String seller;
	
	@Column(name = "CR_PERCENT_VALIDITY", nullable = true, columnDefinition="decimal(10,2)")
	private Double percentValidity;
	
	@Column(name = "CR_QUANTITY", nullable = false, columnDefinition="decimal(10,2)")
	private Double quantity;
	
	@Column(name = "CR_NRO_INVOICE", nullable = true, length = 256)
	private String nroInvoice;
	
	@Column(name = "CR_CONDITION_MATERIAL", nullable = true, length = 2048)
	private String conditionMaterial;
	
	@Column(name = "CR_COMMENT", nullable = true, length = 256)
	private String comment;
	

	
	public Integer getIdCR() {
		return idCR;
	}

	public void setIdCR(Integer idCR) {
		this.idCR = idCR;
	}

	public String getRepTitle() {
		return repTitle;
	}

	public void setRepTitle(String repTitle) {
		this.repTitle = repTitle;
	}

	public String getRepSubtitle() {
		return repSubtitle;
	}

	public void setRepSubtitle(String repSubtitle) {
		this.repSubtitle = repSubtitle;
	}

	public String getRepReference() {
		return repReference;
	}

	public void setRepReference(String repReference) {
		this.repReference = repReference;
	}

	public String getRepReview() {
		return repReview;
	}

	public void setRepReview(String repReview) {
		this.repReview = repReview;
	}

	public String getRepRegister() {
		return repRegister;
	}

	public void setRepRegister(String repRegister) {
		this.repRegister = repRegister;
	}

	public String getRepCode() {
		return repCode;
	}

	public void setRepCode(String repCode) {
		this.repCode = repCode;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getNroInvoice() {
		return nroInvoice;
	}

	public void setNroInvoice(String nroInvoice) {
		this.nroInvoice = nroInvoice;
	}

	public String getConditionMaterial() {
		return conditionMaterial;
	}

	public void setConditionMaterial(String conditionMaterial) {
		this.conditionMaterial = conditionMaterial;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public Double getPercentValidity() {
		return percentValidity;
	}

	public void setPercentValidity(Double percentValidity) {
		this.percentValidity = percentValidity;
	}

	public String getCodeMaterialPnc01() {
		return codeMaterialPnc01;
	}

	public void setCodeMaterialPnc01(String codeMaterialPnc01) {
		this.codeMaterialPnc01 = codeMaterialPnc01;
	}
	
	
}
