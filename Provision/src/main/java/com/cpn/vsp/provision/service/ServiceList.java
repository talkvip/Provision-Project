package com.cpn.vsp.provision.service;

import java.util.List;

public class ServiceList {
	List<Service> services;
	Service service;

	public Service getService() {
		return service;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
}
