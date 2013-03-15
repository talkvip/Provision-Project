package com.cpn.vsp.provision.certificate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Entity
public class CertificateProvider implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 7010401320628914848L;
	@Id
	private String hostName;
	private String userName;
	private String password;

	public CertificateProvider() {
	}

	public CertificateProvider(String url) {
		this.hostName = url;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String url) {
		this.hostName = url;
	}

	@JsonIgnore
	public List<Certificate> getRemoteCertificates() {
		DefaultHttpClient client = new DefaultHttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(getUserName(), getPassword());
		client.getCredentialsProvider().setCredentials(new AuthScope(getHostName(), 8080, AuthScope.ANY_REALM), credentials);
		HttpComponentsClientHttpRequestFactory commons = new HttpComponentsClientHttpRequestFactory(client);
		RestTemplate template = new RestTemplate(commons);

		Certificate[] certificateList = template.getForEntity("http://" + hostName + ":8080/Provision/certificate", Certificate[].class).getBody();
		for (Certificate c : certificateList) {
			c.setUpstream(true);
			c.setCertificateProvider(this);
		}
		return Arrays.asList(certificateList);
	}


	private RestTemplate getRestTemplate(){
		DefaultHttpClient client = new DefaultHttpClient();

		client.getCredentialsProvider().setCredentials(new AuthScope(getHostName(), 8080, AuthScope.ANY_REALM), new UsernamePasswordCredentials(getUserName(), getPassword()));
		HttpComponentsClientHttpRequestFactory commons = new HttpComponentsClientHttpRequestFactory(client);
		return new RestTemplate(commons);
	}

	private static <T> HttpEntity<T> makeEntity(T aT, String aUserName, String aPassword) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authentication", "Basic " + Base64.encodeBase64String((aUserName + ":" + aPassword).getBytes()).replaceAll("\n", ""));
		return new HttpEntity<>(aT, headers);
	}

	public Certificate signCertificateSigningRequest(CertificateSigningRequest aCSR) {
		String url = "http://" + getHostName() + ":8080/Provision/certificate/signCSR";
		CertificateSigningRequest sentCSR = new CertificateSigningRequest();
		sentCSR.setCsr(aCSR.getCsr());
		sentCSR.setSigner(new LocatedCertificate(aCSR.getSigner()));
		sentCSR.setSignee(new DownstreamCertificate(aCSR.getSignee()));
		return getRestTemplate().postForEntity(url, makeEntity(sentCSR, getUserName(), getPassword()), Certificate.class).getBody();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("hostName", hostName).append("userName", userName)
				.append("password", password);
		return builder.toString();
	}


}
