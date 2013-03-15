package com.cpn.vsp.provision.user.session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cpn.vsp.provision.database.DataTransferObject;
import com.cpn.vsp.provision.user.User;

@Entity
public class Session implements DataTransferObject {

	private static final long serialVersionUID = -468421830309668406L;

	@Id
	public String id = UUID.randomUUID().toString();

	@OneToOne
	@JsonIgnore
	public User user;
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date expiresAt;

	@OneToMany(fetch=FetchType.EAGER, targetEntity = SessionData.class, mappedBy = "session", orphanRemoval = true, cascade = CascadeType.REMOVE)
	private List<SessionData> sessionData = new ArrayList<>();

	public String get(String aKey) {
		for (SessionData data : sessionData) {
			if (aKey.toLowerCase().equals(data.getKey().toLowerCase())) {
				return data.getValue();
			}
		}
		return null;
	}
	public Session() {
	}
	

	public Session(User aUser, Date anExpiresAt) {
		user = aUser;
		expiresAt = anExpiresAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Date expiresAt) {
		this.expiresAt = expiresAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<SessionData> getSessionData() {
		return sessionData;
	}

	public void setSessionData(List<SessionData> sessionData) {
		this.sessionData = sessionData;
	}

}
