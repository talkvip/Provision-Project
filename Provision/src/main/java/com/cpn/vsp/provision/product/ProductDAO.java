package com.cpn.vsp.provision.product;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.vsp.provision.strategy.provision.ResourceProvisioningStrategy;

@Service
@NamedQueries({ @NamedQuery(name = "allForProvisioningStrategy", query = "from Product where provisioningStrategy = :strategy") })
@Transactional
public class ProductDAO {

	@PersistenceContext
	EntityManager entityManager;

	public List<Product> allForProvisioningStrategy(Class<? extends ResourceProvisioningStrategy> clazz) {
		return entityManager.createQuery("from Product where provisioningStrategy = :strategy", Product.class).setParameter("strategy", clazz.getName()).getResultList();
	}

	public Product getProduct(String aProductId) {
		return entityManager.find(Product.class, aProductId);
	}
}
