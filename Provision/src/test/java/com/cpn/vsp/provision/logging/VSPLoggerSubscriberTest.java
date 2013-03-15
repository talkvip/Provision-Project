package com.cpn.vsp.provision.logging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;


public class VSPLoggerSubscriberTest {

	@Test
	public void test() {
		JmsTemplate template = new JmsTemplate(new ActiveMQConnectionFactory("tcp://control.dev.intercloud.net:51515"));
		//JmsTemplate template = new JmsTemplate(new ActiveMQConnectionFactory("tcp://localhost:61616"));
		while(true){
			Logger log = LoggerFactory.getLogger(VSPLoggerSubscriberTest.class);
			log.info(template.receiveAndConvert("VSPLogger").toString());
			//System.out.println(template.receiveAndConvert("VSPLogging"));
		}
		
	}

}
