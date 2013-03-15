package com.cpn.vsp.provision.remediation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpn.net.DNS;
import com.cpn.vsp.provision.resource.VirtualResource;

@Service
public class InstanceRemediationStrategy implements RemediationStrategy {

	@Autowired
	InstanceProvisioningService instanceProvisioningService;
	@PersistenceContext
	EntityManager entityManager;

	public InstanceRemediationStrategy() {
	}

	public InstanceRemediationStrategy(InstanceProvisioningService instanceProvisioningService) {
		this.instanceProvisioningService = instanceProvisioningService;
	}

	@Override
	public VirtualResource remediate(VirtualResource aResource) throws Exception {
		// Product aProduct =aResource.getProduct();
		// Resource aNewResource =
		// virtualCloudGatewayProvisioningStrategy.newInstance(aResource.getProduct());
		System.out.println("Remediating the Instance.......");
		// TODO Auto-generated method stub
		// return this.provisionInstance(aResource.getProduct());

		ProvisionedServer provisionedServer = instanceProvisioningService.provisionInstance(aResource.getProduct());
		aResource.setServerId(provisionedServer.getServer().getId());
		aResource.setCertificate(provisionedServer.getCertificate());
		aResource.setHostName(DNS.getDomain(provisionedServer.getIpAddress()));
		aResource.setIpAddress(provisionedServer.getIpAddress());

		entityManager.merge(aResource);
		return aResource;
	}
}
