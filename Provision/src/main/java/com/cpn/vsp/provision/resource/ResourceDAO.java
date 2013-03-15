package com.cpn.vsp.provision.resource;

import org.springframework.stereotype.Service;

import com.cpn.vsp.provision.database.AbstractDAO;

@Service
public class ResourceDAO extends AbstractDAO<Resource> {

	@Override
	public Class<Resource> getDTOClass() {
		return Resource.class;
	}

	
}
