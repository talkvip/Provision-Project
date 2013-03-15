package com.cpn.os4j.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServerRequest {

	@JsonProperty("server")
	ServerConfiguration serverConfig;

	public ServerRequest() {
	}

	public ServerRequest(ServerConfiguration aConfig) {
		setServerConfig(aConfig);
	}

	public ServerConfiguration getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(ServerConfiguration serverConfig) {
		this.serverConfig = serverConfig;
	}
}