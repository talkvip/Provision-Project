package com.cpn.vsp.provision.resource;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@DiscriminatorValue("PhysicalResource")
public class PhysicalResource extends Resource {


	private static final long serialVersionUID = -8642109216067158871L;
	private String serialKey;
	private String password;
	

	public String getSerialKey() {
		return serialKey;
	}

	public void setSerialKey(String serial) {
		this.serialKey = serial;
	}

	@Override
	public void accept(ResourceVisitor aResourceVisitor) {
		aResourceVisitor.visit(this);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
