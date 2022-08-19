package com.isacore.quality.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.isacore.localdate.converter.LocalDateConverter;
import com.isacore.util.LocalDateDeserializeIsa;

@Entity(name = "exit_material_history")
@Table(name = "EXIT_MATERIAL_HISTORY")
public class ExitMaterialHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMH_ID")
	private Integer idEMH;
	
	@Column(name = "EMH_QUANTITY", nullable = false, columnDefinition = "decimal(10,2)")
	private Double quantity;
	
	@Column(name = "EMH_DESCRIPTION", nullable = true, length = 2048)
	private String description;
	
	@Column(name = "NCP_ID", nullable = false)
	private Integer ncpID;
	
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	@Column(name = "EMH_DATE", nullable = false)
	private LocalDate date;
	
	@Column(name = "EMH_TYPE", nullable = false, length = 2048)
	private String type;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "EMH_ID", nullable = true)
	private List<TaskNcp> listTasks;
	
	@OneToOne(cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "CR_ID", nullable = true )
	private ConcessionRequest concessionRequest;
	
	/* Campos de auditoria */
	@Column(name = "EMH_ASUSER", nullable = false, length = 2048)
	private String asUser;
	
	@Column(name = "EMH_U_NAME", nullable = false, length = 2048)
	private String nameUser;
	
	@Column(name = "EMH_JOB", nullable = false, length = 2048)
	private String job;
	
	@Column(name = "EMH_WORK_AREA", nullable = false, length = 2048)
	private String workArea;
	
	public Integer getIdEMH() {
		return idEMH;
	}

	public void setIdEMH(Integer idEMH) {
		this.idEMH = idEMH;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<TaskNcp> getListTasks() {
		return listTasks;
	}

	public void setListTasks(List<TaskNcp> listTasks) {
		this.listTasks = listTasks;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
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

	public ConcessionRequest getConcessionRequest() {
		return concessionRequest;
	}

	public void setConcessionRequest(ConcessionRequest concessionRequest) {
		this.concessionRequest = concessionRequest;
	}

	public Integer getNcpID() {
		return ncpID;
	}

	public void setNcpID(Integer ncpID) {
		this.ncpID = ncpID;
	}
	
	
}
