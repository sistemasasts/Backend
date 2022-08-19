package com.isacore.sgc.acta.model;

import java.time.LocalDateTime;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isacore.localdate.converter.LocalDateTimeConverter;


@Entity(name = "meetingMinute")
@Table(name = "MEETINGMINUTE")
public class MeetingMinute {

	@Id
	@Column(name = "MEE_IDMINUTE", nullable = false, length = 16)
	private String idMinute;
	
	@Column(name = "MEE_SUBJECT", nullable = false, length = 128)
	private String subject;
	
	@Column(name = "MEE_REVIEW", nullable = false, length = 2)
	private String review;
	
	@Column(name = "MEE_DATEMEETING", nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy@HH:mm:ss", timezone="America/Bogota")
	private LocalDateTime dateMeeting;
	
	@Column(name = "MEE_SUMMARY", columnDefinition = "varchar(Max)", nullable = false)
	private String summary;
	
	@Column(name = "MEE_REFERENCE", nullable = false, length = 128)
	private String reference;
	
	@Column(name = "MEE_CODING", nullable = false, length = 8)
	private String coding;
	
	@Column(name = "MEE_ASUSER", nullable = false, length = 32)
	private String owner;
	
	@Column(name = "MEE_FILEPATH",columnDefinition = "varchar(Max)", nullable = true)
	private String filePath;
	
	@Column(name = "MEE_TYPE", nullable = false, length = 2)
	private String type;
	
	@JsonIgnore
	@OneToMany(mappedBy = "minute", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
	private List<ActionPlan> plans;	
	
	@OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "MEE_IDMINUTE", nullable = true)
	private List<Diary> activities;
	
	@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinTable(name = "ASSISTANCE",
	joinColumns = {@JoinColumn(name = "MEE_IDMINUTE")},
	inverseJoinColumns = {@JoinColumn(name = "EMP_CIEMPLOYEE")})
	private List<Employee> participants;
	

	public String getIdMinute() {
		return idMinute;
	}

	public void setIdMinute(String idMinute) {
		this.idMinute = idMinute;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public LocalDateTime getDateMeeting() {
		return dateMeeting;
	}

	public void setDateMeeting(LocalDateTime dateMeeting) {
		this.dateMeeting = dateMeeting;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getCoding() {
		return coding;
	}

	public void setCoding(String coding) {
		this.coding = coding;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<ActionPlan> getPlans() {
		return plans;
	}

	public void setPlans(List<ActionPlan> plans) {
		this.plans = plans;
	}

	public List<Diary> getActivities() {
		return activities;
	}

	public void setActivities(List<Diary> activities) {
		this.activities = activities;
	}

	public List<Employee> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Employee> participants) {
		this.participants = participants;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
