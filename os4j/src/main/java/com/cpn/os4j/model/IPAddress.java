package com.cpn.os4j.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class IPAddress implements Serializable {

	private static final long serialVersionUID = -6192279024256341201L;
	String id;
	int version;
	String addr;
	String ip;
	@JsonProperty("fixed_ip")
	String fixedIp;
	@JsonProperty("instance_id")
	String instanceId;
	String pool;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("version", version).append("addr", addr).append("ip", ip).append("fixedIp", fixedIp).append("instanceId", instanceId).append("pool", pool);
		return builder.toString();
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getFixedIp() {
		return fixedIp;
	}

	public void setFixedIp(String fixedIp) {
		this.fixedIp = fixedIp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
	}
}