package com.cpn.vsp.provision;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpn.os4j.ComputeEndpoint;
import com.cpn.os4j.ServiceCatalog;
import com.cpn.os4j.model.Access;

@Service
public class EndpointFactory {

	ComputeEndpoint computeEndpoint = null;

	@Autowired
	Boolean localhostHack;

	@Autowired
	String openStackComputeRegion;

	@Autowired
	ServiceCatalog serviceCatalog;

	public synchronized ComputeEndpoint getComputeEndpoint() {
		if (computeEndpoint != null) {
			DateTimeFormatter format = ISODateTimeFormat.dateTimeNoMillis();
			DateTime expires = format.parseDateTime(computeEndpoint.getToken().getExpires());
			expires = expires.minusMinutes(10);
			if (expires.isBefore(new DateTime().toInstant())) {
				Access access = serviceCatalog.getAccess();
				access.localhostHack = localhostHack;
				computeEndpoint = access.getComputeEndpoint(openStackComputeRegion, "publicURL");
			}
		} else {
			Access access = serviceCatalog.getAccess();
			access.localhostHack = localhostHack;
			computeEndpoint = access.getComputeEndpoint(openStackComputeRegion, "publicURL");
		}
		return computeEndpoint;
	}

}
