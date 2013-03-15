package com.cpn.os4j.glance;

public class GlanceEndPointURL {
	private String glanceEndPointURL;
	private String glanceEndPointUserId;
	private String glanceEndPointPassword;
	
	public GlanceEndPointURL(String glanceEndPointURL,String glanceEndPointUserId,String glanceEndPointPassword)
	{
		this.glanceEndPointURL=glanceEndPointURL;
		this.glanceEndPointUserId=glanceEndPointUserId;
		this.glanceEndPointPassword=glanceEndPointPassword;
	}

	public String getGlanceEndPointURL() {
		return glanceEndPointURL;
	}

	public String getGlanceEndPointUserId() {
		return glanceEndPointUserId;
	}

	public String getGlanceEndPointPassword() {
		return glanceEndPointPassword;
	}
	

	
}
