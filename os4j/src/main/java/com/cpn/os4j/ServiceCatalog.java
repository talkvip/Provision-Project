package com.cpn.os4j;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import com.cpn.os4j.model.Access;
import com.cpn.os4j.model.AccessResponse;

public class ServiceCatalog {

	private static RestTemplate restTemplate = new RestTemplate();
	private OpenStackCredentials credentials;
	private String serverUrl;

	public ServiceCatalog(String aServerUrl, OpenStackCredentials someCredentials) {
		serverUrl = aServerUrl;
		credentials = someCredentials;
	}
	public HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		return headers;
	}
	public Access getAccess() {
		return restTemplate.postForEntity(serverUrl + "/v2.0/tokens", credentials, AccessResponse.class).getBody().getAccess();
	}
}