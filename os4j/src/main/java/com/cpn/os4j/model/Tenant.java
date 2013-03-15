package com.cpn.os4j.model;

import java.io.Serializable;

public class Tenant implements Serializable {

	private static final long serialVersionUID = -8570469557845043470L;
	String id;
	String name;
	String description;
	String enabled;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
}