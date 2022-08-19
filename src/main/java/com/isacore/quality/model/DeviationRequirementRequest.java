package com.isacore.quality.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "deviation_requirements_request")
@Table(name = "DEVIATION_REQUIREMENTS_REQUEST")
public class DeviationRequirementRequest {
	
	@Id
	@Column(name = "DRR_ID", nullable= false)
	private String idDRR;
	
	

}
