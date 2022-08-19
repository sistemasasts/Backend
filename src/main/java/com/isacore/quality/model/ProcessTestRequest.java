package com.isacore.quality.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;

@Entity(name = "PROCESS_TEST_REQUEST")
@Table(name = "process_test_request")
public class ProcessTestRequest {

	@Id
	@Column(name = "PRT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProcessTest;

	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	@Column(name = "PRT_DELIVER_DATE", nullable = true)
	private LocalDate deliverDate;

	@Column(name = "PRT_OBJECTIVE", nullable = true, length = 2048)
	private String objective;

	@Column(name = "PRT_MATERIAL_LINE", nullable = true, length = 2048)
	private String materialLine;

	@Column(name = "PRT_PRODUCT_GETTING", nullable = true, length = 2048)
	private String productGetting;

	@Column(name = "PRT_PROCESS_VARIABLES", nullable = true, length = 2048)
	private String processVariables;

	@Column(name = "PRT_ADDITION_VERIFICATION", nullable = true, length = 2048)
	private String additionVerification;

	@Column(name = "PRT_URL_IMG", nullable = true, length = 512)
	private String urlImg;

	@Column(name = "PRT_INFORM_EXIST", nullable = true)
	private boolean informExist;

	@Column(name = "PRT_ASUSER", nullable = true)
	private String asUser;

	@Column(name = "PRT_LINE_BELONG", nullable = true, length = 512)
	private String lineBelong;

	@Column(name = "PRT_DATE_REG", nullable = true)
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime dateRegistration;

	@Column(name = "PRT_U_NAME", nullable = true)
	private String nameUser;

	@Column(name = "PRT_U_NAME_APPROVE", nullable = true, length = 512)
	private String nameUserAprove;

	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	@Column(name = "PRT_APPROVE_DATE", nullable = true)
	private LocalDate approveDate;

	@Column(name = "PRT_COMMENT", nullable = true, length = 1024)
	private String comment;

	@Transient
	private String imgBase64;

	@Transient
	private String nameImg;

	@Transient
	private String extImg;

	public Integer getIdProcessTest() {
		return idProcessTest;
	}

	public void setIdProcessTest(Integer idProcessTest) {
		this.idProcessTest = idProcessTest;
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

	public String getMaterialLine() {
		return materialLine;
	}

	public void setMaterialLine(String materialLine) {
		this.materialLine = materialLine;
	}

	public String getProductGetting() {
		return productGetting;
	}

	public void setProductGetting(String productGetting) {
		this.productGetting = productGetting;
	}

	public String getProcessVariables() {
		return processVariables;
	}

	public void setProcessVariables(String processVariables) {
		this.processVariables = processVariables;
	}

	public String getAdditionVerification() {
		return additionVerification;
	}

	public void setAdditionVerification(String additionVerification) {
		this.additionVerification = additionVerification;
	}

	public String getUrlImg() {
		return urlImg;
	}

	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}

	public boolean isInformExist() {
		return informExist;
	}

	public void setInformExist(boolean informExist) {
		this.informExist = informExist;
	}

	public String getImgBase64() {
		return imgBase64;
	}

	public void setImgBase64(String imgBase64) {
		this.imgBase64 = imgBase64;
	}

	public String getNameImg() {
		return nameImg;
	}

	public void setNameImg(String nameImg) {
		this.nameImg = nameImg;
	}

	public String getExtImg() {
		return extImg;
	}

	public void setExtImg(String extImg) {
		this.extImg = extImg;
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

	public String getNameUserAprove() {
		return nameUserAprove;
	}

	public void setNameUserAprove(String nameUserAprove) {
		this.nameUserAprove = nameUserAprove;
	}

	public LocalDate getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(LocalDate approveDate) {
		this.approveDate = approveDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLineBelong() {
		return lineBelong;
	}

	public void setLineBelong(String lineBelong) {
		this.lineBelong = lineBelong;
	}
	
}
