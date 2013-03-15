package com.cpn.vsp.provision.certificate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.jasypt.hibernate4.type.EncryptedStringType;

@JsonAutoDetect
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "id"))
@TypeDef(name = "encryptedString", typeClass = EncryptedStringType.class, parameters = { @Parameter(name = "encryptorRegisteredName", value = "hibernateStringEncryptor") })
public class Certificate implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8031287454475086644L;

	public enum Role {
		CLOUD_GATEWAY(1),
		MANAGER(2),
		LOGGER(4),
		USER(8),
		SSL_PROXY_CLIENT(16),
		SSL_PROXY_SERVER(32);
		
		public final int value;
		Role(int aValue){
			value = aValue;
		}
		
		int getValue(){
			return value;
		}
		
		boolean isInRole(int aValue){
			if((aValue & value) == value){
				return true;
			}
			return false;
		}
		
		int addRole(int existingRole){
			return existingRole | value;
		}
		
		int removeRole(int existingRole){
			return existingRole & ~value;
		}
	}
	
	public Certificate(){
		subject.setNsComment("UUID:" + id);
	}
	
	public Certificate(String anId){
		id = anId;
		subject.setNsComment("UUID:" + id);
	}
	@Id
	private String id = UUID.randomUUID().toString();

	@Column(columnDefinition="LONGTEXT")
	@Type(type = "encryptedString")
	private String privateKey;
	@Column(columnDefinition="LONGTEXT")
	private String cert;

	@OneToOne
	private CertificateProvider certificateProvider;

	@OneToOne(cascade={CascadeType.ALL})
	private CertificateSubject subject = new CertificateSubject();
	
	private int daysValidFor;

	private int role = 0;
	@ManyToOne
	private Certificate signer;
	
	@OneToMany(mappedBy="signer", cascade=CascadeType.REMOVE)
	@JsonIgnore
	private Set<Certificate> signees;

	private boolean selfSigned = false;
	private boolean upstream = false;
	private boolean downstream = false;
		
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date signedOn = new Date();

	@JsonAnySetter
	public void anySetter(String aKey, Object aValue){
		subject.put(aKey, aValue.toString());
	}
	
	public boolean hasRole(Role aRole){
		return aRole.isInRole(role);
	}
	
	public Certificate addRole(Role aRole){
		role = aRole.addRole(role);
		return this;
	}
	
	public Certificate removeRole(Role aRole){
		role = aRole.removeRole(role);
		return this;
	}
	
	public String getPrivateKey() {
		return privateKey;
	}

	public String getCert() {
		return cert;
	}

	public void setCert(String cert) {
		this.cert = cert;
	}

	public Certificate getSigner() {
		return signer;
	}

	public void setSigner(Certificate signer) {
		this.signer = signer;
	}

	public CertificateProvider getCertificateProvider() {
		return certificateProvider;
	}

	public void setCertificateProvider(CertificateProvider certificateProvider) {
		this.certificateProvider = certificateProvider;
	}

	public CertificateSubject getSubject() {
		return subject;
	}

	public void setSubject(CertificateSubject subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("certificateProvider", certificateProvider).append("subject", subject);
		return builder.toString();
	}

	public int getDaysValidFor() {
		return daysValidFor;
	}

	public void setDaysValidFor(int daysValidFor) {
		this.daysValidFor = daysValidFor;
	}

	public boolean isSelfSigned() {
		return selfSigned;
	}

	public void setSelfSigned(boolean selfSigned) {
		this.selfSigned = selfSigned;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public boolean isUpstream() {
		return upstream;
	}

	public void setUpstream(boolean upstream) {
		this.upstream = upstream;
	}

	public boolean isDownstream() {
		return downstream;
	}

	public void setDownstream(boolean downstream) {
		this.downstream = downstream;
	}

	public String getId() {
		return id;
	}

	public void setId(String uuid) {
		this.id = uuid;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(id);
		return builder.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		   if (obj == this) { return true; }
		   if (obj.getClass() != getClass()) {
		     return false;
		   }
		   Certificate rhs = (Certificate) obj;
		   return new EqualsBuilder()
		                 .appendSuper(super.equals(obj))
		                 .append(id, rhs.id)
		                 .isEquals();
	}

	public Date getSignedOn() {
		return signedOn;
	}

	public void setSignedOn(Date signedOn) {
		this.signedOn = signedOn;
	}

	@JsonIgnore
	public Set<Certificate> getSignees() {
		return signees;
	}

	public void setSignees(Set<Certificate> signees) {
		this.signees = signees;
	}
	
	
	@JsonIgnore
	public String buildSignerChain() {
		if(signer == null){
			return cert;
		}
		StringBuffer buffer = new StringBuffer();
		for(Certificate c: getSignerChain()){
			buffer.append(c.getCert());
		}
		return buffer.toString();
	}
	@JsonIgnore
	public static String buildCABundle(Set<Certificate> aSet) {
		Set<Certificate> signers = resolveAllSigners(aSet);
		StringBuffer buffer = new StringBuffer();
		for(Certificate c : signers){
			buffer.append(c.getCert());
		}
		return buffer.toString();
	}
	
	@JsonIgnore
	public Set<Certificate> getSigningChain() {
		return resolveAllSigners(this);
	}
	@JsonIgnore
	public Set<Certificate> getSignerChain() {
		return resolveAllSigners(getSigner());
	}
	@JsonIgnore
	private static Set<Certificate> resolveAllSigners(Certificate aCert){
		Set<Certificate> set = new HashSet<>();
		if(aCert == null){
			return set;
		}
		set.add(aCert);
		if (aCert.getSigner() != null) {
			set.addAll(resolveAllSigners(aCert.getSigner()));
		}
		return set;
	}
	@JsonIgnore
	public static Set<Certificate> resolveAllSigners(Collection<Certificate> someCerts){
		Set<Certificate> set = new HashSet<>();
		for(Certificate c : someCerts){
			set.addAll(resolveAllSigners(c));
		}
		return set;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}


}
