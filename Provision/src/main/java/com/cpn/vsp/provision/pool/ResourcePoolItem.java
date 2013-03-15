package com.cpn.vsp.provision.pool;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.resource.Resource;

@Entity
public class ResourcePoolItem {

	@Id
	
	private String id = UUID.randomUUID().toString();
	
	@OneToOne
	private Resource resource;
	@OneToOne
	private Product product;
	
	public ResourcePoolItem() {
	}
	public ResourcePoolItem(Product aProduct, Resource aResource) {
		product = aProduct;
		resource = aResource;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
}
