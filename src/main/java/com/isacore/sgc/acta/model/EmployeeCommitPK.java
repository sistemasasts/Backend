package com.isacore.sgc.acta.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class EmployeeCommitPK implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMC_IDMCO", nullable = false)
	private int idMCO;
	
	@ManyToOne
	@JoinColumn(name = "EMP_CIEMPLOYEE", nullable = false)
	private Employee employee;
	
	@ManyToOne
	@JoinColumn(name = "COM_IDCOMMITTEE", nullable = false)
	private Committee commit;

	public int getIdMCO() {
		return idMCO;
	}

	public void setIdMCO(int idMCO) {
		this.idMCO = idMCO;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Committee getCommitee() {
		return commit;
	}

	public void setCommitee(Committee commit) {
		this.commit = commit;
	}
	
	
}
