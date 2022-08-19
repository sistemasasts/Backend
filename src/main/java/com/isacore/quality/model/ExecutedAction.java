package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "executed_action")
@Table(name = "EXECUTED_ACTION")
public class ExecutedAction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EA_ID")
	private Integer idExecutedAction;
	
	@Column(name = "EA_DESCRIPTION", nullable = true, length = 1024)
	private String description;

	public Integer getIdExecutedAction() {
		return idExecutedAction;
	}

	public void setIdExecutedAction(Integer idExecutedAction) {
		this.idExecutedAction = idExecutedAction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
