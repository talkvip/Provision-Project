package com.cpn.vsp.provision.notify;

public interface UnmarshallerHelper<T> {
	public Class<T> getUnmarshallingClass();

	public String getUnmarshallingXPath();
}
