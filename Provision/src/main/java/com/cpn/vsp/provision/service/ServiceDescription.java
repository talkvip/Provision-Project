package com.cpn.vsp.provision.service;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ServiceDescription {

	@Id
	String id = UUID.randomUUID().toString();
	String name;
	String family;
	String pkgurl;
	
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
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getPkgurl() {
		return pkgurl;
	}
	public void setPkgurl(String pkgurl) {
		this.pkgurl = pkgurl;
	}
	
}
