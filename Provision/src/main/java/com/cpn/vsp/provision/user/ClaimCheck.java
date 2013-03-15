package com.cpn.vsp.provision.user;

import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ClaimCheck {

	@Id
	private String id = UUID.randomUUID().toString();
	@OneToOne(cascade=CascadeType.MERGE)
	private User user;
	private boolean claimed = false;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createdOn = new Date();

	public ClaimCheck() {
	}

	public ClaimCheck(User aUser) {
		user = aUser;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isClaimed() {
		return claimed;
	}

	public void setClaimed(boolean claimed) {
		this.claimed = claimed;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



}
