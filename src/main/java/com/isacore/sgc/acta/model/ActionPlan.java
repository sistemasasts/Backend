package com.isacore.sgc.acta.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.isacore.localdate.converter.LocalDateConverter;

@Entity(name = "actionPlan")
@Table(name = "ACTIONPLAN")
public class ActionPlan {

	@Id
	@Column(name = "ACT_IDACTIONPLAN", nullable = false)
	private String idPlan;
	
	//@JsonIgnore	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEE_IDMINUTE", nullable = false)
	private MeetingMinute minute;
	
	@Column(name = "ACT_DESCRIPTION",columnDefinition = "varchar(Max)", nullable = false)
	private String description;
	
	@Column(name = "ACT_STARTDATE", nullable = false)
	@Convert(converter = LocalDateConverter.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy",timezone = "America/Bogota")
	private LocalDate startDate;
	
	@Column(name = "ACT_DEADLINE", nullable = false)
	@Convert(converter = LocalDateConverter.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy",timezone = "America/Bogota")
	private LocalDate deadLine;
	
	@Column(name = "ACT_EXECUTE", nullable = true)
	private boolean executed;
	
	@Column(name = "ACT_EXECUTIONDATE", nullable = true)
	@Convert(converter = LocalDateConverter.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy",timezone = "America/Bogota")
	private LocalDate executionDate;
	
	@Column(name = "ACT_ASUSER", nullable = true, length = 32)
	private String asUser;
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable(name = "RESPONSIBLE",
	joinColumns = {@JoinColumn(name = "ACT_IDACTIONPLAN")},
	inverseJoinColumns = {@JoinColumn(name = "EMP_CIEMPLOYEE")})
	private List<Employee> responsible;

	public ActionPlan() {}
	
	public ActionPlan(String idPlan, String description, LocalDate startDate, LocalDate deadLine, boolean executed,
			LocalDate executionDate) {
		this.idPlan = idPlan;
		this.description = description;
		this.startDate = startDate;
		this.deadLine = deadLine;
		this.executed = executed;
		this.executionDate = executionDate;
	}

	public String getIdPlan() {
		return idPlan;
	}

	public void setIdPlan(String idPlan) {
		this.idPlan = idPlan;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(LocalDate deadLine) {
		this.deadLine = deadLine;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public LocalDate getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(LocalDate executionDate) {
		this.executionDate = executionDate;
	}

	public String getAsUser() {
		return asUser;
	}

	public void setAsUser(String asUser) {
		this.asUser = asUser;
	}

	public MeetingMinute getMinute() {
		return minute;
	}

	public void setMinute(MeetingMinute minute) {
		this.minute = minute;
	}

	public List<Employee> getResponsible() {
		return responsible;
	}

	public void setResponsible(List<Employee> responsible) {
		this.responsible = responsible;
	}
	
	
	
	
}
