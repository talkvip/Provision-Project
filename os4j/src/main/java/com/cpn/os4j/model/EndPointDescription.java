package com.cpn.os4j.model;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class EndPointDescription {
	List<Map<String, String>> endpoints;
	@JsonProperty("endpoints_links")
	List<String> endpointLinks;
	String name;
	String type;


	public List<Map<String, String>> getEndpoints() {
		return endpoints;
	}
	public void setEndpoints(List<Map<String, String>> endpoints) {
		this.endpoints = endpoints;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	public List<String> getEndpointLinks() {
		return endpointLinks;
	}

	public void setEndpointLinks(List<String> endpointLinks) {
		this.endpointLinks = endpointLinks;
	}


}