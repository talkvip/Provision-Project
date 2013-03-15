package com.cpn.os4j.model;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class Image extends AbstractOpenStackModel{

	private static final long serialVersionUID = -4256144888881122608L;
	String id;
	String name;
	String updated;
	String created;
	@JsonProperty("tenant_id")
	String tenantId;
	@JsonProperty("user_id")
	String userId;
	String status;
	int progress;
	int minDisk;
	int minRam;
	Server server;
	Map<String, String> metadata;


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
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("links", links).append("name", name).append("updated", updated).append("created", created).append("tenantId", tenantId).append("userId", userId).append("status", status).append("progress", progress)
				.append("minDisk", minDisk).append("minRam", minRam).append("server", server).append("metadata", metadata);
		return builder.toString();
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public int getMinDisk() {
		return minDisk;
	}
	public void setMinDisk(int minDisk) {
		this.minDisk = minDisk;
	}
	public int getMinRam() {
		return minRam;
	}
	public void setMinRam(int minRam) {
		this.minRam = minRam;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public Map<String, String> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}


}