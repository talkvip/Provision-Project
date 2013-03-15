package com.cpn.vsp.provision.resource;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.vsp.provision.certificate.Certainly;
import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.Certificate.Role;
import com.cpn.vsp.provision.certificate.CertificateController;
import com.cpn.vsp.provision.certificate.CertificateSigningRequest;
import com.cpn.vsp.provision.certificate.PKCS12Certificate;
import com.cpn.vsp.provision.certificate.PKCS12CertificateRequest;
import com.cpn.vsp.provision.image.GlanceFirmwareImage;
import com.cpn.vsp.provision.mobile.MobileConfig;
import com.cpn.vsp.provision.pool.ResourcePool;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.server.ServerController;
import com.cpn.vsp.provision.service.Service;
import com.cpn.vsp.provision.service.ServiceDescription;
import com.cpn.vsp.provision.strategy.provision.PayloadGenerator;
import com.cpn.vsp.provision.strategy.provision.ResourceProvisioningStrategy;
import com.cpn.vsp.provision.strategy.provision.VirtualCloudGatewayProvisioningStrategy;

@Controller
@RequestMapping(value = "/resource")
@Transactional
@NamedQueries(value = { @NamedQuery(name = "resourceBySerialKey", query = "from PhysicalResource where s") })
public class ResourceController {

	@Autowired
	ApplicationContext context;

	// @Autowired
	// EndPoint endPoint;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	Certainly certainly;

	@Autowired
	CertificateController certificateController;

	@Autowired
	ServerController serverController;

	@Autowired
	ResourcePool resourcePool;

	private ResourceProvisioningStrategy getProvisioningStrategy(Product aProduct) throws BeansException, ClassNotFoundException {
		ResourceProvisioningStrategy strategy = (ResourceProvisioningStrategy) context.getBean(Class.forName(aProduct.getProvisioningStrategy()));
		if (strategy == null) {
			throw new RuntimeException("I don't know about the provisioning strategy: " + aProduct.getProvisioningStrategy());
		}
		return strategy;
	}

