package com.cpn.vsp.provision.service;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;

@Entity
@JsonTypeInfo(include = As.PROPERTY, use = org.codehaus.jackson.annotate.JsonTypeInfo.Id.CLASS, property = "class")
public abstract class Service {

	@Id
	String id = UUID.randomUUID().toString();
	String api;
	@OneToOne
	ServiceDescription serviceDescription;
	transient Status status;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ServiceDescription getDescription() {
		return serviceDescription;
	}

	public void setDescription(ServiceDescription description) {
		this.serviceDescription = description;
	}
}
