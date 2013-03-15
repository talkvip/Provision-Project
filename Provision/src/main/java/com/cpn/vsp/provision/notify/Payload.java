package com.cpn.vsp.provision.notify;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.cpn.vsp.provision.order.ProductOrder;
import com.cpn.vsp.provision.user.Company;
import com.cpn.vsp.provision.user.User;
import com.cpn.xml.XMLUtil;

public class Payload {

	private Company company;
	private ProductOrder productOrder;
	private Account account;
	private User user;

	public static Payload unmarshall(Node node) {
		Payload payload = new Payload();
		XMLUtil x = new XMLUtil(node);
		try {
			if(x.getNode("company") != null && x.getNode("company").hasChildNodes()){
				payload.setCompany(Company.unmarshall(x.getNode("company")));
			}
			if(x.getNode("productOrder") != null && x.getNode("productOrder").hasChildNodes()){
				payload.setOrder(ProductOrder.unmarshall(x.getNode("productOrder")));
			}
			if(x.getNode("account") != null && x.getNode("account").hasChildNodes()){
				payload.setAccount(Account.unmarshall(x.getNode("account")));
			}
			if(x.getNode("user") != null && x.getNode("user").hasChildNodes()){
				payload.setUser(User.unmarshall(x.getNode("user")));
			}
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return payload;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public ProductOrder getOrder() {
		return productOrder;
	}

	public void setOrder(ProductOrder productOrder) {
		this.productOrder = productOrder;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
