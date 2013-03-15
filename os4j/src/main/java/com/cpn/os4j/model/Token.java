package com.cpn.os4j.model;

import java.io.Serializable;

public class Token implements Serializable{

	private static final long serialVersionUID = 558710544261035380L;
	String expires;
	String id;
	Tenant tenant;
	public String getExpires() {
		return expires;
	}
	public void setExpires(String expires) {
		this.expires = expires;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Tenant getTenant() {
		return tenant;
	}
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

}