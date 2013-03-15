package com.cpn.vsp.provision.strategy.provision;

import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.resource.Resource;

public interface ResourceProvisioningStrategy {
	

	public Resource provision(Product aProduct) throws Exception;

	public void deprovision(Resource resource);
}
