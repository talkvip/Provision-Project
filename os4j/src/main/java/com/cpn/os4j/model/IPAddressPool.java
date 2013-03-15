package com.cpn.os4j.model;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.cpn.os4j.ComputeEndpoint;

public class IPAddressPool {

	String name = "default";

	@JsonIgnore
	private transient ComputeEndpoint computeEndpoint;


	public IPAddressPool() {
		// TODO Auto-generated constructor stub
	}

	public IPAddressPool(String aName) {
		name =aName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IPAddress> getIPAddresses() {
		List<IPAddress> addresses = getComputeEndpoint().listAddresses();
		Iterator<IPAddress> i = addresses.iterator();
		while (i.hasNext()) {
			IPAddress ip = i.next();
			if (!ip.getPool().equals(name) || (ip.getInstanceId() != null)) {
				i.remove();
			}
		}
		return addresses;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("name", name);
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IPAddressPool other = (IPAddressPool) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public ComputeEndpoint getComputeEndpoint() {
		return computeEndpoint;
	}

	public void setComputeEndpoint(ComputeEndpoint computeEndpoint) {
		this.computeEndpoint = computeEndpoint;
	}

}