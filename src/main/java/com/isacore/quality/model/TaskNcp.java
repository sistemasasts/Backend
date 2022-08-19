package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "taskncp")
@Table(name = "TASK_NCP")
public class TaskNcp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TASK_ID")
	private Integer idTask;
	
	@Column(name = "TASK_DESCRIPTION", nullable = false, columnDefinition = "varchar(max)")
	private String descriptionTask;
	
	@Column(name = "TASK_PERCENT", nullable = true, columnDefinition = "decimal(10,2)")
	private Double percentTask;

	public Integer getIdTask() {
		return idTask;
	}

	public void setIdTask(Integer idTask) {
		this.idTask = idTask;
	}

	public String getDescriptionTask() {
		return descriptionTask;
	}

	public void setDescriptionTask(String descriptionTask) {
		this.descriptionTask = descriptionTask;
	}

	public Double getPercentTask() {
		return percentTask;
	}

	public void setPercentTask(Double percentTask) {
		this.percentTask = percentTask;
	}
	
}
