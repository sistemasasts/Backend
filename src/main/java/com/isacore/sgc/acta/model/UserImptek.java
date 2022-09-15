package com.isacore.sgc.acta.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.isacore.localdate.converter.LocalDateTimeConverter;
import com.isacore.util.LocalDateTimeDeserializeIsa;
import com.isacore.util.LocalDateTimeSerializeIsa;


@Entity(name = "userImptek")
@Table(name = "USERIMPTEK")
public class UserImptek {
	
	@Id
	@Column(name = "USER_IDUSER", nullable = false, length = 20)
	private String idUser;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMP_CIEMPLOYEE", nullable = true)
	private Employee employee;
	
	@Column(name = "USER_NICKNAME", nullable = false, length = 32)
	private String nickName;
	
	@Column(name = "USER_PASS", nullable = false, length = 1024)
	private String userPass;
	
	@Column(name = "USER_LASTKEYDATECHANGE", nullable = true)
	@Convert(converter = LocalDateTimeConverter.class)
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime lastKeyDateChange;
	
	@Column(name = "USER_LASTACCESS", nullable = false)
	@Convert(converter = LocalDateTimeConverter.class)
	@JsonSerialize(using = LocalDateTimeSerializeIsa.class)
	@JsonDeserialize(using = LocalDateTimeDeserializeIsa.class)
	private LocalDateTime lastAccess;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "ROLE_NAME",insertable = true, updatable = true, nullable = true)
	private Role role;

	public UserImptek() {
		super();
	}

	public UserImptek(String idUser, Employee employee, String nickName, String userPass,
			LocalDateTime lastKeyDateChange, LocalDateTime lastAccess) {
		super();
		this.idUser = idUser;
		this.employee = employee;
		this.nickName = nickName;
		this.userPass = userPass;
		this.lastKeyDateChange = lastKeyDateChange;
		this.lastAccess = lastAccess;
	}

	public UserImptek(String idUser, Employee employee, String nickName, String userPass,
			LocalDateTime lastKeyDateChange, LocalDateTime lastAccess, Role role) {
		super();
		this.idUser = idUser;
		this.employee = employee;
		this.nickName = nickName;
		this.userPass = userPass;
		this.lastKeyDateChange = lastKeyDateChange;
		this.lastAccess = lastAccess;
		this.role = role;
	}


	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public LocalDateTime getLastKeyDateChange() {
		return lastKeyDateChange;
	}

	public void setLastKeyDateChange(LocalDateTime lastKeyDateChange) {
		this.lastKeyDateChange = lastKeyDateChange;
	}

	public LocalDateTime getLastAccess() {
		return lastAccess;
	}

	public void setLastAccess(LocalDateTime lastAccess) {
		this.lastAccess = lastAccess;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Employee getEmployee() {
		return employee;
	}

	public String getCorreo(){
		if(getEmployee() != null)
			return getEmployee().getEmail();
		return "";
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
}
