package com.cpn.vsp.provision.order;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.cpn.vsp.provision.database.DataTransferObject;
import com.cpn.xml.XMLUtil;

@Entity
public class OrderItem implements DataTransferObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id = UUID.randomUUID().toString();
	private String quantity;
	private String unit;
	@ManyToOne
	private ProductOrder productOrder;

	public static OrderItem unmarshall(Node n) {
		OrderItem i = new OrderItem();
		XMLUtil x = new XMLUtil(n);
		try {
			i.quantity = x.get("quantity");
			i.unit = x.get("unit");
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return i;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductOrder getOrder() {
		return productOrder;
	}

	public void setOrder(ProductOrder productOrder) {
		this.productOrder = productOrder;
	}

}
