package com.cpn.os4j;

import org.apache.commons.lang.builder.ToStringBuilder;

public class OpenStackCredentials {

	private AuthenticationCredentials auth;

	public OpenStackCredentials() {
	}

	public OpenStackCredentials(AuthenticationCredentials someCreds) {
		setAuth(someCreds);
	}

	public AuthenticationCredentials getAuth() {
		return auth;
	}

	public void setAuth(AuthenticationCredentials auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("auth", auth);
		return builder.toString();
	}

}