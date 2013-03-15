package com.cpn.vsp.provision.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;

import com.cpn.xml.XMLUtil;

@Entity
public class Company {

	@Id
	private String id = UUID.randomUUID().toString();
	private String name;

	private String country;
	private String email;
	private String phoneNumber;
	private String website;

	@OneToMany(targetEntity = User.class, mappedBy = "company")
	private List<User> members = new ArrayList<>();

	public Company() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getMembers() {
		return members;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static Company unmarshall(Node node) {
		Company c = new Company();
		XMLUtil x = new XMLUtil(node);
		try {
			c.name = x.get("name");
			c.email = x.get("email");
			c.id = x.get("uuid");
			c.phoneNumber = x.get("phoneNumber");
			c.website = x.get("website");
			c.country = x.get("country");
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return c;

	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
