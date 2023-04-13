package com.isacore.quality.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.util.LocalDateDeserializeIsa;
import com.isacore.util.LocalDateSerializeIsa;

@JsonInclude(Include.NON_NULL)
@Entity(name = "hcchead")
@Table(name = "HCCHEAD")
public class HccHead {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "HCCH_SAPCODE", nullable = false, length = 64)	
	private String sapCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", insertable = true, updatable = false)
	private Product product;
	/**
	 * Esta variable tambien se la utiliza para el campo Nombre del proveedor 
	 * en el caso de la HCC de materia prima.
	 * En el hcc PT es el codigo del documento de SGC
	 */
	@Column(name = "HCCH_NORM", nullable = false, length = 4096)
	private String hccNorm;
	
	@Column(name = "HCCH_DATE", nullable = false)
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate dateCreate;
	
	/*
	 * Fecha de la orden de pedido de materia prima
	 * Fecha de orden de produccion para producto terminado
	 */
	@Column(name = "HCCH_DATE_ORDER", nullable = true)
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = LocalDateSerializeIsa.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate dateOrder;
	
	
	/**
	 * {@value campo que se utiliza en la HCC de materia prima para la órden de compra de materia prima}
	 */
	@Column(name = "HCCH_ORDER_NUMBER", nullable = true, length = 128)
	private String orderNumber;
	
	/**
	 * Esta variable tambien sela utiliza para el campo Pedido en el caso de la 
	 * HCC de materia prima
	 */
	@Column(name = "HCCH_PERIODICITY", nullable = true, length = 128)
	private String periodicity;
	
	@Column(name = "HCCH_REFERRAL_GUIDE", nullable = true, length = 64)
	private String referralGuide;
	
	@Column(name = "HCCH_CODE", nullable = false, length = 32)
	private String code;
	
	@Column(name = "HCCH_REVIEW", nullable = true, length = 64)
	private String review;
	
	@Column(name = "HCCH_REFERENCE", nullable = true, length = 32)
	private String reference;
	/**
	 * Esta variable tambien se la utiliza tambien para el campo código del proveedor
	 * en el caso de la HCC de materia prima
	 */
	@Column(name = "HCCH_OF", nullable = true, length = 64)
	private String of;
	
	@Column(name = "HCCH_BATCH", nullable = false, length = 32)
	private String hcchBatch;
	
	@Column(name = "HCCH_COMMENT", nullable = false, columnDefinition = "varchar(max)")
	private String comment;
	
	@Column(name = "HCCH_ANALYSIS", nullable = false, columnDefinition = "varchar(max)")
	private String analysis;
	
	@Column(name = "HCCH_ASUSER", nullable = false, length = 64)
	private String asUser;
	
	@Column(name = "HCCH_U_NAME", nullable = false, length = 1024)
	private String userName;
	
	@Column(name = "HCCH_JOB", nullable = false, length = 512)
	private String job;
	
	@Column(name = "HCCH_WORK_AREA", nullable = false, length = 512)
	private String workArea;
	
	@Column(name = "HCCH_REPORT_HEADT", nullable = false, length = 16)
	private String reportHeadT;
	
	@Column(name = "HCCH_IDPROVIDER", nullable = true)
	private Integer idProviderMP;

	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "HCC_HEAD_ID", nullable = false)
	private List<HccDetail> detail;
	
	public HccHead() {this.id = 0L;}

	private String rutaImagenMuestra;

	@Transient
	private String imagenPatron64;

	public HccHead(String sapCode, Product product, LocalDate dateCreate, String hcchBatch, String analysis) {
		super();
		this.sapCode = sapCode;
		this.product = product;
		this.dateCreate = dateCreate;
		this.hcchBatch = hcchBatch;
		this.analysis = analysis;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getHccNorm() {
		return hccNorm;
	}

	public void setHccNorm(String hccNorm) {
		this.hccNorm = hccNorm;
	}

	public LocalDate getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(LocalDate dateCreate) {
		this.dateCreate = dateCreate;
	}

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getOf() {
		return of;
	}

	public void setOf(String of) {
		this.of = of;
	}

	public String getHcchBatch() {
		return hcchBatch;
	}

	public void setHcchBatch(String hcchBatch) {
		this.hcchBatch = hcchBatch;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
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

	public List<HccDetail> getDetail() {
		return detail;
	}

	public void setDetail(List<HccDetail> detail) {
		this.detail = detail;
	}

	public LocalDate getDateOrder() {
		return dateOrder;
	}

	public void setDateOrder(LocalDate dateOrder) {
		this.dateOrder = dateOrder;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getReportHeadT() {
		return reportHeadT;
	}

	public void setReportHeadT(String reportHeadT) {
		this.reportHeadT = reportHeadT;
	}

	public String getReferralGuide() {
		return referralGuide;
	}

	public void setReferralGuide(String referralGuide) {
		this.referralGuide = referralGuide;
	}

	public Integer getIdProviderMP() {
		return idProviderMP;
	}

	public void setIdProviderMP(Integer idProviderMP) {
		this.idProviderMP = idProviderMP;
	}

	public String getRutaImagenMuestra() {
		return rutaImagenMuestra;
	}

	public void setRutaImagenMuestra(String rutaImagenMuestra) {
		this.rutaImagenMuestra = rutaImagenMuestra;
	}

	public String getImagenPatron64() {
		return imagenPatron64;
	}

	public void setImagenPatron64(String imagenPatron64) {
		this.imagenPatron64 = imagenPatron64;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HccHead other = (HccHead) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