	@RequestMapping(method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	Resource[] listResources() throws IOException {
		return entityManager.createQuery("from Resource", Resource.class).getResultList().toArray(new Resource[0]);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	<T extends Resource> T newResource(@RequestBody final Product aProduct) throws Exception {
		if (getProvisioningStrategy(aProduct) instanceof VirtualCloudGatewayProvisioningStrategy) {
			return (T) resourcePool.deplete(aProduct);
		} else {
			return (T) getProvisioningStrategy(aProduct).provision(aProduct);
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	@Transactional
	public @ResponseBody
	Resource updateDevice(@RequestBody Resource aResource) throws Exception {
		return entityManager.merge(aResource);
	}

	@RequestMapping(value = "/{aResourceId}", method = RequestMethod.GET)
	public @ResponseBody
	Resource queryDevice(@PathVariable int aResourceId) throws IOException {
		return entityManager.find(Resource.class, aResourceId);
	}

	@RequestMapping(value = "/physical", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	PhysicalResource newPhysicalResource(@RequestBody PhysicalResource aResource) {
		entityManager.persist(aResource);
		return aResource;
	}

	private Set<Certificate> getAllSigners() {
		Set<Certificate> signers = new HashSet<>();
		signers.addAll(entityManager.createQuery("from Certificate where role > 0", Certificate.class).getResultList());
		return signers;
	}

	private String getPayloadCABundle() {
		return Certificate.buildCABundle(getAllSigners());
	}

	@RequestMapping(value = "/physical/{serialKey}/signCSR", method = RequestMethod.PUT)
	@Transactional
	public @ResponseBody
	byte[] signResourceCSR(@RequestBody String aCertificateSigningRequest, @PathVariable String serialKey) {
		PhysicalResource resource = entityManager.createQuery("from PhysicalResource where serialKey = ?", PhysicalResource.class).setParameter(1, serialKey).getSingleResult();
		CertificateSigningRequest csr = new CertificateSigningRequest(aCertificateSigningRequest);
		csr.setSigner(entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Role.CLOUD_GATEWAY.value).getSingleResult());
		Certificate signee = new Certificate();
		signee.setDaysValidFor(7300);
		csr.setSignee(signee);
		signee.getSubject().setCA(false);
		signee.setSelfSigned(false);
		String cert = certainly.signCertificateSigningRequest(csr).getCert();
		return context.getBean(PayloadGenerator.class).buildPayload(resource.getHostName(), resource.getIpAddress(), cert, Certificate.resolveAllSigners(getAllSigners()), getPayloadCABundle());
	}

	@RequestMapping(value = "/physical/{serialKey}/password", method = RequestMethod.PUT)
	@Transactional
	public @ResponseBody
	void setPassword(@PathVariable String serialKey, @RequestBody String aPassword) {
		PhysicalResource r = entityManager.createQuery("from PhysicalResource where serialKey = ?", PhysicalResource.class).setParameter(1, serialKey).getSingleResult();
		r.setPassword(aPassword);
		entityManager.merge(r);
	}

	@RequestMapping(value = "/physical/{serialKey}/password", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	String getPassword(@PathVariable String serialKey) {
		PhysicalResource r = entityManager.createQuery("from PhysicalResource where serialKey = ?", PhysicalResource.class).setParameter(1, serialKey).getSingleResult();
		return r.getPassword();
	}

	@RequestMapping(value = "/physical/{serialKey}/hostname", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	String getHostname(@PathVariable String serialKey) {
		return entityManager.createQuery("from PhysicalResource where serialKey = ?", PhysicalResource.class).setParameter(1, serialKey).getSingleResult().getHostName();
	}

	@RequestMapping(value = "/physical", method = RequestMethod.PUT)
	@Transactional
	public @ResponseBody
	PhysicalResource updatePhysicalResource(@RequestBody PhysicalResource aResource) {
		return entityManager.merge(aResource);
	}

	@RequestMapping(value = "/physical/{serialKey}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	PhysicalResource queryPhysicalDevice(@PathVariable("serialKey") String serialKey) throws IOException {
		return entityManager.createQuery("from PhysicalResource where serialKey = ?", PhysicalResource.class).setParameter(1, serialKey).getSingleResult();
	}

	@RequestMapping(value = "/physical/{serialKey}", method = RequestMethod.DELETE)
	@Transactional
	public @ResponseBody
	void deletePhysicalDevice(@PathVariable("serialKey") String serialKey) throws IOException {
		entityManager.createQuery("delete from PhysicalResource where serialKey = ?").setParameter(1, serialKey).executeUpdate();
	}

	@RequestMapping(value = "/physical/{serialKey}/firmware", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	void getFirmware(@PathVariable("serialKey") String serialKey, HttpServletResponse aResponse) throws IOException {
		PhysicalResource resource = entityManager.createQuery("from PhysicalResource where serialKey = ?", PhysicalResource.class).setParameter(1, serialKey).getSingleResult();
		GlanceFirmwareImage image = entityManager.find(GlanceFirmwareImage.class, Integer.parseInt(resource.getProduct().getMetadata("firmware")));
		image.download(aResponse.getOutputStream());
	}

	@RequestMapping(value = "/physical/{serialKey}/firmware/checksum", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	String getFirmwareChecksum(@PathVariable("serialKey") String serialKey) throws IOException {
		PhysicalResource resource = entityManager.createQuery("from PhysicalResource where serialKey = ?", PhysicalResource.class).setParameter(1, serialKey).getSingleResult();
		GlanceFirmwareImage image = entityManager.find(GlanceFirmwareImage.class, Integer.parseInt(resource.getProduct().getMetadata("firmware")));
		return image.getChecksum() + " " + image.getSize();
	}

	@RequestMapping(value = "/{resourceId}", method = RequestMethod.DELETE)
	@Transactional
	public @ResponseBody
	void terminateResouce(@PathVariable("resourceId") String aResourceId) throws Exception {
		Resource resource = entityManager.find(Resource.class, aResourceId);
		resourcePool.clearOut(resource);
		getProvisioningStrategy(resource.getProduct()).deprovision(resource);
	}

	@RequestMapping(value = "/{resourceId}/mobileconfig", method = RequestMethod.GET)
	public void getMobileConfig(@PathVariable("resourceId") int aResourceId, HttpServletResponse aResponse) throws TransformerException, IOException {
		VirtualResource resource = entityManager.find(VirtualResource.class, aResourceId);
		Certificate signer = entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role)", Certificate.class).setParameter("role", Certificate.Role.USER.value).getSingleResult();
		Certificate identity = new Certificate();
		identity.setSelfSigned(false);
		identity.setDaysValidFor(365);
		identity.getSubject().put("C", "US");
		identity.getSubject().put("ST", "California");
		identity.getSubject().put("O", "ClearPath Networks");
		identity.getSubject().put("OU", "Engineering Department");
		identity.getSubject().put("CN", "VPN User Cert for " + resource.getHostName());
		identity.getSubject().put("L", "El Segundo");
		identity.getSubject().put("emailAddress", "certs@clearpathnet.com");
		identity.getSubject().put("subjectAltName", "email:copy");
		identity.getSubject().setCA(false);
		identity.setRole(0);
		identity = entityManager.find(Certificate.class, certificateController.sign(signer, identity).getId());
		Set<Certificate> chains = new HashSet<>();
		chains.add(identity);
		PKCS12Certificate id = (certainly.pkcs12(new PKCS12CertificateRequest().setCertificate(identity).setCaBundle(identity.buildSignerChain())));
		Set<Certificate> caBundle = new HashSet<>();
		for (Certificate c : resource.getCertificate().getSignerChain()) {
			caBundle.add(c);
		}

		MobileConfig config = new MobileConfig("VPN Mobile Config for " + resource.getProduct().getName() + "@" + resource.getHostName(), id, caBundle, "com.cpn.vsp.vpn.mobileconfig", "ClearPath Networks", "VPN Mobile Config for "
				+ resource.getProduct().getName() + "@" + resource.getHostName(), "VPN for " + resource.getProduct().getName() + "@" + resource.getHostName(), resource.getHostName(), "clearpathnet", "password", "baldeagle");
		config.toString();

		ByteArrayInputStream stream = new ByteArrayInputStream(certainly.sign(getCert(), getPrivateKey(), getCABundle(), config.toString().getBytes()));
		aResponse.setContentType("application/x-apple-aspen-config");
		aResponse.setHeader("Content-Disposition", "attachment; filename=" + resource.getHostName() + ".mobileconfig");
		IOUtils.copy(stream, aResponse.getOutputStream());
		aResponse.flushBuffer();
	}

	@RequestMapping(value = "/{resourceId}/service", method = RequestMethod.GET)
	@Logged
	public @ResponseBody
	List<Service> addService(@PathVariable("resourceId") String resourceId) throws Exception {
		VirtualResource resource = entityManager.find(VirtualResource.class, resourceId);
		return serverController.listServices(resource.getServerId());
	}

	@RequestMapping(value = "/{resourceId}/service/{serviceId}", method = RequestMethod.PUT)
	@Logged
	public @ResponseBody
	Service updateService(@PathVariable("resourceId") String resourceId, @PathVariable("serviceId") String aServiceId, @RequestBody ServiceDescription aServiceDescription) throws Exception {
		VirtualResource resource = entityManager.find(VirtualResource.class, resourceId);
		return serverController.updateService(resource.getServerId(), aServiceId, aServiceDescription);
	}

	@RequestMapping(value = "/{resourceId}/service/{serviceId}", method = RequestMethod.DELETE)
	@Logged
	public @ResponseBody
	void deleteService(@PathVariable("resourceId") String resourceId, @PathVariable("serviceId") String aServiceId) throws Exception {
		VirtualResource resource = entityManager.find(VirtualResource.class, resourceId);
		serverController.deleteService(resource.getServerId(), aServiceId);
	}

	@RequestMapping(value = "/{resourceId}/service", method = RequestMethod.POST)
	@Logged
	public @ResponseBody
	Service addService(@PathVariable("resourceId") String resourceId, @RequestBody ServiceDescription aServiceDescription) throws Exception {
		VirtualResource resource = entityManager.find(VirtualResource.class, resourceId);
		return serverController.addService(resource.getServerId(), aServiceDescription);
	}

	private static String getResource(String aFileName) throws IOException {
		InputStream ins = "".getClass().getClassLoader().getResourceAsStream(aFileName);
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		String line = null;
		String whole = "";
		while ((line = in.readLine()) != null) {
			whole += line + "\n";
		}
		return whole;
	}

	private static String getCABundle() {
		try {
			return getResource("certs/mobileConfigTrustedSignerCABundle.crt");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String getPrivateKey() {
		try {
			return getResource("certs/mobileConfigTrustedSignerKey.pem");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String getCert() {
		try {
			return getResource("certs/mobileConfigTrustedSignerCert.crt");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
