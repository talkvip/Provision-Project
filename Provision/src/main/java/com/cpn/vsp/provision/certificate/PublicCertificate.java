package com.cpn.vsp.provision.certificate;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PublicCertificate extends Certificate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 28652930968213846L;
	private Certificate certificate;
	
	public PublicCertificate(Certificate aCert) {
		certificate = aCert;
	}

	@JsonIgnore
	public String getPrivateKey() {
		return certificate.getPrivateKey();
	}

	public String getCert() {
		return certificate.getCert();
	}

	public void setCert(String cert) {
		certificate.setCert(cert);
	}

	public Certificate getSigner() {
		return certificate.getSigner() != null ? new PublicCertificate(certificate.getSigner()) : null;
	}

	public void setSigner(Certificate signer) {
		certificate.setSigner(signer);
	}

	public int hashCode() {
		return certificate.hashCode();
	}


	public String getId() {
		return certificate.getId();
	}

	public void setId(String id) {
		certificate.setId(id);
	}

	public CertificateProvider getCertificateProvider() {
		return certificate.getCertificateProvider();
	}

	public void setCertificateProvider(CertificateProvider certificateProvider) {
		certificate.setCertificateProvider(certificateProvider);
	}

	public CertificateSubject getSubject() {
		return certificate.getSubject();
	}

	public void setSubject(CertificateSubject subject) {
		certificate.setSubject(subject);
	}

	public String toString() {
		return certificate.toString();
	}

	public int getDaysValidFor() {
		return certificate.getDaysValidFor();
	}

	public void setDaysValidFor(int daysValidFor) {
		certificate.setDaysValidFor(daysValidFor);
	}

	public boolean isSelfSigned() {
		return certificate.isSelfSigned();
	}

	public void setSelfSigned(boolean selfSigned) {
		certificate.setSelfSigned(selfSigned);
	}

	public void setPrivateKey(String privateKey) {
		certificate.setPrivateKey(privateKey);
	}

	public boolean equals(Object obj) {
		return certificate.equals(obj);
	}
	
	public int getRole(){
		return certificate.getRole();
	}
	public void setRole(int aRole){
		certificate.setRole(aRole);
	}
}
