package com.cpn.vsp.provision.service;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.cpn.os4j.model.SerializedFile;
import com.cpn.os4j.model.Server;

public class ServiceEndpoint {

	Server server;

	public ServiceEndpoint(Server aServer) {
		server = aServer;
	}

	public List<Service> listServices() {
		return new RestTemplate().getForEntity("http://" + server.getAccessIPv4() + ":3000/services", ServiceList.class).getBody().getServices();
	}

	public Service addService(ServiceDescription aServiceDescription) {
		return new RestTemplate().postForEntity("http://" + server.getAccessIPv4() + ":3000/services", aServiceDescription, ServiceList.class).getBody().getService();
	}

	public Service updateService(Service aService) {
		return new RestTemplate().postForEntity("http://" + server.getAccessIPv4() + ":3000/services/" + aService.id, aService.getDescription(), ServiceList.class).getBody().getService();
	}

	public void deleteService(Service aService) {
		new RestTemplate().delete("http://" + server.getAccessIPv4() + ":3000/service/" + aService.id);
	}

	public void setPersonality(final List<SerializedFile> aPersonality) {
		ServerPersonality personality = new ServerPersonality();
		personality.setPersonality(aPersonality);
		new RestTemplate().postForEntity("http://" + server.getAccessIPv4() + ":3000/personality", personality, String.class);
	}
}
