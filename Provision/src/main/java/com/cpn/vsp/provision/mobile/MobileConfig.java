package com.cpn.vsp.provision.mobile;

import static com.cpn.vsp.provision.mobile.CertificatePayload.Type.CA;
import static com.cpn.vsp.provision.mobile.CertificatePayload.Type.IDENTITY;
import static com.cpn.vsp.provision.mobile.CertificatePayload.Type.INTERMEDIATE_CA;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;

import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.PKCS12Certificate;
import com.cpn.xml.XMLNode;
import com.cpn.xml.XMLUtil;
public class MobileConfig {
	private static final DocumentBuilder builder;
	
	Document document;


	private int certificateCounter = 1;

	static {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private String generateUUID(){
		return UUID.randomUUID().toString().toUpperCase();
	}
	
	private IPSecPayload getIPSecPayload(XMLNode.XMLNodeFactory xm, CertificatePayload authCert, String aHostName, String aUser, String aPassword){
		return new IPSecPayload(xm, authCert, aHostName, aUser, aPassword);
	}
	
	private XMLNode getNetworkingPayload(XMLNode.XMLNodeFactory xm, CertificatePayload authCert, String anOrganization, String anIdentifier, String aDisplayName, String aHostName, String aUser, String aPassword){
		return new NetworkingPayload(xm, getIPSecPayload(xm, authCert, aHostName, aUser, aPassword), anOrganization, aDisplayName, anIdentifier);
	}
	
	private CertificatePayload getCertificatePayload(XMLNode.XMLNodeFactory xm, String aCertificateName, String aCertificateBody, String anOrganization, String anIdentifier, CertificatePayload.Type aType){
		return new CertificatePayload(xm, aCertificateName, aCertificateBody, anOrganization, anIdentifier, certificateCounter++, aType);
	}
	
	private XMLNode getRemovalPasswordPayload(XMLNode.XMLNodeFactory xm, String anOrganization, String anIdentifier,  String aRemovalPassword){
		return new RemovalPasswordPayload(xm, anOrganization, anIdentifier, aRemovalPassword);
	}
	
	public XMLNode[] getPayloads(XMLNode.XMLNodeFactory xm, CertificatePayload authCert, List<CertificatePayload> someCerts, String anOrganization, String anIdentifier, String aVPNDisplayName, String aHostName, String aUser, String aPassword, String aRemovalPassword){
		List<XMLNode> nodes = new ArrayList<>();
		nodes.add(getNetworkingPayload(xm, authCert, anOrganization, anIdentifier, aVPNDisplayName, aHostName, aUser, aPassword));
		nodes.add(authCert);
		for(CertificatePayload cert: someCerts){
			nodes.add(cert);
		}
		nodes.add(getRemovalPasswordPayload(xm, anOrganization, anIdentifier, aRemovalPassword));
		return nodes.toArray(new XMLNode[nodes.size()]);
	}
	


	public MobileConfig(String aDisplayName, PKCS12Certificate anAuthCert, Set<Certificate> someCerts, String anIdentifier, String anOrganization, String aDescription, String aVPNName, String aHostName, String anXAuthUserName, String anXAuthPassword, String aRemovalPassword) throws TransformerException {
		document = builder.getDOMImplementation().createDocument(null, "plist", builder.getDOMImplementation().createDocumentType("plist", "-//Apple//DTD PLIST 1.0//EN", "http://www.apple.com/DTDs/PropertyList-1.0.dtd"));
		//String organization = "ClearPath Networks";
		//String displayName = "ClearPath Networks Always On Profile (115)";
		//String identifier = "com.cpn.vpn.alwayson.os115";
		//String description = "ClearPath Snap VPN On Demand";
		//String vpnName = "ClearPath Always On VPN";
//		String aHostName = "os115.cpncloud.com";
//		String anXAuthUserName = "clearpathnet";
//		String anXAuthPassword = "password";
//		String aRemovalPassword = "baldeagle";
		List<CertificatePayload> certs = new ArrayList<>();
		XMLNode.XMLNodeFactory xm = new XMLNode.XMLNodeFactory(document);
		CertificatePayload authCert = getCertificatePayload(xm,anAuthCert.getSubject().getCommonName(), anAuthCert.getPkcs12(), anOrganization, anIdentifier, IDENTITY);
		for(Certificate c : someCerts){
			
			String striped = c.getCert().replaceAll("-----BEGIN CERTIFICATE-----", "").replaceAll("-----END CERTIFICATE-----", "");
			certs.add(getCertificatePayload(xm, c.getSubject().getCommonName(), striped, anOrganization, anIdentifier, c.getSigner() != null ? INTERMEDIATE_CA : CA));	
			
		}

		XMLNode node = 
				
					xm.l("dict", 
						xm.l("key", "PayloadContent"),
						xm.l("array", getPayloads(xm, authCert, certs, anOrganization, anIdentifier, aVPNName, aHostName, anXAuthUserName, anXAuthPassword, aRemovalPassword)),
						xm.l("key", "PayloadDescription"),
						xm.l("string", aDescription),
						xm.l("key", "PayloadDisplayName"),
						xm.l("string", aDisplayName),
						xm.l("key", "PayloadIdentifier"),
						xm.l("string", anIdentifier),
						xm.l("key", "PayloadOrganization"),
						xm.l("string", anOrganization),
						xm.l("key", "PayloadRemovalDisallowed"),
						xm.l("true"),
						xm.l("key", "PayloadType"),
						xm.l("string", "Configuration"),
						xm.l("key", "PayloadUUID"),
						xm.l("string", generateUUID()),
						xm.l("key", "PayloadVersion"),
						xm.l("integer", "1")
						
					);
		document.getDocumentElement().appendChild(node.node);
		document.getDocumentElement().setAttribute("version", "1.0");

	}


	@Override
	public String toString() {
		try {
			return XMLUtil.prettyPrint(document);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
