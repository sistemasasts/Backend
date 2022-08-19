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

@Entity(name = "process")
@Table(name = "PROCESS")
public class ProcessFlow {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PROC_ID")
	private Integer idProcess;
		
	@Column(name = "PROC_START_DATE", nullable = true)
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate startDate;
	
	
	@Column(name = "PROC_FINISH_DATE", nullable = true)
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializeIsa.class)
	private LocalDate finishDate;
	
	@Column(name = "PROC_STATE", nullable = true)
	private String state;
	
	@OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "PROC_ID", nullable = false)
	private List<ActionProcess> listActionsProcess;
	
	@OneToOne(cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "REQ_ID", nullable = true )
	private RequestTest testRequest;
	
	@OneToOne(cascade={CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "PRT_ID", nullable = true )
	private ProcessTestRequest processTestRequest;
	
	@OneToOne(cascade={CascadeType.DETACH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "SUB_PROC_ID", nullable = true)
	private ProcessFlow subProcess;
	
	
	public Integer getIdProcess() {
		return idProcess;
	}

	public void setIdProcess(Integer idProcess) {
		this.idProcess = idProcess;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(LocalDate finishDate) {
		this.finishDate = finishDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<ActionProcess> getListActionsProcess() {
		return listActionsProcess;
	}

	public void setListActionsProcess(List<ActionProcess> listActionsProcess) {
		this.listActionsProcess = listActionsProcess;
	}

	public RequestTest getTestRequest() {
		return testRequest;
	}

	public void setTestRequest(RequestTest testRequest) {
		this.testRequest = testRequest;
	}

	public ProcessTestRequest getProcessTestRequest() {
		return processTestRequest;
	}

	public void setProcessTestRequest(ProcessTestRequest processTestRequest) {
		this.processTestRequest = processTestRequest;
	}


	public ProcessFlow getSubProcess() {
		return subProcess;
	}

	public void setSubProcess(ProcessFlow subProcesHijo) {
		this.subProcess = subProcesHijo;
	}
	
	public boolean isSubProcess() {
		if (this.subProcess!= null)
			return true;
		else
			return false;
	}

	
}
