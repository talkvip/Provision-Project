package com.cpn.os4j.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonProperty;

public class Flavor extends AbstractOpenStackModel {


	private static final long serialVersionUID = 1264918407525243722L;
	String id;
	String name;
	int ram;
	int disk;
	int vcpus;
	@JsonProperty("rxtx_factor")
	float rxtxFactor;
	@JsonProperty("OS-FLV-EXT-DATA:ephemeral")
	int ephemeral;
	String swap;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id).append("links", links).append("name", name).append("ram", ram).append("disk", disk).append("vcpus", vcpus).append("rxtxFactor", rxtxFactor).append("ephemeral", ephemeral).append("swap", swap);
		return builder.toString();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}
	public int getDisk() {
		return disk;
	}
	public void setDisk(int disk) {
		this.disk = disk;
	}
	public int getVcpus() {
		return vcpus;
	}
	public void setVcpus(int vcpus) {
		this.vcpus = vcpus;
	}
	public float getRxtxFactor() {
		return rxtxFactor;
	}
	public void setRxtxFactor(float rxtxFactor) {
		this.rxtxFactor = rxtxFactor;
	}
	public int getEphemeral() {
		return ephemeral;
	}
	public void setEphemeral(int ephemeral) {
		this.ephemeral = ephemeral;
	}
	public String getSwap() {
		return swap;
	}
	public void setSwap(String swap) {
		this.swap = swap;
	}

}