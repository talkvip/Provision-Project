package com.cpn.vsp.provision.remediation;

import com.cpn.os4j.model.Server;
import com.cpn.vsp.provision.certificate.Certificate;

public class ProvisionedServer {
	private Server server;
	private Certificate certificate;
	private String ipAddress;

	public ProvisionedServer(Server aServer, Certificate aCertificate, String anAddress) {

		this.server = aServer;
		this.certificate = aCertificate;
		ipAddress = anAddress;

	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
