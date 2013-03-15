package com.cpn.vsp.provision.certificate;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class CertificateSigningRequest implements Serializable {

	
	private static final long serialVersionUID = 452590152369913976L;

	private String csr;

	private Certificate signer;

	private Certificate signee;

	
	public CertificateSigningRequest() {
	}
	
	public CertificateSigningRequest(String aCSR){
		csr = aCSR;
	}

	public String getCsr() {
		return csr;
	}

	public void setCsr(String csr) {
		this.csr = csr;
	}

	public Certificate getSigner() {
		return signer;
	}

	public CertificateSigningRequest setSignerFluent(Certificate signer) {
		this.signer = signer;
		return this;
	}

	public void setSigner(Certificate signer) {
		this.signer = signer;
	}

	public Certificate getSignee() {
		return signee;
	}

	public void setSignee(Certificate signee) {
		this.signee = signee;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("csr", csr).append("signer", signer)
				.append("signee", signee);
		return builder.toString();
	}

	
	
}
