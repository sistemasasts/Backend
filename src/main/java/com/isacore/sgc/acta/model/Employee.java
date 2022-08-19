package com.isacore.sgc.acta.model;

import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isacore.quality.model.Area;

@Entity(name = "employee")
@Table(name = "EMPLOYEE")
public class Employee {
	
	@Id
	@Column(name = "EMP_CIEMPLOYEE", length = 20)
	private String ciEmployee;
	
	@OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinColumn(name = "KIN_IDKINDEMPLOYEE", nullable = false)
	private KindEmployee kind;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AREA_ID", nullable = false)
	private Area area;
	
	@Column(name = "EMP_NAME", length = 64, nullable = false)
	private String name;
	
	@Column(name = "EMP_LASTNAME", length = 64, nullable = false)
	private String lastName;
	
	@Column(name = "EMP_EMAIL", length = 64)
	private String email;
	
	@Column(name = "EMP_DATALIFE", length = 16)
	private String codDataLife;
	
	@Column(name = "EMP_STATE")
	private Boolean state;
	
	@Column(name = "EMP_JOB", nullable = true, length = 512)
	private String job;
	
	@ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "participants")
	private List<MeetingMinute> minutes;
	
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "responsible", fetch = FetchType.LAZY)
	private List<ActionPlan> plans;

	public String getCiEmployee() {
		return ciEmployee;
	}

	public void setCiEmployee(String ciEmployee) {
		this.ciEmployee = ciEmployee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodDataLife() {
		return codDataLife;
	}

	public void setCodDataLife(String codDataLife) {
		this.codDataLife = codDataLife;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public List<MeetingMinute> findMinutes() {
		return minutes;
	}

	public void setMinutes(List<MeetingMinute> minutes) {
		this.minutes = minutes;
	}

	public List<ActionPlan> getPlans() {
		return plans;
	}

	public void setPlans(List<ActionPlan> plans) {
		this.plans = plans;
	}
	
	
	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCompleteName() {
		StringBuilder sb = new StringBuilder().append(this.name).append(" ").append(this.lastName);		
		StringTokenizer st = new StringTokenizer(sb.toString());
		StringBuilder name = new StringBuilder();
		while(st.hasMoreTokens()) {
			name.append(StringUtils.capitalize(st.nextToken())).append(" ");
		}		
		return StringUtils.trim(name.toString());
	}

	public KindEmployee getKind() {
		return kind;
	}

	public void setKind(KindEmployee kind) {
		this.kind = kind;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
}
