package com.cpn.vsp.provision.data;

import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;


public class Data {

	@Id
	private String name;
	@Basic(fetch=FetchType.LAZY)
	private byte[] data;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonIgnore
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("name", name).append("data", Arrays.toString(data));
		return builder.toString();
	}
	
	
}
