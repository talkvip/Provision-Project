package com.cpn.vsp.provision.server;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.os4j.model.Server;
import com.cpn.vsp.provision.EndpointFactory;
import com.cpn.vsp.provision.service.Service;
import com.cpn.vsp.provision.service.ServiceDescription;
import com.cpn.vsp.provision.service.ServiceEndpoint;

@Controller
@RequestMapping(value = "/server")
@Transactional
public class ServerController {

	@Autowired
	EndpointFactory endPointFactory;

	@PersistenceContext
	EntityManager entityManager;

	@RequestMapping(method = RequestMethod.GET)
	@Logged
	public @ResponseBody
	List<Server> listServers() throws IOException {
		return endPointFactory.getComputeEndpoint().listServers();
	}

	@RequestMapping(value = "/{serverId}", method = RequestMethod.DELETE)
	@Logged
	public @ResponseBody
	void terminateInstance(@PathVariable("serverId") String serverId) throws Exception {
		endPointFactory.getComputeEndpoint().deleteServer(serverId);
	}

	@RequestMapping(value = "/{serverId}/service", method = RequestMethod.GET)
	@Logged
	public @ResponseBody
	List<Service> listServices(@PathVariable("serverId") String serverId) throws Exception {
		Server server = endPointFactory.getComputeEndpoint().getServerDetails(serverId);
		ServiceEndpoint endpoint = new ServiceEndpoint(server);
		return endpoint.listServices();
	}

	@RequestMapping(value = "/{serverId}/service", method = RequestMethod.POST)
	@Logged
	public @ResponseBody
	Service addService(@PathVariable("serverId") String serverId, @RequestBody ServiceDescription aServiceDescription) throws Exception {
		Server server = endPointFactory.getComputeEndpoint().getServerDetails(serverId);
		ServiceEndpoint endpoint = new ServiceEndpoint(server);
		Service service = endpoint.addService(aServiceDescription);
		entityManager.persist(service);
		return service;
	}

	@RequestMapping(value = "/{serverId}/service/{serviceId}", method = RequestMethod.PUT)
	@Logged
	public @ResponseBody
	Service updateService(@PathVariable("serverId") String serverId, @PathVariable("serviceId") String serviceId, @RequestBody ServiceDescription aServiceDescription) throws Exception {
		Server server = endPointFactory.getComputeEndpoint().getServerDetails(serverId);
		ServiceEndpoint endpoint = new ServiceEndpoint(server);
		Service service = entityManager.find(Service.class, serviceId);
		service.setDescription(aServiceDescription);
		service = endpoint.updateService(service);
		entityManager.merge(service);
		return service;
	}

	@RequestMapping(value = "/{serverId}/service/{serviceId}", method = RequestMethod.DELETE)
	@Logged
	public @ResponseBody
	void deleteService(@PathVariable("serverId") String aServerId, @PathVariable("serviceId") String aServiceId) {
		Server server = endPointFactory.getComputeEndpoint().getServerDetails(aServerId);
		ServiceEndpoint endpoint = new ServiceEndpoint(server);
		Service service = entityManager.find(Service.class, aServiceId);
		endpoint.deleteService(service);
		entityManager.remove(service);
	}
}
