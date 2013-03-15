package com.cpn.vsp.provision.pool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.product.ProductDAO;
import com.cpn.vsp.provision.resource.Resource;
import com.cpn.vsp.provision.strategy.provision.ResourceProvisioningStrategy;
import com.cpn.vsp.provision.strategy.provision.VirtualCloudGatewayProvisioningStrategy;

@Service
@Transactional
public class ResourcePool {

	@Autowired
	Long resourecePoolUpperBound = 5l;

	@Autowired
	@Qualifier("virtualCloudGatewayProvisioningStrategy")
	ResourceProvisioningStrategy acquisitionStrategy;

	@Autowired
	ResourcePoolItemDAO resourcePoolItemDAO;

	@Autowired
	ProductDAO productDAO;

	@Scheduled(fixedDelay = 30000)
	public void replenish() throws Exception {
		List<Product> products = productDAO.allForProvisioningStrategy(VirtualCloudGatewayProvisioningStrategy.class);
		for (Product p : products) {
			replenish(p);
		}
	}

	public synchronized void replenish(Product aProduct) throws Exception {
		replenish(aProduct, resourecePoolUpperBound - size(aProduct));
	}

	public long size(Product aProduct) {
		return resourcePoolItemDAO.size(aProduct);
	}

	private void addProductToPool(Product aProduct) throws Exception {
		resourcePoolItemDAO.addToPool(aProduct, acquisitionStrategy.provision(aProduct));
	}

	private synchronized void replenish(Product aProduct, long aSize) throws Exception {
		for (int i = 0; i < aSize; ++i) {
			addProductToPool(aProduct);
		}
	}

	public Resource deplete(final Product aProduct) throws Exception {
		if (size(aProduct) == 0) {
			return acquisitionStrategy.provision(aProduct);
		}
		return resourcePoolItemDAO.getFromPool(aProduct);
	}

	public void clearOut(final Resource aResource) {
		resourcePoolItemDAO.removeFromPool(aResource);
	}
}
