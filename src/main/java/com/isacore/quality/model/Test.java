package com.isacore.quality.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;

@Entity(name = "test")
@Table(name = "TEST")
public class Test {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TEST_ID")
	private Integer idTest;
	
	@Column(name = "TEST_PROPERTY_CODE", nullable = true, length = 64)
	private String idProperty;
	
	@Column(name = "TEST_DATE", nullable = true)
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime dateLog;
	
	@Column(name = "TEST_TIME", nullable = true)
	@Temporal(TemporalType.TIME)
	private Date timeLog;
	
	@Column(name = "TEST_SAP_CODE", nullable = true, length = 32)
	private String sapCode;
	
	@Column(name = "TEST_PRODUCT_NAME", nullable = true, length = 512)
	private String productName;
	
	@Column(name = "TEST_BATCH", nullable = true, length = 64)
	private String batchTest;
	
	@Column(name = "TEST_MIN", nullable = true, columnDefinition = "decimal(16,6)")
	private Double min;
	
	@Column(name = "TEST_MAX", nullable = true, columnDefinition = "decimal(16,6)")
	private Double max;
	
	@Column(name = "TEST_TEMPERATURE", nullable = true, columnDefinition = "decimal(16,6)")
	private Double temperature;
	
	@Column(name = "TEST_RESULT", nullable = false, columnDefinition = "decimal(16,6)")
	private Double resultTest;
	
	@Column(name = "TEST_PASS", nullable = true)
	private Boolean passTest;
	
	@Column(name = "TEST_OWNER", nullable = true, length = 128)
	private String owner;
	
	@Column(name = "TEST_PROVIDER", nullable = true, length = 128)
	private String provider;
	
	@Column(name = "TEST_PRESENTATION", nullable = true, length = 64)
	private String presentation;
	
	@Column(name = "TEST_SPINDLE", nullable = true, length = 8)
	private String spindle;
	
	@Column(name = "TEST_SPEED_RPM", nullable = true, columnDefinition = "decimal(16,6)")
	private Double speedRPM;
	
	@Column(name = "TEST_TIME_VISCOCIDAD", nullable = true)
	private Integer timeViscocidad;
	
	@Column(name = "TEST_TORQUE", nullable = true, columnDefinition = "decimal(16,6)")
	private Double torque;
	
	@Column(name = "TEST_RES114_PROVIDER", nullable = true, length = 128)
	private String res114Provider;
	
	@Column(name = "TEST_RES114_BATCH", nullable = true, length = 64)
	private String res114Batch;
	
	@Column(name = "TEST_RESAS45_PROVIDER", nullable = true, length = 128)
	private String resAs45Provider;
	
	@Column(name = "TEST_RESAS45_BATCH", nullable = true, length = 64)
	private String resAs45Batch;
	
	@Column(name = "TEST_OPERATOR", nullable = true, length = 128)
	private String operator;
	
	@Column(name = "TEST_COLOR", nullable = true, length = 64)
	private String color;
	
	@Column(name = "TEST_M1_INI", nullable = true, columnDefinition = "decimal(16,6)")
	private Double m1Ini;
	
	@Column(name = "TEST_M2_INI", nullable = true, columnDefinition = "decimal(16,6)")
	private Double m2Ini;
	
	@Column(name = "TEST_M3_INI", nullable = true, columnDefinition = "decimal(16,6)")
	private Double m3Ini;
	
	@Column(name = "TEST_M1_END", nullable = true, columnDefinition = "decimal(16,6)")
	private Double m1End;
	
	@Column(name = "TEST_M2_END", nullable = true, columnDefinition = "decimal(16,6)")
	private Double m2End;
	
	@Column(name = "TEST_M3_END", nullable = true, columnDefinition = "decimal(16,6)")
	private Double m3End;
	
	@Column(name = "TEST_P1", nullable = true, columnDefinition = "decimal(16,6)")
	private Double p1;
	
	@Column(name = "TEST_P2", nullable = true, columnDefinition = "decimal(16,6)")
	private Double p2;
	
	@Column(name = "TEST_P3", nullable = true, columnDefinition = "decimal(16,6)")
	private Double p3;
	
	@Column(name = "TEST_COMMENT", nullable = true, columnDefinition = "varchar(max)")
	private String comment;
	
	@Column(name = "TEST_PROMISSING", nullable = true)
	private Boolean prommissing;
	
	@Column(name = "TEST_IDPRODUCT", nullable = true)
	private Integer idProduct;
	
	@Column(name = "TEST_PREMIXER", nullable = true)
	private Integer premixer;
	
	@Column(name = "TEST_MIXER", nullable = true)
	private Integer mixer;
	
	@Column(name = "TEST_AVERAGMP", nullable = true, columnDefinition = "decimal(16,6)")
	private Double averageMP ;
	
	@Column(name = "TEST_IDPROVIDER", nullable = true)
	private Integer idProvider;
	
	@Column(name = "TEST_RESULT_VIEW", nullable = true, length = 32)
	private String test_result_view;
	
	
	
