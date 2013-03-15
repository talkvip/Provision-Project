package com.cpn.vsp.provision.remediation;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpn.net.DNS;
import com.cpn.os4j.ComputeEndpoint;
import com.cpn.os4j.model.IPAddressPool;
import com.cpn.os4j.model.SerializedFile;
import com.cpn.os4j.model.Server;
import com.cpn.vsp.provision.CompositeException;
import com.cpn.vsp.provision.EndpointFactory;
import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.CertificateController;
import com.cpn.vsp.provision.certificate.CertificateSigningRequest;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.service.ServiceEndpoint;

@Service
public class InstanceProvisioningService {

	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	CertificateController certController;
	@Autowired
	EndpointFactory endPointFactory;
	@Autowired
	String managementHostName;
	@Autowired
	String vcgPoolName;

	private static final String managementConfPayload = "# firmware payload delivered management.conf file\ntls-client\npull\n\ndev tun\nnobind\n\nremote %%MANAGER_HOSTNAME%% 7000\n\nremote-random\nresolv-retry infinite\n\ntls-cipher AES256-SHA\nca   /etc/ca-bundle.pem\ncert /etc/identity/snap.cert\nkey  /etc/identity/snap.key\n\npersist-key\nmlock\n\ncomp-lzo no\n\ntls-timeout   10\nkeepalive 5 45\n\nverb 3\n";


	public synchronized void deprovionInstance(String aServerId) {
		ComputeEndpoint endpoint = endPointFactory.getComputeEndpoint();
		endpoint.deleteServer(aServerId);
	}


	public synchronized ProvisionedServer provisionInstance(Product aProduct) throws NumberFormatException, Exception {

		ComputeEndpoint endpoint = endPointFactory.getComputeEndpoint();
		IPAddressPool pool = endpoint.getIPAddressPoolByName(vcgPoolName);

		String address = pool.getIPAddresses().get(0).getIp();
		String hostName = DNS.getDomain(address);
		Certificate certificate = makeSnapCertificate(hostName);
		ComputeEndpoint ep = endPointFactory.getComputeEndpoint();
		Server server = ep.createServer("VCG@" + hostName, address, aProduct.getMetadata("image"), aProduct.getMetadata("flavor"), null, buildPayload(hostName, address, certificate));

		try {
			server = server.waitUntilRunning(120000);
			server.associateIp(address);
			ServiceEndpoint sep = new ServiceEndpoint(server);
			sep.setPersonality(buildPayload(hostName, address, certificate));
			
		} catch (Exception e) {
			try {
				server.delete();
			} catch (Exception e2) {
				CompositeException ce = new CompositeException();
				ce.add(e);
				ce.add(e2);
				throw ce;
			}
			throw e;
		}
		return new ProvisionedServer(server, certificate, address);
	}

	public Certificate makeSnapCertificate(String aHostName) throws UnknownHostException {
		Certificate snapSigner = entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Certificate.Role.CLOUD_GATEWAY.value).getSingleResult();
		Certificate certificate = new Certificate();
		certificate.getSubject().setCountryName("US");
		certificate.getSubject().setState("CA");
		certificate.getSubject().setLocality("El Segundo");
		certificate.getSubject().setOrganization("ClearPath Networks");
		certificate.getSubject().setOrganizationalUnit("VSP");
		certificate.getSubject().setCommonName(aHostName);
		certificate.getSubject().setEmailAddress("certs@clearpathnet.com");
		certificate.getSubject().setSubjectAltName("DNS:" + aHostName);
		certificate.getSubject().setPathlen(-1);
		certificate.getSubject().setCA(false);
		certificate.setSelfSigned(false);
		certificate.setDaysValidFor(7300);
		certificate.setRole(0);

		certificate = certController.add(certificate);
		CertificateSigningRequest csr = certController.getCSRForCert(certificate);
		csr.setSigner(snapSigner);
		certificate = certController.signCSR(csr);
		return certificate;
	}

	private String getCABundle() {
		return Certificate.buildCABundle(getAllSigners());
	}

	private Set<Certificate> getAllSigners() {
		Set<Certificate> signers = new HashSet<>();
		signers.addAll(entityManager.createQuery("from Certificate where role > 0", Certificate.class).getResultList());
		return signers;
	}


	private List<SerializedFile> buildPayload(String aHostName, String anIpAddress, Certificate aCertificate) {
		Set<Certificate> caErs = Certificate.resolveAllSigners(getAllSigners());

		List<SerializedFile> personality = new ArrayList<>();

		personality.add(new SerializedFile("/etc/network/management.conf", managementConfPayload.replaceAll("%%MANAGER_HOSTNAME%%", managementHostName)));
		for (Certificate ca : caErs) {
			personality.add(new SerializedFile("/etc/ipsec.d/cacerts/" + ca.getSubject().getCommonName() + ".cert", ca.getCert()));
		}
		personality.add(new SerializedFile("/etc/ca-bundle.pem", getCABundle()));
		personality.add(new SerializedFile("/rom/deviceInfo", anIpAddress));
		personality.add(new SerializedFile("/etc/identity/snap.cert", aCertificate.getCert()));
		personality.add(new SerializedFile("/etc/identity/snap.key", aCertificate.getPrivateKey()));
		return personality;
	}
}
