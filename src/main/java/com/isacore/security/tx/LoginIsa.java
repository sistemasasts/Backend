package com.isacore.security.tx;

public class LoginIsa{

	private String userName;
	
	private String pass;

	
	public LoginIsa() {
		super();
	}

	public LoginIsa(String userName, String pass) {
		super();
		this.userName = userName;
		this.pass = pass;
	}	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
	
}
