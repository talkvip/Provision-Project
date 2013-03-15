package com.cpn.os4j;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.cpn.os4j.model.Network;

public class NetworkResponse {

	Network network;
	List<Network> networks;
	public Network getNetwork() {
		return network;
	}
	public void setNetwork(Network network) {
		this.network = network;
	}
	public List<Network> getNetworks() {
		return networks;
	}
	public void setNetworks(List<Network> networks) {
		this.networks = networks;
	}
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("network", network).append("networks", networks);
		return builder.toString();
	}

}