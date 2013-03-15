package com.cpn.vsp.provision.resource;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
@DiscriminatorValue("VirtualResource")
public class VirtualResource extends Resource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1497238223319950860L;
	private String volumeId;
	private String serverId;


	@Override
	public void accept(ResourceVisitor aResourceVisitor) {
		aResourceVisitor.visit(this);
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}


	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}


}