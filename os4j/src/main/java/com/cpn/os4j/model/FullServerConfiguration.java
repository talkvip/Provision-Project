package com.cpn.os4j.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

public class FullServerConfiguration implements Serializable, ServerConfiguration{


	private static final long serialVersionUID = -5963753281735130626L;
	String name;
	String imageRef;
	String flavorRef;
	String accessIPv4;

	Map<String, String> metadata;
	List<SerializedFile> personality;

	public FullServerConfiguration() {
	}

	public FullServerConfiguration(String aName, String anIpAddress, String anImageRef, String aFlavorRef, Map<String, String> someMetadata, List<SerializedFile> aPersonality) {
		name = aName;
		imageRef = anImageRef;
		flavorRef = aFlavorRef;
		metadata = someMetadata;
		personality = aPersonality;
		accessIPv4 = anIpAddress;
	}

	public FullServerConfiguration(String aName, IPAddress anIpAddress, Image anImage, Flavor aFlavor, Map<String, String> someMetadata, List<SerializedFile> aPersonality) {
		this(aName, anIpAddress.getIp(), anImage.getSelfRef(), aFlavor.getSelfRef(), someMetadata, aPersonality);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public String getFlavorRef() {
		return flavorRef;
	}

	public void setFlavorRef(String flavorRef) {
		this.flavorRef = flavorRef;
	}

	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public List<SerializedFile> getPersonality() {
		return personality;
	}

	public void setPersonality(List<SerializedFile> personality) {
		this.personality = personality;
	}

	public String getAccessIPv4() {
		return accessIPv4;
	}

	public void setAccessIPv4(String accessIPv4) {
		this.accessIPv4 = accessIPv4;
	}




}