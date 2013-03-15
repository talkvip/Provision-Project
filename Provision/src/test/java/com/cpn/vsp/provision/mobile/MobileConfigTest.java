package com.cpn.vsp.provision.mobile;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.CertificateController;
import com.cpn.vsp.provision.certificate.CertificateSigningRequest;
import com.cpn.vsp.provision.certificate.CertificateSubject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/WEB-INF/Provision-servlet.xml" })

public class MobileConfigTest {

	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	CertificateController certController;
	
	@Test
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Rollback
	public void testMobileConfig() throws TransformerException, IOException {
		Certificate ca = new Certificate();
		CertificateSubject subject = new CertificateSubject();
		subject.put("C", "US");
		subject.put("ST", "California");
		subject.put("O", "ClearPath Networks");
		subject.put("OU", "Engineering Department");
		subject.put("CN", "ClearPath Networks Root Certificate");
		subject.put("emailAddress", "certs@clearpathnet.com");
		subject.put("subjectAltName", "DNS:os115.cpncloud.com");

		ca.setSubject(subject);
		ca.setDaysValidFor(365);
		
		ca = certController.genCA(ca);
		
		subject = new CertificateSubject();
		subject.put("C", "US");
		subject.put("ST", "California");
		subject.put("O", "ClearPath Networks");
		subject.put("OU", "Engineering Department");
		subject.put("CN", "ClearPath Networks Signing Certificate");
		subject.put("emailAddress", "certs@clearpathnet.com");
		subject.put("subjectAltName", "DNS:os115.cpncloud.com");

		Certificate signer = new Certificate();
		signer.setSubject(subject);
		signer.setDaysValidFor(365);
		certController.add(signer);
		
		CertificateSigningRequest csr = certController.getCSRForCert(signer);
		csr.setSigner(ca);
		signer = certController.signCSR(csr);
		
		subject = new CertificateSubject();
		subject.put("C", "US");
		subject.put("ST", "California");
		subject.put("O", "ClearPath Networks");
		subject.put("OU", "Engineering Department");
		subject.put("CN", "ClearPath Networks Server Certificate");
		subject.put("emailAddress", "certs@clearpathnet.com");
		subject.put("subjectAltName", "DNS:os115.cpncloud.com");

		Certificate server = new Certificate();
		server.setSubject(subject);
		server.setDaysValidFor(365);
		certController.add(server);
		
		csr = certController.getCSRForCert(server);
		csr.setSigner(signer);
		server = certController.signCSR(csr);
		
		subject = new CertificateSubject();
		subject.put("C", "US");
		subject.put("ST", "California");
		subject.put("O", "ClearPath Networks");
		subject.put("OU", "Engineering Department");
		subject.put("CN", "ClearPath Networks Client Certificate");
		subject.put("emailAddress", "certs@clearpathnet.com");
		subject.put("subjectAltName", "DNS:os115.cpncloud.com");

		Certificate client = new Certificate();
		client.setSubject(subject);
		client.setDaysValidFor(365);
		certController.add(client);
		
		csr = certController.getCSRForCert(client);
		csr.setSigner(signer);
		client = certController.signCSR(csr);
		
		assertNotNull(client.getPrivateKey());
		
		assertNotNull(server.getPrivateKey());
		assertNotNull(signer.getPrivateKey());
		assertNotNull(ca.getPrivateKey());
		
		Set<Certificate> set = new HashSet<>();
		set.add(server);
		
		//System.out.println(XMLUtil.prettyPrint(mc.document));
	}

}
