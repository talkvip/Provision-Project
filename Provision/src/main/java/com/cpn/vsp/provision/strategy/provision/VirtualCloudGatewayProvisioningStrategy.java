package com.cpn.vsp.provision.strategy.provision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.net.DNS;
import com.cpn.vsp.provision.certificate.CertificateController;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.remediation.InstanceProvisioningService;
import com.cpn.vsp.provision.remediation.ProvisionedServer;
import com.cpn.vsp.provision.resource.PhysicalResource;
import com.cpn.vsp.provision.resource.Resource;
import com.cpn.vsp.provision.resource.ResourceVisitor;
import com.cpn.vsp.provision.resource.VirtualResource;

@ProvisioningStrategy(description = "A Virtual Cloud Gateway Provisioning Strategy", type = StrategyType.VIRTUAL)
@Service
@Primary
public class VirtualCloudGatewayProvisioningStrategy implements ResourceProvisioningStrategy {

	private static final Logger logger = LoggerFactory.getLogger(VirtualCloudGatewayProvisioningStrategy.class);

	@Autowired
	CertificateController certController;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	InstanceProvisioningService instanceProvisioningService;


	public VirtualCloudGatewayProvisioningStrategy() {
	}

	@Transactional
	public Resource newInstance(final Product aProduct) throws Exception {
		ProvisionedServer server = instanceProvisioningService.provisionInstance(aProduct);

		VirtualResource d = new VirtualResource();
		d.setServerId(server.getServer().getId());
		d.setIpAddress(server.getIpAddress());
		d.setProduct(aProduct);
		d.setHostName(DNS.getDomain(server.getIpAddress()));
		d.setCertificate(server.getCertificate());
		entityManager.persist(d);
		return d;
	}

	@Override
	public Resource provision(Product aProduct) throws Exception {
		return newInstance(aProduct);
	}

	@Override
	@Transactional
	public void deprovision(Resource resource) {
		ResourceVisitor deprovisioner = new ResourceVisitor() {
			@Override
			public void visit(VirtualResource aResource) {
				try {
					instanceProvisioningService.deprovionInstance(aResource.getServerId());
				} catch (Exception e) {
					logger.warn(e.getMessage(), e);
				}
				entityManager.remove(aResource);
			}

			@Override
			public void visit(PhysicalResource physicalResource) {
				throw new UnsupportedOperationException();
			}
		};
		resource.accept(deprovisioner);
	}
}
