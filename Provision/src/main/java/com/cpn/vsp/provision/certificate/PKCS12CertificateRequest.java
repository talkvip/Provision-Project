package com.cpn.vsp.provision.certificate;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class PKCS12CertificateRequest {

	Certificate certificate;
	String caBundle;

	public Certificate getCertificate() {
		return certificate;
	}

	public PKCS12CertificateRequest setCertificate(Certificate certificate) {
		this.certificate = certificate;
		return this;
	}

	@JsonProperty("caBundle")
	public String getCaBundle() {
		return caBundle;
	}

	@JsonProperty("caBundle")
	public PKCS12CertificateRequest setCaBundle(String certBundle) {
		this.caBundle = certBundle;
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("certificate", certificate).append("caBundle", caBundle);
		return builder.toString();
	}
	

}
