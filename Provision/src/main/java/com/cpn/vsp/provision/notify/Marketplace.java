package com.cpn.vsp.provision.notify;

import org.w3c.dom.Node;

import com.cpn.xml.XMLUtil;

public class Marketplace {

	private String baseUrl;
	private String partner;

	public static Marketplace unmarshall(Node node) {
		Marketplace marketplace = new Marketplace();
		XMLUtil x = new XMLUtil(node);
		try {
			marketplace.baseUrl = x.get("baseUrl");
			marketplace.partner = x.get("partner");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return marketplace;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

}
