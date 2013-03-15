package com.cpn.vsp.provision.user;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.cpn.vsp.provision.resource.Resource;

@Entity
public class CollaborationGroup {

	@Id
	private String id = UUID.randomUUID().toString();
	@OneToOne
	private User user;
	@OneToOne
	private Resource resource;
//	@ManyToMany(targetEntity = User.class, fetch = FetchType.EAGER)
//	@JoinTable(name = "CollaborationGroup_User", joinColumns = { @JoinColumn(name = "collaborationGroup_id") }, inverseJoinColumns = { @JoinColumn(name = "user_id") })
//	private List<User> collaborators = new ArrayList<>();
//	
	public CollaborationGroup() {
		super();
	}
	public CollaborationGroup(User aUser) {
		super();
		user = aUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User owner) {
		this.user = owner;
	}

//	public List<User> getCollaborators() {
//		return collaborators;
//	}
//
//	public void setCollaborators(List<User> collaborators) {
//		this.collaborators = collaborators;
//	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}

}
