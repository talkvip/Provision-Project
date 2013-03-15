package com.cpn.vsp.provision.mobile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.CertificateController;
import com.cpn.vsp.provision.certificate.CertificateSigningRequest;
import com.cpn.vsp.provision.certificate.CertificateSubject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/WEB-INF/Provision-servlet.xml" })
public class CertificateControllerTest {

	@Autowired(required = true)
	public ApplicationContext context;
	
	@PersistenceContext
	public EntityManager manager;

	@Autowired
	public CertificateController controller;

	
	@Test
	@Transactional
	@Rollback
	public void testGetCert() throws Exception {
		Certificate cert = new Certificate();
		CertificateSubject subject = new CertificateSubject();
		subject.put("C", "US");
		subject.put("ST", "California");
		subject.put("O", "ClearPath Networks");
		subject.put("OU", "Engineering Department");
		subject.put("CN", "ClearPath Networks Root Certificate");
		subject.put("emailAddress", "certs@clearpathnet.com");
		subject.put("subjectAltName", "DNS:os115.cpncloud.com");

		cert.setSubject(subject);
		cert.setDaysValidFor(365);
		
		Certificate ca = controller.genCA(cert);
		
		cert = new Certificate();
		cert.setSubject(subject);
		cert.setDaysValidFor(365);
		

		cert = controller.add(cert);

		CertificateSigningRequest csr = controller.getCSRForCert(cert);
		csr.setSigner(ca);
		Certificate signedCert = controller.signCSR(csr);

		System.out.println(signedCert.getId() + signedCert.getPrivateKey() + signedCert.getCert());
	}
}
