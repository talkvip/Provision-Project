package com.cpn.vsp.provision.logging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

public class JMSLogger {

	@Autowired
	ActiveMQConnectionFactory jmsConnectionFactory;

	private static final Logger log = LoggerFactory.getLogger(JMSLogger.class);

	private boolean running = true;

	public JMSLogger() {
	}

	public void stop() {
		running = false;
	}

	public void init() {
		final JmsTemplate template = new JmsTemplate(jmsConnectionFactory);
		Thread t = new Thread("JMSLogger thread") {
			@Override
			public void run() {
				while (running) {
					LoggingMessage message = (LoggingMessage) template.receiveAndConvert("VSPLogger");
					if (message.state.equals(LoggingMessage.State.THROWN)) {
						log.info(message.toString(), (Throwable) message.result);
					} else {
						log.info(message.toString());
					}
				}
			};
		};
		t.start();
	}
}
