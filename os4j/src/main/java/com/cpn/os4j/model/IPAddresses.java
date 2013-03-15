package com.cpn.os4j.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class IPAddresses implements Serializable {


	private static final long serialVersionUID = 1892397415639139533L;
	@JsonProperty("public")
	List<IPAddress> publicAddresses;
	@JsonProperty("private")
	List<IPAddress> privateAddresses;
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("publicAddresses", publicAddresses).append("privateAddresses", privateAddresses);
		return builder.toString();
	}
	public List<IPAddress> getPublicAddresses() {
		return publicAddresses;
	}
	public void setPublicAddresses(List<IPAddress> publicAddresses) {
		this.publicAddresses = publicAddresses;
	}
	public List<IPAddress> getPrivateAddresses() {
		return privateAddresses;
	}
	public void setPrivateAddresses(List<IPAddress> privateAddresses) {
		this.privateAddresses = privateAddresses;
	}

}