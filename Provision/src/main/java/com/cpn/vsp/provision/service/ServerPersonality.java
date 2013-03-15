package com.cpn.vsp.provision.service;

import java.util.List;

import com.cpn.os4j.model.SerializedFile;

public class ServerPersonality {
	List<SerializedFile> personality;

	public List<SerializedFile> getPersonality() {
		return personality;
	}

	public void setPersonality(List<SerializedFile> personality) {
		this.personality = personality;
	}
}
