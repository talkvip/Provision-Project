package com.cpn.vsp.provision.metadata;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Metadata implements Serializable {

	private static final long serialVersionUID = 8555388426595022072L;
	@Id
	private String id = UUID.randomUUID().toString();
	private String name;
	private String value;
	
	public Metadata(){
	}
	
	public Metadata(String aName, String aValue){
		name = aName;
		value = aValue;
	}
	
	public Metadata(String aName, Object aValue){
		name = aName;
		value = aValue.toString();
	}
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
