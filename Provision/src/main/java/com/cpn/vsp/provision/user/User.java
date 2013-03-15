package com.cpn.vsp.provision.user;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.w3c.dom.Node;

import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.database.DataTransferObject;
import com.cpn.xml.XMLUtil;

@Entity
public class User implements DataTransferObject {

	private static final long serialVersionUID = -5889559329276233923L;

	@Id
	private String id = UUID.randomUUID().toString();

	private String email;

	private String password;
	private boolean verified = false;
	private boolean pending = false;

//	@OneToMany(targetEntity = CollaborationGroup.class, mappedBy = "user", cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
//	private List<CollaborationGroup> collaborationGroups = new ArrayList<>();

	private String firstName;
	private String lastName;
	private String language;

	private String openId;

	@OneToOne
	private Company company;

	@OneToOne(cascade = CascadeType.REMOVE)
	private Certificate certificate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("pending", pending).append("verified", verified).append("firstName", firstName).append("lastName", lastName).append("email", email).append("language", language).append("certificate", certificate);
		return builder.toString();
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

//	public List<CollaborationGroup> getCollaborationGroups() {
//		return collaborationGroups;
//	}
//
//	public void setCollaborationGroups(List<CollaborationGroup> collaborationGroups) {
//		this.collaborationGroups = collaborationGroups;
//	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public static User unmarshall(Node node) {
		User user = new User();
		XMLUtil x = new XMLUtil(node);
		try {
			user.email = x.get("email");
			user.firstName = x.get("firstName");
			user.lastName = x.get("lastName");
			user.setOpenId(x.get("openId"));
			user.id = x.get("uuid");
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return user;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
