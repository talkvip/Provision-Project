package com.cpn.os4j.command;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.cpn.os4j.model.Token;

public class RestCommand<Request, Response> {

	private static final RestTemplate restTemplate = new RestTemplate();
	private String path;
	private Request requestModel;
	private Class<Response> responseModel;

	private Token token;

	public RestCommand(Token aToken) {
		token = aToken;
	}

	public HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Auth-Token", token.getId());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public Response get() {
		return restTemplate.exchange(getPath(), HttpMethod.GET, new HttpEntity<String>(getHttpHeaders()), getResponseModel()).getBody();
	}

	public Response post() {
		return restTemplate.exchange(getPath(), HttpMethod.POST, new HttpEntity<Request>(getRequestModel(), getHttpHeaders()), getResponseModel()).getBody();
	}

	public Response put() {
		return restTemplate.exchange(getPath(), HttpMethod.PUT, new HttpEntity<Request>(getRequestModel(), getHttpHeaders()), getResponseModel()).getBody();
	}


	public void delete() {
		if (getRequestModel() == null) {
			restTemplate.exchange(getPath(), HttpMethod.DELETE, new HttpEntity<String>(getHttpHeaders()), null);
		} else {
			restTemplate.exchange(getPath(), HttpMethod.DELETE, new HttpEntity<Request>(getRequestModel(), getHttpHeaders()), null);
		}
	}



	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Request getRequestModel() {
		return requestModel;
	}

	public void setRequestModel(Request requestModel) {
		this.requestModel = requestModel;
	}

	public Class<Response> getResponseModel() {
		return responseModel;
	}

	public void setResponseModel(Class<Response> responseModel) {
		this.responseModel = responseModel;
	}

}