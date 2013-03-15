package com.cpn.vsp.provision.notify;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.cpn.vsp.provision.user.User;
import com.cpn.xml.XMLUtil;

public class Event {

	User creator;
	String flag;
	Marketplace marketplace;
	Payload payload;
	String returnUrl;
	String type;

	public static Event unmarshall(final Node aNode) {
		final Event e = new Event();
		final XMLUtil x = new XMLUtil(aNode);
		try {
			e.flag = x.get("flag");
			e.returnUrl = x.get("returnUrl");
			e.type = x.get("type");
			e.creator = User.unmarshall(x.getNode("creator"));
			e.marketplace = Marketplace.unmarshall(x.getNode("marketplace"));
			e.payload = Payload.unmarshall(x.getNode("payload"));
			
			
		} catch (final XPathExpressionException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
		return e;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Marketplace getMarketplace() {
		return marketplace;
	}

	public void setMarketplace(Marketplace marketplace) {
		this.marketplace = marketplace;
	}

	public Payload getPayload() {
		return payload;
	}

	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
