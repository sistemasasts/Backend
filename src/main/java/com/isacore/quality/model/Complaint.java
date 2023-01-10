package com.isacore.quality.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;
import com.isacore.util.date.MoreDates;

@Entity(name = "complaint")
@Table(name = "COMPLAINT")
public class Complaint {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COM_ID")
	private Integer idComplaint;
	
	@Column(name = "COM_IDPRODUCT", nullable = true)
	private Integer idProduct;
	
	@Column(name = "COM_IDPROVIDER", nullable = true)
	private Integer idProvider;
	
	@Column(name = "COM_BATCH_PROVIDER", nullable = true, length = 64)
	private String batchProvider;
	
	@Column(name = "COM_BATCH_PALLET", nullable = true, length = 64)
	private String palletNumber;
	
	@Column(name = "COM_AFFECTED_PRODUCT", nullable = true, length = 256)
	private String affectedProduct;
	
	@Column(name = "COM_AMOUNT_AFFECTED", nullable = true, columnDefinition = "decimal(16,6)")
	private Double affectedAmount;
	
	@Column(name = "COM_AMOUNT_TOTAl", nullable = true, columnDefinition = "decimal(16,6)")
	private Double totalAmount;
	
	@Column(name = "COM_PLACE", nullable = true, length = 64)
	private String place;
	
	@Column(name = "COM_DATE", nullable = true)
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime dateComplaint;
	
	@Column(name = "COM_APPLY_RETURN", nullable = true)
	private Boolean applyReturn;
	
	@Column(name = "COM_PORCENT_COMPLAINT", nullable = true, columnDefinition = "decimal(16,6)")
	private Double porcentComplaint;
	
	@Column(name = "COM_DETAIL_NCP", nullable = true, length = 1024)
	private String detailNCP;
	
	@Column(name = "COM_ASUSER", nullable = false, length = 64)
	private String asUser;
	
	@Column(name = "COM_U_NAME", nullable = false, length = 1024)
	private String userName;
	
	@Column(name = "COM_JOB", nullable = false, length = 512)
	private String job;
	
	@Column(name = "COM_WORK_AREA", nullable = false, length = 512)
	private String workArea;
	
	@Column(name = "COM_STATE", nullable = false, length = 8)
	private String state;
	
	@Column(name = "COM_UNIT", nullable = true, length = 16)
	private String unitP;

	@ManyToOne(fetch = FetchType.EAGER)
	private UnidadMedida unit;
	
	@Column(name = "COM_DATECREATE", nullable = true)
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime dateCreateComplaint;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "COM_ID", nullable = false)
	private List<ProviderActionPlan> listActionsPlanProvider;

	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "COM_ID", nullable = true)
	private List<Problem> listProblems;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "COM_ID", nullable = false)
	private List<ExecutedAction> listExecutedActons;
	
	private String otherProvider;
	
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime closeDate;
	
	@NotNull
	private long number;
	
	//@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	//@JoinColumn(name = "COM_ID")
	@Transient
	private Product product;
	
	@Transient
	private Provider provider;
	
	@Transient
	private Long kpiTime;
	
	public Integer getIdComplaint() {
		return idComplaint;
	}

	public void setIdComplaint(Integer idComplaint) {
		this.idComplaint = idComplaint;
	}

	public Integer getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}

	public Integer getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(Integer idProvider) {
		this.idProvider = idProvider;
	}
	

	public String getAffectedProduct() {
		return affectedProduct;
	}

	public void setAffectedProduct(String affectedProduct) {
		this.affectedProduct = affectedProduct;
	}

	public String getBatchProvider() {
		return batchProvider;
	}

	public void setBatchProvider(String batchProvider) {
		this.batchProvider = batchProvider;
	}

	public String getPalletNumber() {
		return palletNumber;
	}

	public void setPalletNumber(String palletNumber) {
		this.palletNumber = palletNumber;
	}

	public Double getAffectedAmount() {
		return affectedAmount;
	}

	public void setAffectedAmount(Double affectedAmount) {
		this.affectedAmount = affectedAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public LocalDateTime getDateComplaint() {
		return dateComplaint;
	}

	public void setDateComplaint(LocalDateTime dateComplaint) {
		this.dateComplaint = dateComplaint;
	}

	public Boolean getApplyReturn() {
		return applyReturn;
	}

	public void setApplyReturn(Boolean applyReturn) {
		this.applyReturn = applyReturn;
	}

	public Double getPorcentComplaint() {
		return porcentComplaint;
	}

	public void setPorcentComplaint(Double porcentComplaint) {
		this.porcentComplaint = porcentComplaint;
	}

	public String getDetailNCP() {
		return detailNCP;
	}

	public void setDetailNCP(String detailNCP) {
		this.detailNCP = detailNCP;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getWorkArea() {
		return workArea;
	}

	public void setWorkArea(String workArea) {
		this.workArea = workArea;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public LocalDateTime getDateCreateComplaint() {
		return dateCreateComplaint;
	}

	public void setDateCreateComplaint(LocalDateTime dateCreateComplaint) {
		this.dateCreateComplaint = dateCreateComplaint;
	}

	public List<ProviderActionPlan> getListActionsPlanProvider() {
		return listActionsPlanProvider;
	}

	public void setListActionsPlanProvider(List<ProviderActionPlan> listActionsPlanProvider) {
		this.listActionsPlanProvider = listActionsPlanProvider;
	}

	public List<Problem> getListProblems() {
		return listProblems;
	}

	public void setListProblems(List<Problem> listProblems) {
		this.listProblems = listProblems;
	}

	public List<ExecutedAction> getListExecutedActons() {
		return listExecutedActons;
	}

	public void setListExecutedActons(List<ExecutedAction> listExecutedActons) {
		this.listExecutedActons = listExecutedActons;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public String getUnitP() {
		return this.unit != null ? this.unit.getAbreviatura() : "";
	}

	public void setUnitP(String unitP) {
		this.unitP = unitP;
	}

	public String getOtherProvider() {
		return otherProvider;
	}

	public void setOtherProvider(String otherProvider) {
		this.otherProvider = otherProvider;
	}

	public Long getKpiTime() {
		if(closeDate !=null) {
			kpiTime = (long) MoreDates.totalBusinessDaysBetween(dateComplaint.toLocalDate(), closeDate.toLocalDate());
		}	
		
		return kpiTime;
	}

	public void setKpiTime(Long kpiTime) {
		this.kpiTime = kpiTime;
	}

	public LocalDateTime getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(LocalDateTime closeDate) {
		this.closeDate = closeDate;
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public UnidadMedida getUnit() {
		return unit;
	}

	public void setUnit(UnidadMedida unit) {
		this.unit = unit;
	}
}
