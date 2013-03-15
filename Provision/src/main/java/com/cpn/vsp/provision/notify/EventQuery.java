package com.cpn.vsp.provision.notify;

public class EventQuery extends AbstractNotificationQuery<Event> {

	public EventQuery(String token) {
		super(token);
	}
	
	@Override
	public Class<Event> getUnmarshallingClass() {
		return Event.class;
	}
	@Override
	public String getUnmarshallingXPath() {
		return "event";
	}

}
