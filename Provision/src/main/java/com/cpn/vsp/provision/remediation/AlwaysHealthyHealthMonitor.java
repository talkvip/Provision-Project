package com.cpn.vsp.provision.remediation;

import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlwaysHealthyHealthMonitor implements HealthMonitor {

	Logger logger = LoggerFactory.getLogger(AlwaysHealthyHealthMonitor.class);
	
	public AlwaysHealthyHealthMonitor() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isHealthy(String aResourceName) throws UnknownHostException, IOException {
		logger.trace("Always True is getting called");
		
		// TODO Auto-generated method stub
		return true;
	}

}
