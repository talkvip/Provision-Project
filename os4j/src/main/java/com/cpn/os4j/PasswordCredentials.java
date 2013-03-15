package com.cpn.os4j;

import java.io.Serializable;

public class PasswordCredentials implements Serializable {

	private static final long serialVersionUID = -2018402245732990822L;
	String username;
	String password;

	public PasswordCredentials(String aUsername, String aPassword) {
		username = aUsername;
		password = aPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}