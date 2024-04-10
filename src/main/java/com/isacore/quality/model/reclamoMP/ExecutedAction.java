package com.isacore.quality.model.reclamoMP;

import com.isacore.EntidadBase;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "executed_action")
@Table(name = "EXECUTED_ACTION")
public class ExecutedAction extends EntidadBase {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "EA_ID")
//	private Integer idExecutedAction;
	
	@Column(name = "EA_DESCRIPTION", nullable = true, length = 1024)
	private String description;

	protected ExecutedAction() {	}
	public ExecutedAction(String description) {
		this.description = description;
	}
}
