package com.cpn.vsp.provision.pool;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.resource.Resource;

@Service
@Transactional
public class ResourcePoolItemDAO {

	@PersistenceContext
	EntityManager entityManager;

	public long size(Product aProduct) {
		return entityManager.createQuery("select count(*) from ResourcePoolItem where product = ?", Long.class).setParameter(1, aProduct).getSingleResult();
	}

	public void addToPool(Product aProduct, Resource aResource) {
		ResourcePoolItem item = new ResourcePoolItem(aProduct, aResource);
		entityManager.persist(item);
	}

	public Resource getFromPool(Product aProduct) {
		ResourcePoolItem item = entityManager.createQuery("from ResourcePoolItem where product = ?", ResourcePoolItem.class).setParameter(1, aProduct).getResultList().get(0);
		entityManager.remove(item);
		return item.getResource();
	}
	
	public void removeFromPool(Resource aResource){
		entityManager.createQuery("delete from ResourcePoolItem where resource = ?").setParameter(1, aResource).executeUpdate();
	}
	
}
