package com.cpn.vsp.provision.certificate;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
public class CertificateSubject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4396853624556228364L;
	private static final Logger log = LoggerFactory.getLogger(CertificateSubject.class);
	@Id
	
	private String id = UUID.randomUUID().toString();
	private String countryName;
	private String state;
	private String locality;
	private String organization;
	private String organizationalUnit;
	private String commonName;
	private String emailAddress;
	private String subjectAltName;
	private String nsComment;
	private int pathlen = -1;
	private boolean CA = false;
	

	public void put(String aKey, String aValue) {
		switch (aKey) {
		case "C":
		case "countryName":
			setCountryName(aValue);
			return;
		case "CA":
			setCA(Boolean.parseBoolean(aValue));
			return;
		case "pathlen":
			try{
				setPathlen(Integer.parseInt(aValue));
			}catch(Exception e ){
				log.warn("Couldn't parse an Integer from " + aValue + " for pathlen.");
			}
			return;
		case "CN":
		case "commonName":
			setCN(aValue);
			return;
		case "state":
		case "ST":
			setST(aValue);
			return;
		case "locality":
		case "L":
			setL(aValue);
			return;
		case "organization":
		case "O":
			setO(aValue);
			return;
		case "organizationalUnit":
		case "OU":
			setOU(aValue);
			return;
		case "emailAddress":
			setEmailAddress(aValue);
			return;
		case "subjectAltName":
			setSubjectAltName(aValue);
			return;
		case "nsComment":
			setNsComment(aValue);
			return;
		default:
			return;
		}
	}

	@JsonIgnore
	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	@JsonProperty("C")
	public String getC() {
		return countryName;
	}

	public void setC(String countryName) {
		this.countryName = countryName;
	}

	@JsonIgnore
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("ST")
	public String getST() {
		return state;
	}

	public void setST(String state) {
		this.state = state;
	}

	@JsonIgnore
	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}
	@JsonProperty("L")
	public String getL() {
		return locality;
	}

	public void setL(String locality) {
		this.locality = locality;
	}

	@JsonIgnore
	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}
	@JsonProperty("O")
	public String getO() {
		return organization;
	}

	public void setO(String organization) {
		this.organization = organization;
	}

	@JsonIgnore
	public String getOrganizationalUnit() {
		return organizationalUnit;
	}

	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}
	@JsonProperty("OU")
	public String getOU() {
		return organizationalUnit;
	}

	public void setOU(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}

	@JsonIgnore
	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	@JsonProperty("CN")
	public String getCN() {
		return commonName;
	}

	public void setCN(String commonName) {
		this.commonName = commonName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getSubjectAltName() {
		return subjectAltName;
	}

	public void setSubjectAltName(String subjectAltName) {
		this.subjectAltName = subjectAltName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("nsComment")
	public String getNsComment() {
		return nsComment;
	}
	@JsonProperty("nsComment")
	public void setNsComment(String nsComment) {
		this.nsComment = nsComment;
	}

	@JsonProperty("pathlen")
	public int getPathlen() {
		return pathlen;
	}

	public void setPathlen(int pathlen) {
		this.pathlen = pathlen;
	}

	@JsonProperty("CA")
	public boolean getCA() {
		return CA;
	}

	public void setCA(boolean cA) {
		CA = cA;
	}
	
}
