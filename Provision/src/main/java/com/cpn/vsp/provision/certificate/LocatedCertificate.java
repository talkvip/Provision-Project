package com.cpn.vsp.provision.certificate;


public class LocatedCertificate extends Certificate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5407890411947449761L;
	private Certificate certificate;

	public LocatedCertificate(Certificate aCertificate) {
		certificate = aCertificate;
	}

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
		return certificate.getSigner();
	}

	public int hashCode() {
		return certificate.hashCode();
	}

	public void setSigner(Certificate signer) {
		certificate.setSigner(signer);
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

	public boolean isUpstream() {
		return false;
	}

	public void setUpstream(boolean upstream) {
		certificate.setUpstream(upstream);
	}

	public boolean isDownstream() {
		return false;
	}

	public void setDownstream(boolean downstream) {
		certificate.setDownstream(downstream);
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
