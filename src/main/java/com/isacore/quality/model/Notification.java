package com.isacore.quality.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;

@Entity(name = "notification")
@Table(name = "NOTIFICATION")
public class Notification {

	@Id
	@Column(name = "NOTI_ID", nullable = false, length = 32)
	private String idNotification;
	
	@Column(name = "NOTI_TITLE", nullable = true, length = 1024)
	private String title;
	
	@Column(name = "NOTI_MESSAGE", nullable = true)
	private String message;
	
	@Column(name = "NOTI_TYPE", nullable = true, length = 16)
	private String type;
	
	@Column(name = "NOTI_IDAP", nullable = true)
	private String idActionProcess;
	
	@Column(name = "NOTI_IDPROCESS", nullable = true)
	private Integer idProcess;
	
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	@Column(name = "NOTI_DATE", nullable = true)
	private LocalDateTime date;
	
	@Column(name = "NOTI_STATE", nullable = true, length = 16)
	private String state;
	
	@Column(name = "NOTI_USER", nullable = true, length = 64)
	private String userImptek;
	/*
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_IDUSER", nullable = false)
	private UserImptek userImptek;
	*/
	public Notification() {
		this.date = LocalDateTime.now();
		this.state = "Pendiente";
		this.idNotification= this.generateId();
	}
	

	public String getIdNotification() {
		return idNotification;
	}

	public void setIdNotification(String idNotification) {
		this.idNotification = idNotification;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdActionProcess() {
		return idActionProcess;
	}

	public void setIdActionProcess(String idActionProcess) {
		this.idActionProcess = idActionProcess;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserImptek() {
		return userImptek;
	}

	public void setUserImptek(String userImptek) {
		this.userImptek = userImptek;
	}
	
	public Integer getIdProcess() {
		return idProcess;
	}


	public void setIdProcess(Integer idProcess) {
		this.idProcess = idProcess;
	}

	private String generateId() {
		String id=null;
		LocalDateTime cc = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		id=cc.format(formatter);
		id= id.replaceAll("[^\\dA-Za-z]", "");
				
		return id;
	}
	
	
}
