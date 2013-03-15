package com.cpn.vsp.provision.certificate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.vsp.provision.certificate.Certificate.Role;

@Controller
@RequestMapping(value = "/certificate")
public class CertificateController {

	@PersistenceContext
	public EntityManager entityManager;

	@Autowired
	private Certainly certainly;

	private static <T extends Certificate> List<Certificate> makeSafe(List<T> aList) {
		List<Certificate> results = new ArrayList<>();
		for (Certificate c : aList) {
			results.add(new PublicCertificate(c));
		}
		return results;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	@Transactional
	@Logged
	public @ResponseBody
	List<Certificate> list() throws IOException {
		return makeSafe(entityManager.createQuery("from Certificate", Certificate.class).getResultList());
	}

	@RequestMapping(value = "/signers", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	List<Certificate> listSigners() throws IOException {
		return makeSafe(entityManager.createQuery("from Certificate where subject.CA = true", Certificate.class).getResultList());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	Certificate show(@PathVariable String id) throws IOException {
		return new PublicCertificate(entityManager.find(Certificate.class, id));
	}

	@RequestMapping(value = "/role/{role}/{context}/signCSR")
	@Transactional
	public @ResponseBody Certificate actionByRole(@PathVariable(value = "role") String aRole, @PathVariable(value="context") String aContext, @RequestBody CertificateSigningRequest aCSR){
		Role role;
		switch (aRole) {
		case "gateway":
		case "snap":
			role = Role.CLOUD_GATEWAY;
			break;
		case "manager":
			role = Role.MANAGER;
			break;
		case "logger":
			role = Role.LOGGER; 
			break;
		case "user":
			role = Role.USER;
			break;
		default:
			role = Role.CLOUD_GATEWAY;
			break;
		}

		Certificate cert = entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", role.getValue()).getSingleResult();
		aCSR.setSigner(cert);
		aCSR.getSignee().setDownstream(true);
		return signCSR(aCSR);
	}


	@RequestMapping(value = "/role/{role}/{context}")
	@Transactional
	@Logged
	public void getCertForRole(@PathVariable(value = "role") String aRole, @PathVariable(value = "context") String aContext, HttpServletResponse aResponse) throws IOException {
		Role role;
		switch (aRole) {
		case "gateway":
		case "snap":
			role = Role.CLOUD_GATEWAY;
			break;
		case "manager":
			role = Role.MANAGER;
			break;
		case "logger":
			role = Role.LOGGER; 
			break;
		case "user":
			role = Role.USER;
			break;
		default:
			role = Role.CLOUD_GATEWAY;
			break;
		}

		Certificate cert = entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", role.getValue()).getSingleResult();
		switch (aContext) {
		case "cert":
			showCertificate(cert.getId(), aResponse);
			return;
		case "privateKey":
			showPrivateKey(cert.getId(), aResponse);
			return;
		case "signerBundle":
			bundleSignerChain(cert.getId(), aResponse);
			return;
		}

	}

	@RequestMapping(value = "/{id}/exposed", method = RequestMethod.GET)
	@Transactional
	@Logged
	public @ResponseBody
	Certificate showEverything(@PathVariable String id) throws IOException {
		return entityManager.find(Certificate.class, id);
	}

	@RequestMapping(value = "/{id}/cert", method = RequestMethod.GET, produces = "application/x-pem-file")
	@Transactional
	@Logged
	public void showCertificate(@PathVariable String id, HttpServletResponse aResponse) throws IOException {
		Certificate cert = entityManager.find(Certificate.class, id);
		ByteArrayInputStream stream = new ByteArrayInputStream(cert.getCert().getBytes());
		aResponse.setContentType("application/x-pem-file");
		aResponse.setHeader("Content-Disposition", "attachment; filename=" + cert.getSubject().getCommonName() + ".cert");
		IOUtils.copy(stream, aResponse.getOutputStream());
		aResponse.flushBuffer();
	}

	@RequestMapping(value = "/{id}/privateKey", method = RequestMethod.GET, produces = "application/x-pem-file")
	@Transactional
	@Logged
	public @ResponseBody
	void showPrivateKey(@PathVariable String id, HttpServletResponse aResponse) throws IOException {
		Certificate cert = entityManager.find(Certificate.class, id);
		ByteArrayInputStream stream = new ByteArrayInputStream(cert.getPrivateKey().getBytes());
		aResponse.setContentType("application/x-pem-file");
		aResponse.setHeader("Content-Disposition", "attachment; filename=" + cert.getSubject().getCommonName() + ".key");
		IOUtils.copy(stream, aResponse.getOutputStream());
		aResponse.flushBuffer();
	}

	public Certificate sign(Certificate signer, Certificate signee) throws UnknownHostException {
		return signCSR(getCSRForCert(add(signee)).setSignerFluent(signer));
	}

	@Transactional
	@RequestMapping(value = "/{id}/signerBundleChain", method = RequestMethod.GET)
	@Logged
	public void bundleSignerChain(@PathVariable String id, HttpServletResponse aResponse) throws IOException {
		Certificate cert = entityManager.find(Certificate.class, id);
		String signers = Certificate.buildCABundle(cert.getSigningChain());
		ByteArrayInputStream stream = new ByteArrayInputStream(signers.getBytes());
		aResponse.setContentType("application/x-pem-file");
		aResponse.setHeader("Content-Disposition", "attachment; filename=caBundle.cert");
		IOUtils.copy(stream, aResponse.getOutputStream());
		aResponse.flushBuffer();
	}

	@Transactional
	@RequestMapping(value = "/{id}/signerBundle", method = RequestMethod.GET)
	@Logged
	public void bundleSigners(@PathVariable String id, HttpServletResponse aResponse) throws IOException {
		Certificate cert = entityManager.find(Certificate.class, id);
		String signers = Certificate.buildCABundle(cert.getSignerChain());
		ByteArrayInputStream stream = new ByteArrayInputStream(signers.getBytes());
		aResponse.setContentType("application/x-pem-file");
		aResponse.setHeader("Content-Disposition", "attachment; filename=caBundle.cert");
		IOUtils.copy(stream, aResponse.getOutputStream());
		aResponse.flushBuffer();
	}

	@Transactional
	@RequestMapping(value = "", method = RequestMethod.POST)
	@Logged
	public @ResponseBody
	Certificate add(@RequestBody Certificate aCert) throws UnknownHostException {

		if (aCert.getSigner() == null || "-1".equals(aCert.getSigner().getId())) {
			aCert.setSigner(null);
			aCert.setSelfSigned(true);
			return genCA(aCert);
		} else {
			aCert = certainly.populateCertificate(aCert);
			aCert.setCertificateProvider(null);
			entityManager.persist(aCert);
			return signCSR(getCSRForCert(aCert));
		}
	}

	@Transactional
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@Logged
	public @ResponseBody
	void delete(@PathVariable String id) {
		entityManager.remove(entityManager.find(Certificate.class, id));
	}

	@Transactional
	@RequestMapping(value = { "", "/{id}" }, method = RequestMethod.PUT)
	@Logged
	public @ResponseBody
	Certificate update(@RequestBody Certificate aT) {
		aT.setPrivateKey(entityManager.find(Certificate.class, aT.getId()).getPrivateKey());
		return entityManager.merge(aT);
	}

	
	private void cleanCertificate(Certificate aCert, CertificateProvider aProvider) {
		if (aCert.getSigner() != null) {
			Certificate signer = entityManager.find(Certificate.class, aCert.getSigner().getId());
			if (signer != null) {
				aCert.setSigner(signer);
			} else {
				cleanCertificate(aCert.getSigner(), aProvider);
				aCert.getSigner().setCertificateProvider(aProvider);
				entityManager.persist(aCert.getSigner());
			}
		}
		aCert.getSubject().setId("0");
		aCert.setUpstream(true);
	}

	@Transactional
	public void refreshRemoteCertificates(CertificateProvider aProvider) {
		for (Certificate c : aProvider.getRemoteCertificates()) {
			if (entityManager.find(Certificate.class, c.getId()) == null) {
				cleanCertificate(c, aProvider);
				entityManager.persist(c);
			}
		}
	}

	@Transactional
	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, String> refresh() {
		for (CertificateProvider s : entityManager.createQuery("from CertificateProvider", CertificateProvider.class).getResultList()) {
			refreshRemoteCertificates(s);
		}
		Map<String, String> result = new HashMap<>();
		result.put("success", "true");
		return result;
	}

	@RequestMapping(value = "/ca", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	Certificate genCA(@RequestBody Certificate aCert) throws UnknownHostException {
		aCert = certainly.generateSelfSigned(aCert);
		aCert.setCertificateProvider(null);
		entityManager.persist(aCert);
		return new PublicCertificate(aCert);
	}

	@Transactional
	public CertificateSigningRequest getCSRForCert(Certificate cert) {
		cert = entityManager.find(Certificate.class, cert.getId());
		CertificateSigningRequest csr = certainly.generateCertificateSigningRequest(cert);
		csr.setSignee(new PublicCertificate(csr.getSignee()));
		csr.setSigner(new PublicCertificate(cert.getSigner()));
		return csr;
	}

	@Transactional
	@RequestMapping(value = "/signCSR", method = RequestMethod.POST)
	public @ResponseBody
	Certificate signCSR(@RequestBody CertificateSigningRequest csr) {
		if (!csr.getSignee().isDownstream()) {
			csr.setSignee(entityManager.find(Certificate.class, csr.getSignee().getId()));
		}
		csr.setSigner(entityManager.find(Certificate.class, csr.getSigner().getId()));
		Certificate signedCert = null;
		if (csr.getSigner().isUpstream()) {
			signedCert = csr.getSigner().getCertificateProvider().signCertificateSigningRequest(csr);
		  signedCert.setPrivateKey(csr.getSignee().getPrivateKey());
		} else {
			signedCert = certainly.signCertificateSigningRequest(csr);
		}

		if (!csr.getSignee().isDownstream()) {
			signedCert.setSigner(csr.getSigner());
			signedCert.setDownstream(false);
			signedCert = entityManager.merge(signedCert);
			return signedCert;
		}
		return signedCert;
	}
}
