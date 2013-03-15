package com.cpn.vsp.provision.rac;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;

import com.cpn.vsp.provision.resource.Resource;
import com.cpn.vsp.provision.resource.ResourceDAO;
import com.cpn.vsp.provision.user.session.Session;
import com.cpn.vsp.provision.user.session.SessionDAO;
import com.cpn.xml.XMLNode;
import com.cpn.xml.XMLUtil;

@Controller
@RequestMapping("/rac")
@Transactional
public class RemoteAccessClientController {

	private static final DocumentBuilder builder;

	@Autowired
	SessionDAO sessionDAO;

	@Autowired
	ResourceDAO resourceDAO;

	@Autowired
	String publicAddress = "localhost";

	@Autowired
	String publicPort = "8080";

	static {
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/url/{sessionId}")
	public @ResponseBody
	String getUrl(@PathVariable String sessionId) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Session session = sessionDAO.getSessionById(sessionId);
		if(session == null){
			throw new RuntimeException("Your session has expired. You'll need to log back into the marketplace to use your Secure Private Network.");
		}
		
		String notEncrypted = "http://" + publicAddress + ("80".equals(publicPort) ? "" : ":" + publicPort) + "/rac/?sessionId=" + sessionId;
		String secretID = "L0ck it up saf3";
		byte[] key = (secretID).getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		byte[] encrypted = cipher.doFinal(notEncrypted.getBytes());

		return "vpnrac://?ts=" + Base64.encodeBase64String(encrypted);
	}

	@RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody
	String get(@RequestParam String sessionId) throws IOException {
		Session session = sessionDAO.getSessionById(sessionId);
		Resource resource = resourceDAO.find(session.get("resourceId"));
		Document document = builder.getDOMImplementation().createDocument(null, "vpn", builder.getDOMImplementation().createDocumentType("vpn", "", ""));
		XMLNode.XMLNodeFactory xm = new XMLNode.XMLNodeFactory(document);

		document.getDocumentElement().appendChild(xm.l("remote_host", resource.getHostName()).node);
		document.getDocumentElement().appendChild(xm.l("remote_ip_address", resource.getHostName()).node);
		document.getDocumentElement().appendChild(xm.l("cabundle", resource.getCertificate().buildSignerChain()).node);
		document.getDocumentElement().appendChild(xm.l("certificate", session.getUser().getCertificate().getCert()).node);
		document.getDocumentElement().appendChild(xm.l("privatekey", session.getUser().getCertificate().getPrivateKey()).node);
		document.getDocumentElement().appendChild(xm.l("options", "--dev tap --tls-client --pull ping 15 --verb 0 --cipher AES-256-CBC --comp-lzo").node);

		return XMLUtil.print(document);
	}
}
