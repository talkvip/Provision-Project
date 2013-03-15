package com.cpn.os4j;

public class RebootRequest {
	String type;

	public static final String HARD = "HARD";
	public static final String SOFT = "SOFT";

	public RebootRequest() {
	}

	public RebootRequest(String aType) {
		type = aType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}