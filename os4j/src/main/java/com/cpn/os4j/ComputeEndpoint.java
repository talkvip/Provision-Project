package com.cpn.os4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.cpn.logging.Logged;
import com.cpn.os4j.command.RestCommand;
import com.cpn.os4j.model.Flavor;
import com.cpn.os4j.model.FlavorsResponse;
import com.cpn.os4j.model.FullServerConfiguration;
import com.cpn.os4j.model.IPAddress;
import com.cpn.os4j.model.IPAddressPool;
import com.cpn.os4j.model.IPAddressResponse;
import com.cpn.os4j.model.Image;
import com.cpn.os4j.model.ImagesResponse;
import com.cpn.os4j.model.SerializedFile;
import com.cpn.os4j.model.Server;
import com.cpn.os4j.model.ServerNameConfiguration;
import com.cpn.os4j.model.ServerRequest;
import com.cpn.os4j.model.ServerResponse;
import com.cpn.os4j.model.ServersResponse;
import com.cpn.os4j.model.Token;

public class ComputeEndpoint implements Serializable {

	private static final long serialVersionUID = -612911970872331536L;

	String serverUrl;
	Token token;

	public ComputeEndpoint(String aServerUrl, Token aToken) {
		super();
		token = aToken;
		serverUrl = aServerUrl;

	}

	public Token getToken() {
		return token;
	}

	public String getTenantId() {
		return token.getTenant().getId();
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public List<Image> listImages() {
		RestCommand<String, ImagesResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/images/detail");
		command.setResponseModel(ImagesResponse.class);
		return command.get().getImages();
	}

	public List<Server> listServers() {
		RestCommand<String, ServersResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/servers/detail");
		command.setResponseModel(ServersResponse.class);
		List<Server> servers = command.get().getServers();
		for (Server s : servers) {
			s.setComputeEndpoint(this);
		}
		return servers;
	}

	public Server getServerDetails(String aServerId) {
		RestCommand<String, ServerResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/servers/" + aServerId);
		command.setResponseModel(ServerResponse.class);
		return command.get().getServer().setComputeEndpoint(this);
	}

	public Server getServerDetails(Server aServer) {
		return getServerDetails(aServer.getId());
	}

	public Server renameServer(String aServerId, String aName) {
		RestCommand<ServerRequest, ServerResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/servers/" + aServerId);
		ServerNameConfiguration config = new ServerNameConfiguration();
		config.setName(aName);
		command.setRequestModel(new ServerRequest(config));
		command.setResponseModel(ServerResponse.class);
		return command.put().getServer().setComputeEndpoint(this);
	}

	public Server createServer(String aName, String anIpAddress, String anImageRef, String aFlavorRef, Map<String, String> someMetadata, List<SerializedFile> aPersonality) {
		if (someMetadata == null) {
			someMetadata = new HashMap<>();
		}
		if (aPersonality == null) {
			aPersonality = new ArrayList<>();
		}

		RestCommand<ServerRequest, ServerResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/servers");
		command.setRequestModel(new ServerRequest(new FullServerConfiguration(aName, anIpAddress, anImageRef, aFlavorRef, someMetadata, aPersonality)));
		command.setResponseModel(ServerResponse.class);
		return command.post().getServer().setComputeEndpoint(this);
	}

	public Server createServer(String aName, IPAddress anIpAddress, Image anImage, Flavor aFlavor, Map<String, String> someMetadata, List<SerializedFile> aPersonality) {
		return createServer(aName, anIpAddress.getIp(), anImage.getSelfRef(), aFlavor.getSelfRef(), someMetadata, aPersonality);
	}

	public Server rebootServer(Server aServer, boolean aHard) {
		return rebootServer(aServer.getId(), aHard);
	}

	public Server rebootServer(String aServerId, boolean aHard) {
		RestCommand<RebootRequest, String> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/servers/" + aServerId + "/action");
		command.setRequestModel(new RebootRequest(aHard ? RebootRequest.HARD : RebootRequest.SOFT));
		command.post();
		return getServerDetails(aServerId);
	}

	public Server associateIp(Server aServer, IPAddress anIpAddress) {
		return associateIp(aServer.getId(), anIpAddress.getIp());
	}

	public Server associateIp(String aServerId, String anIpAddress) {
		RestCommand<Map<String, Map<String, String>>, String> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/servers/" + aServerId + "/action");
		Map<String, Map<String, String>> request = new HashMap<>();
		Map<String, String> ip = new HashMap<>();
		ip.put("address", anIpAddress);
		request.put("addFloatingIp", ip);
		command.setRequestModel(request);
		command.post();
		return getServerDetails(aServerId);
	}

	public void deleteServer(Server aServer) {
		deleteServer(aServer.getId());
	}

	public void deleteServer(String anId) {
		RestCommand<String, String> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/servers/" + anId);
		command.delete();
	}

	public List<Flavor> listFlavors() {
		RestCommand<String, FlavorsResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/flavors/detail");
		command.setResponseModel(FlavorsResponse.class);
		return command.get().getFlavors();
	}

//	@Logged
	public IPAddress allocateIPAddress(IPAddressPool aPool) {
		RestCommand<Map<String, String>, IPAddressResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/os-floating-ips");
		command.setResponseModel(IPAddressResponse.class);
		Map<String, String> args = new HashMap<>();
		args.put("pool", aPool.getName());
		command.setRequestModel(args);
		return command.post().getIpAddress();
	}

	public List<IPAddress> listAddresses() {
		RestCommand<String, IPAddressResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/os-floating-ips");
		command.setResponseModel(IPAddressResponse.class);
		return command.get().getIpAddresses();
	}

	public List<IPAddressPool> listPools() {
		RestCommand<String, IPAddressResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/os-floating-ip-pools");
		command.setResponseModel(IPAddressResponse.class);
		List<IPAddressPool> pools = command.get().getPools();
		for (IPAddressPool p : pools) {
			p.setComputeEndpoint(this);
		}
		return pools;
	}

	public IPAddressPool getIPAddressPoolByName(String aName) {
		List<IPAddressPool> list = listPools();
		for (IPAddressPool p : list) {
			if (p.getName().equals(aName)) {
				return p;
			}
		}
		return null;
	}

	public IPAddress describeAddress(int anId) {
		RestCommand<String, IPAddressResponse> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/os-floating-ips/" + anId);
		command.setResponseModel(IPAddressResponse.class);
		return command.get().getIpAddress();
	}

	public void deallocateIpAddress(int anId) {
		RestCommand<String, String> command = new RestCommand<>(token);
		command.setPath(getServerUrl() + "/os-floating-ips/" + anId);
		command.delete();
	}

}