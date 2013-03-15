package com.cpn.vsp.provision.user.session;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.cpn.vsp.provision.database.DataTransferObject;

@Entity
public class SessionData implements DataTransferObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -44098721827737286L;
	@Id
	private String id = UUID.randomUUID().toString();
	@Column(name="keyName")
	private String key;
	private String value;
	@ManyToOne
	private Session session;

	public SessionData() {
	}
	public SessionData(String aKey, String aValue) {
		key = aKey;
		value = aValue;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

}
