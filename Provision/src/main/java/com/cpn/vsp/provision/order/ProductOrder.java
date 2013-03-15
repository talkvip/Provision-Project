package com.cpn.vsp.provision.order;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.cpn.vsp.provision.database.DataTransferObject;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.user.User;
import com.cpn.xml.XMLUtil;

@Entity
public class ProductOrder implements DataTransferObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id = UUID.randomUUID().toString();
	private String editionCode;
	private String pricingDuration;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "productOrder", fetch=FetchType.EAGER)
	private Set<OrderItem> items = new HashSet<>();

	@OneToOne
	private User creator;

	@ManyToOne
	private Product product;

	public static ProductOrder unmarshall(Node node) {
		ProductOrder productOrder = new ProductOrder();
		XMLUtil x = new XMLUtil(node);
		try {
			productOrder.editionCode = x.get("editionCode");
			productOrder.pricingDuration = x.get("pricingDuration");
			for (Node n : x.getList("item")) {
				productOrder.items.add(OrderItem.unmarshall(n));
			}
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return productOrder;
	}

	public String getEditionCode() {
		return editionCode;
	}

	public void setEditionCode(String editionCode) {
		this.editionCode = editionCode;
	}

	public String getPricingDuration() {
		return pricingDuration;
	}

	public void setPricingDuration(String pricingDuration) {
		this.pricingDuration = pricingDuration;
	}

	public Set<OrderItem> getItems() {
		return items;
	}

	public void setItems(Set<OrderItem> items) {
		this.items = items;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
