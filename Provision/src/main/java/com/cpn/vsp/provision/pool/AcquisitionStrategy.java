package com.cpn.vsp.provision.pool;

import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.resource.Resource;

public interface AcquisitionStrategy {

	public Resource acquire(Product aProduct) throws Exception;

}
