package com.cpn.vsp.provision.remediation;


import com.cpn.vsp.provision.resource.VirtualResource;

public interface RemediationStrategy {
	public VirtualResource remediate(VirtualResource resource) throws Exception;
}
