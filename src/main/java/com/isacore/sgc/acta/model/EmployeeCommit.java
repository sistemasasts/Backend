package com.isacore.sgc.acta.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.isacore.localdate.converter.LocalDateConverter;

@Entity(name = "employeeCommit")
@IdClass(EmployeeCommitPK.class)
@Table(name = "EMPLOYEECOMMIT")
public class EmployeeCommit {
	
	@Id
	private int idMCO;
	
	@Id
	private Employee employee;
	
	@Id
	private Committee commit;
	
	@Column(name = "EMC_STARTDATE", nullable = false)
	@Convert(converter = LocalDateConverter.class)
	private LocalDate startDate;
	
	@Column(name = "EMC_ENDDATE", nullable = true)
	@Convert(converter = LocalDateConverter.class)
	private LocalDate endDate;
	
	@Column(name = "EMC_STATE", nullable = false)
	private boolean state;
}
