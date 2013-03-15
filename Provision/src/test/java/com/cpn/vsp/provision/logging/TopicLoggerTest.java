package com.cpn.vsp.provision.logging;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jms.core.JmsTemplate;

public class TopicLoggerTest {

	@Test
	public void test() {
		String message = "This is my Message";
		JmsTemplate jmsTemplate = Mockito.mock(JmsTemplate.class);
		TopicLogger logger = new JMSTopicLogger(jmsTemplate);
		logger.publish(message);	
		verify(jmsTemplate).convertAndSend(message);
		//jmsTemplate.convertAndSend(msg);

	}

}
