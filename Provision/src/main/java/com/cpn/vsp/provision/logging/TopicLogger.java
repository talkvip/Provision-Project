package com.cpn.vsp.provision.logging;

public interface TopicLogger {
	public void publish(String msg);

	public void publish(LoggingMessage msg);

}
