package com.cpn.os4j.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cpn.os4j.ComputeEndpoint;
import com.cpn.os4j.NetworkEndpoint;

public class Access {
	List<EndPointDescription> serviceCatalog;
	Token token;
	User user;

	public boolean localhostHack = false;

	@JsonIgnore
	public NetworkEndpoint getNetworkEndpoint(String aRegion, String endPointType){
		for (EndPointDescription d : serviceCatalog) {
			if (d.getType().equals("network")) {
				for (Map<String, String> urls : d.getEndpoints()) {
					if (urls.get("region").equals(aRegion)) {
						if (localhostHack) {
							return new NetworkEndpoint(urls.get(endPointType).replaceAll("192\\.168\\.31\\.38", "control.dev.intercloud.net"), token);
						} else {
							return new NetworkEndpoint(urls.get(endPointType), token);
						}
					}
				}
			}
		}
		throw new RuntimeException("Couldn't find the NetworkEndpoint for region: " + aRegion + " and type: " + endPointType);
	}

	@JsonIgnore
	public ComputeEndpoint getComputeEndpoint(String aRegion, String endPointType) {
		for (EndPointDescription d : serviceCatalog) {
			if (d.getType().equals("compute")) {
				for (Map<String, String> urls : d.getEndpoints()) {
					if (urls.get("region").equals(aRegion)) {
						if (localhostHack) {
							return new ComputeEndpoint(urls.get(endPointType).replaceAll("192\\.168\\.31\\.38", "control.dev.intercloud.net"), token);
						} else {
							return new ComputeEndpoint(urls.get(endPointType), token);
						}
					}
				}
			}
		}
		throw new RuntimeException("Couldn't find the ComputeEndpoint for region: " + aRegion + " and type: " + endPointType);
	}

	public List<EndPointDescription> getServiceCatalog() {
		return serviceCatalog;
	}

	public void setServiceCatalog(List<EndPointDescription> serviceCatalog) {
		this.serviceCatalog = serviceCatalog;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}