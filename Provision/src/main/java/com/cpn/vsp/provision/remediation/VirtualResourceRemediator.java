package com.cpn.vsp.provision.remediation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cpn.vsp.provision.resource.VirtualResource;

@Service
public class VirtualResourceRemediator {

	public VirtualResourceRemediator() {
		super();
	}

	@Autowired
	@Qualifier("healthMonitor")
	HealthMonitor healthMonitor;

	@Autowired
	InstanceRemediationStrategy instanceRemediationStrategy;

	public VirtualResourceRemediator(HealthMonitor aHealthMonitor, InstanceRemediationStrategy anInstanceRemediationStrategy) {
		this.healthMonitor = aHealthMonitor;
		this.instanceRemediationStrategy = anInstanceRemediationStrategy;
	}

	public void remediateIfNecessary(VirtualResource aVirtualResource) throws Exception, IOException {
		boolean isHealthy = healthMonitor.isHealthy(aVirtualResource.getIpAddress());
		if (!isHealthy) {
			instanceRemediationStrategy.remediate(aVirtualResource);
		}
	}

}
