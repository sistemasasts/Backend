package com.isacore.quality.model.reclamoMP;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.EntidadBase;
import com.isacore.quality.model.*;
import com.isacore.security.model.Usuario;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;
import com.isacore.util.date.MoreDates;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "complaint")
@Table(name = "COMPLAINT")
public class Complaint extends EntidadBase {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "COM_ID")
//	private Integer idComplaint;
	
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
//	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
//	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
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
	
//	@Column(name = "COM_STATE", nullable = false, length = 8)
//	private String state;

	@Enumerated(EnumType.STRING)
	private ComplaintEstado state;
	
	@Column(name = "COM_UNIT", nullable = true, length = 16)
	private String unitP;

	@ManyToOne(fetch = FetchType.EAGER)
	private UnidadMedida unit;
	
	@Column(name = "COM_DATECREATE", nullable = true)
//	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
//	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime dateCreateComplaint;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "COM_ID", nullable = false)
	private List<ProviderActionPlan> listActionsPlanProvider = new ArrayList<>();

	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "COM_ID", nullable = true)
	private List<Problem> listProblems = new ArrayList<>();
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "COM_ID", nullable = false)
	private List<ExecutedAction> listExecutedActons = new ArrayList<>();
	
	private String otherProvider;
	
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime closeDate;
	
	@NotNull
	private long number;

	private String aprobadorCalidad;
	private String aprobadorCompras;
	private LocalDateTime fechaAprobadorCalidad;
	private LocalDateTime fechaAprobadorCompras;

	private String ordenCompra;
	
	//@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	//@JoinColumn(name = "COM_ID")
//	@Transient
//	private Product product;
//
//	@Transient
//	private Provider provider;
	protected Complaint() {}

	public Complaint(Integer idProduct, Integer idProvider, String batchProvider, String palletNumber, String affectedProduct,
					 Double affectedAmount, Double totalAmount, String place, LocalDateTime dateComplaint, Boolean applyReturn,
					 Double porcentComplaint, String detailNCP, UnidadMedida unit, String otherProvider, Usuario usuario, String ordenCompra) {
		this.idProduct = idProduct;
		this.idProvider = idProvider;
		this.batchProvider = batchProvider;
		this.palletNumber = palletNumber;
		this.affectedProduct = affectedProduct;
		this.affectedAmount = affectedAmount;
		this.totalAmount = totalAmount;
		this.place = place;
		this.dateComplaint = dateComplaint;
		this.applyReturn = applyReturn;
		this.porcentComplaint = porcentComplaint;
		this.detailNCP = detailNCP;
		this.job = usuario.getTrabajo();
		this.workArea = usuario.getArea().getNameArea();
		this.userName = usuario.getNombre();
		this.asUser = usuario.getNombreUsuario();
		this.unitP = unit.getAbreviatura();
		this.unit = unit;
		this.otherProvider = otherProvider;
		this.dateCreateComplaint = LocalDateTime.now();
		this.state = ComplaintEstado.CREADA;
		this.ordenCompra = ordenCompra;
		if(this.dateComplaint == null)
			this.dateComplaint = LocalDateTime.now();
		this.calcularTotal();
	}

	@Transient
	private Long kpiTime;

	public void calcularTotal(){
		double valor = (this.affectedAmount * 100 )/this.totalAmount;
		this.porcentComplaint = Math.round(valor * 100.0) / 100.0;
	}

	public Long getKpiTime() {
		if(closeDate !=null) {
			kpiTime = (long) MoreDates.totalBusinessDaysBetween(dateComplaint.toLocalDate(), closeDate.toLocalDate());
		}
		return kpiTime;
	}

	public void agregarProblema(Problem problem){
		this.listProblems.add(problem);
	}

	public void eliminarProblema(long id){
		this.listProblems.removeIf(x -> x.getId() == id);
	}

	public void agregarAccionEjecutada(ExecutedAction problem){
		this.listExecutedActons.add(problem);
	}

	public void eliminarAccionEjecutada(long id){
		this.listExecutedActons.removeIf(x -> x.getId() == id);
	}

	public void agregarPlanAccion(ProviderActionPlan problem){
		this.listActionsPlanProvider.add(problem);
	}

	public void eliminarPlanAccion(long id){
		this.listActionsPlanProvider.removeIf(x -> x.getId() == id);
	}


}
