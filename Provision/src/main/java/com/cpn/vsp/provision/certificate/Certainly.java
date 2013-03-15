package com.cpn.vsp.provision.certificate;

import java.net.URI;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cpn.logging.Logged;


@Service
public class Certainly {
	private final URI server;

	private final RestTemplate template = new RestTemplate();

	public Certainly() {
		server = null;
	}
	
	@Autowired
	public Certainly(URI certianlyURL) {
		server = certianlyURL;
	}

	private static <T> HttpEntity<T> makeEntity(T aT) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(aT, headers);
	}

	@Logged
	public Certificate populateCertificate(Certificate aCert) {
		return template.postForEntity(server.toString(), makeEntity(aCert), Certificate.class).getBody();
	}

	@Logged
	public Certificate generateSelfSigned(Certificate aCert) {
		return template.postForEntity(server.toString() + "/ca", makeEntity(aCert), Certificate.class).getBody();
	}

	@Logged
	public CertificateSigningRequest generateCertificateSigningRequest(Certificate aCert) {
		return template.postForEntity(server.toString() + "/csr", makeEntity(aCert), CertificateSigningRequest.class).getBody();
	}

	@Logged
	public Certificate signCertificateSigningRequest(CertificateSigningRequest aCSR) {
		return template.postForEntity(server.toString() + "/signCSR", makeEntity(aCSR), Certificate.class).getBody();
	}

	@Logged
	public PKCS12Certificate pkcs12(PKCS12CertificateRequest aRequest) {
		return template.postForEntity(server.toString() + "/pkcs12", makeEntity(aRequest), PKCS12Certificate.class).getBody();
	}
	
	public static class SigningResponse{
		String result;

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
		
	}
	
	public static class SigningRequest{
		private String cert;
		private String privateKey;
		private String ca;
		private String message;
		public String getCert() {
			return cert;
		}
		public void setCert(String cert) {
			this.cert = cert;
		}
		public String getPrivateKey() {
			return privateKey;
		}
		public void setPrivateKey(String privateKey) {
			this.privateKey = privateKey;
		}
		public String getCa() {
			return ca;
		}
		public void setCa(String ca) {
			this.ca = ca;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	@Logged
	public byte[] sign(String aCert, String aPrivateKey, String aCA, byte[] aMessage){
		SigningRequest request = new SigningRequest();
		request.setCa(aCA);
		request.setCert(aCert);
		request.setPrivateKey(aPrivateKey);
		request.setMessage(Base64.encodeBase64String(aMessage));
		return Base64.decodeBase64(template.postForEntity(server.toString() + "/sign", makeEntity(request), SigningResponse.class).getBody().getResult());
	}

}