	public Test() {
		super();
		this.idProperty = "0";
		this.batchTest="";
		this.dateLog = LocalDateTime.now();
		this.sapCode = "0";
		this.productName = "";
		this.min = 0.0;
		this.max = 0.0;
		this.temperature = 0.0;
		this.resultTest = 0.0;
		this.passTest = false;
		this.owner = "";
		this.provider = "";
		this.presentation = "";
		this.spindle = "";
		this.speedRPM = 0.0;
		this.timeViscocidad = 0;
		this.torque = 0.0;
		this.res114Provider = "";
		this.res114Batch = "0";
		this.resAs45Provider = "";
		this.resAs45Batch = "0";
		this.operator = "";
		this.color = "";
		this.m1Ini = 0.0;
		this.m2Ini = 0.0;
		this.m3Ini = 0.0;
		this.m1End = 0.0;
		this.m2End = 0.0;
		this.m3End = 0.0;
		this.p1 = 0.0;
		this.p2 = 0.0;
		this.p3 = 0.0;
		this.comment = "";
		this.prommissing = false;
		this.idProduct=0;
	}
	
	public Test(String idProperty, Double resultTest) {
		super();
		this.idProperty = idProperty;
		this.resultTest = resultTest;
	}

	public Integer getIdTest() {
		return idTest;
	}

	public void setIdTest(Integer idTest) {
		this.idTest = idTest;
	}

	public String getIdProperty() {
		return idProperty;
	}

	public Integer getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}

	public LocalDateTime getDateLog() {
		return dateLog;
	}

	public void setDateLog(LocalDateTime dateLog) {
		this.dateLog = dateLog;
	}

	public Date getTimeLog() {
		return timeLog;
	}

	public void setTimeLog(Date timeLog) {
		this.timeLog = timeLog;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBatchTest() {
		return batchTest;
	}

	public void setBatchTest(String batchTest) {
		this.batchTest = batchTest;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public Double getResultTest() {
		return resultTest;
	}

	public void setResultTest(Double resultTest) {
		this.resultTest = resultTest;
	}

	public Boolean getPassTest() {
		return passTest;
	}

	public void setPassTest(Boolean passTest) {
		this.passTest = passTest;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getPresentation() {
		return presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

	public String getSpindle() {
		return spindle;
	}

	public void setSpindle(String spindle) {
		this.spindle = spindle;
	}

	public Double getSpeedRPM() {
		return speedRPM;
	}

	public void setSpeedRPM(Double speedRPM) {
		this.speedRPM = speedRPM;
	}

	public Integer getTimeViscocidad() {
		return timeViscocidad;
	}

	public void setTimeViscocidad(Integer timeViscocidad) {
		this.timeViscocidad = timeViscocidad;
	}

	public Double getTorque() {
		return torque;
	}

	public void setTorque(Double torque) {
		this.torque = torque;
	}

	public String getRes114Provider() {
		return res114Provider;
	}

	public void setRes114Provider(String res114Provider) {
		this.res114Provider = res114Provider;
	}

	public String getRes114Batch() {
		return res114Batch;
	}

	public void setRes114Batch(String res114Batch) {
		this.res114Batch = res114Batch;
	}

	public String getResAs45Provider() {
		return resAs45Provider;
	}

	public void setResAs45Provider(String resAs45Provider) {
		this.resAs45Provider = resAs45Provider;
	}

	public String getResAs45Batch() {
		return resAs45Batch;
	}

	public void setResAs45Batch(String resAs45Batch) {
		this.resAs45Batch = resAs45Batch;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Double getM1Ini() {
		return m1Ini;
	}

	public void setM1Ini(Double m1Ini) {
		this.m1Ini = m1Ini;
	}

	public Double getM2Ini() {
		return m2Ini;
	}

	public void setM2Ini(Double m2Ini) {
		this.m2Ini = m2Ini;
	}

	public Double getM3Ini() {
		return m3Ini;
	}

	public void setM3Ini(Double m3Ini) {
		this.m3Ini = m3Ini;
	}

	public Double getM1End() {
		return m1End;
	}

	public void setM1End(Double m1End) {
		this.m1End = m1End;
	}

	public Double getM2End() {
		return m2End;
	}

	public void setM2End(Double m2End) {
		this.m2End = m2End;
	}

	public Double getM3End() {
		return m3End;
	}

	public void setM3End(Double m3End) {
		this.m3End = m3End;
	}

	public Double getP1() {
		return p1;
	}

	public void setP1(Double p1) {
		this.p1 = p1;
	}

	public Double getP2() {
		return p2;
	}

	public void setP2(Double p2) {
		this.p2 = p2;
	}

	public Double getP3() {
		return p3;
	}

	public void setP3(Double p3) {
		this.p3 = p3;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getPrommissing() {
		return prommissing;
	}

	public void setPrommissing(Boolean prommissing) {
		this.prommissing = prommissing;
	}

	public Integer getPremixer() {
		return premixer;
	}

	public void setPremixer(Integer premixer) {
		this.premixer = premixer;
	}

	public Integer getMixer() {
		return mixer;
	}

	public void setMixer(Integer mixer) {
		this.mixer = mixer;
	}

	public Double getAverageMP() {
		return averageMP;
	}

	public void setAverageMP(Double averageMP) {
		this.averageMP = averageMP;
	}

	public Integer getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(Integer idProvider) {
		this.idProvider = idProvider;
	}

	public String getTest_result_view() {
		return test_result_view;
	}

	public void setTest_result_view(String test_result_view) {
		this.test_result_view = test_result_view;
	}	
	
	
	
	
}
