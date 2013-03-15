package com.cpn.vsp.provision.logging;

import org.springframework.jms.core.JmsTemplate;

public class JMSTopicLogger implements TopicLogger {

	private JmsTemplate jmsTemplate;

	public JMSTopicLogger(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public void publish(LoggingMessage msg) {
		jmsTemplate.convertAndSend(msg);
	}

	@Override
	public void publish(String msg) {
		jmsTemplate.convertAndSend(msg);
	}

}