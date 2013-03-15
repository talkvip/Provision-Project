package com.cpn.vsp.provision.capability;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Capability implements Serializable {

	private static final long serialVersionUID = -2586352611006388323L;
	@Id
	
	private String id = UUID.randomUUID().toString();
	private String name;
	@Column(columnDefinition="LONGTEXT")
	private String description;
	private String uri;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("name", name)
				.append("description", description).append("uri", uri);
		return builder.toString();
	}
	

	
}
