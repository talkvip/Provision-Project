package com.cpn.vsp.provision.notify;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.cpn.xml.XMLUtil;

public class Account {

	private String accountIdentifier;
	private String status;

	public static Account unmarshall(Node node) {
		Account account = new Account();
		XMLUtil x = new XMLUtil(node);
		try {
			account.accountIdentifier = x.get("accountIdentifier");
			account.status = x.get("status");
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return account;
	}

	public String getAccountIdentifier() {
		return accountIdentifier;
	}

	public void setAccountIdentifier(String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
