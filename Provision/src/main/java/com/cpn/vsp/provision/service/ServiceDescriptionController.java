package com.cpn.vsp.provision.service;

import org.springframework.web.bind.annotation.RequestMapping;

import com.cpn.vsp.provision.AbstractRestController;

@RequestMapping("/server/service/description")
public class ServiceDescriptionController extends AbstractRestController<ServiceDescription>{

	@Override
	public Class<ServiceDescription> getPersistenceClass() {
		return ServiceDescription.class;
	}
	
	
}
