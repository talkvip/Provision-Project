package com.cpn.os4j.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class User {

	String id;
	String username;
	String name;
	List<Role> roles;
	@JsonProperty("roles_links")
	List<String> roleLinks;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<String> getRoleLinks() {
		return roleLinks;
	}
	public void setRoleLinks(List<String> roleLinks) {
		this.roleLinks = roleLinks;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}