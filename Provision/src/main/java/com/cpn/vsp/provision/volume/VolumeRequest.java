package com.cpn.vsp.provision.volume;

import org.apache.commons.lang.builder.ToStringBuilder;

public class VolumeRequest {

	public String availabilityZone;
	public int size;
	public String getAvailabilityZone() {
		return availabilityZone;
	}
	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("availabilityZone", availabilityZone).append("size",
				size);
		return builder.toString();
	}


}
