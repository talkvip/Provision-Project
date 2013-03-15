package com.cpn.os4j;

import java.util.List;

import com.cpn.os4j.command.RestCommand;
import com.cpn.os4j.model.Network;
import com.cpn.os4j.model.Token;

public class NetworkEndpoint {

	String serverUrl;
	Token token;

	public NetworkEndpoint(String aServerUrl, Token aToken) {
		super();
		token = aToken;
		serverUrl = aServerUrl;
	}

	public String getTenantId() {
		return token.getTenant().getId();
	}

	public String getServerUrl() {
		return serverUrl;
	}


	public List<Network> listNetworks() {
		RestCommand<String, NetworkResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/tenants/"+getTenantId() + "/networks/detail");
		command.setResponseModel(NetworkResponse.class);
		return command.get().getNetworks();
	}
}