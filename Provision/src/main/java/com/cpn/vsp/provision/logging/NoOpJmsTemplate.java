package com.cpn.vsp.provision.logging;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.springframework.jms.JmsException;
import org.springframework.jms.connection.JmsResourceHolder;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.support.converter.MessageConverter;

public class NoOpJmsTemplate extends JmsTemplate {
public NoOpJmsTemplate() {
	// TODO Auto-generated constructor stub
}

@Override
public <T> T browse(BrowserCallback<T> action) throws JmsException {
	return null;
}

@Override
public <T> T browse(Queue queue, BrowserCallback<T> action) throws JmsException {
	return null;
}

@Override
public <T> T browse(String queueName, BrowserCallback<T> action)
		throws JmsException {
	return null;
}

@Override
public <T> T browseSelected(Queue queue, String messageSelector,
		BrowserCallback<T> action) throws JmsException {
	return null;
}

@Override
public <T> T browseSelected(String messageSelector, BrowserCallback<T> action)
		throws JmsException {
	return null;
}

@Override
public <T> T browseSelected(String queueName, String messageSelector,
		BrowserCallback<T> action) throws JmsException {
	return null;
}

@Override
public void convertAndSend(Destination destination, Object message,
		MessagePostProcessor postProcessor) throws JmsException {
	
}

@Override
public void convertAndSend(Destination destination, Object message)
		throws JmsException {
	
}

@Override
public void convertAndSend(Object message, MessagePostProcessor postProcessor)
		throws JmsException {
	
}

@Override
public void convertAndSend(Object message) throws JmsException {
	
}

@Override
public void convertAndSend(String destinationName, Object message,
		MessagePostProcessor postProcessor) throws JmsException {
	
}

@Override
public void convertAndSend(String destinationName, Object message)
		throws JmsException {
	
}

@Override
protected QueueBrowser createBrowser(Session session, Queue queue,
		String messageSelector) throws JMSException {
	return null;
}

@Override
protected MessageConsumer createConsumer(Session session,
		Destination destination, String messageSelector) throws JMSException {
	return null;
}

@Override
protected MessageProducer createProducer(Session session,
		Destination destination) throws JMSException {
	return null;
}

@Override
protected Object doConvertFromMessage(Message message) {
	return null;
}

@Override
protected MessageProducer doCreateProducer(Session session,
		Destination destination) throws JMSException {
	return null;
}

@Override
protected Message doReceive(Session session, Destination destination,
		String messageSelector) throws JMSException {
	return null;
}

@Override
protected Message doReceive(Session session, MessageConsumer consumer)
		throws JMSException {
	return null;
}

@Override
protected void doSend(MessageProducer producer, Message message)
		throws JMSException {

}

@Override
protected void doSend(Session session, Destination destination,
		MessageCreator messageCreator) throws JMSException {
	
}

@Override
public <T> T execute(Destination destination, ProducerCallback<T> action)
		throws JmsException {
	return null;
}

@Override
public <T> T execute(ProducerCallback<T> action) throws JmsException {
	return null;
}

@Override
public <T> T execute(SessionCallback<T> action, boolean startConnection)
		throws JmsException {
	return null;
}

@Override
public <T> T execute(SessionCallback<T> action) throws JmsException {
	return null;
}

@Override
public <T> T execute(String destinationName, ProducerCallback<T> action)
		throws JmsException {
	return null;
}

@Override
protected Connection getConnection(JmsResourceHolder holder) {
	return null;
}

@Override
public Destination getDefaultDestination() {
	return null;
}

@Override
public String getDefaultDestinationName() {
	return null;
}

@Override
public int getDeliveryMode() {
	return 0;
}

@Override
public MessageConverter getMessageConverter() {
	return null;
}

@Override
public int getPriority() {
	return 0;
}

@Override
public long getReceiveTimeout() {
	return 0;
}

@Override
protected Session getSession(JmsResourceHolder holder) {
	return null;
}

@Override
public long getTimeToLive() {
	return 0;
}

@Override
protected void initDefaultStrategies() {
	
}

@Override
public boolean isExplicitQosEnabled() {
	// TODO Auto-generated method stub
	return super.isExplicitQosEnabled();
}

@Override
public boolean isMessageIdEnabled() {
	return false;
}

@Override
public boolean isMessageTimestampEnabled() {
	return false;
}

@Override
public boolean isPubSubNoLocal() {
	return false;
}

@Override
protected boolean isSessionLocallyTransacted(Session session) {
	return false;
}

@Override
public Message receive() throws JmsException {
	return null;
}

@Override
public Message receive(Destination destination) throws JmsException {
	return null;
}

@Override
public Message receive(String destinationName) throws JmsException {
	return null;
}

@Override
public Object receiveAndConvert() throws JmsException {
	return null;
}

@Override
public Object receiveAndConvert(Destination destination) throws JmsException {
	return null;
}

@Override
public Object receiveAndConvert(String destinationName) throws JmsException {
	return null;
}

@Override
public Message receiveSelected(Destination destination, String messageSelector)
		throws JmsException {
	return null;
}

@Override
public Message receiveSelected(String destinationName, String messageSelector)
		throws JmsException {
	return null;
}

@Override
public Message receiveSelected(String messageSelector) throws JmsException {
	return null;
}

@Override
public Object receiveSelectedAndConvert(Destination destination,
		String messageSelector) throws JmsException {
	return null;
}

@Override
public Object receiveSelectedAndConvert(String destinationName,
		String messageSelector) throws JmsException {
	return null;
}

@Override
public Object receiveSelectedAndConvert(String messageSelector)
		throws JmsException {
	return null;
}

@Override
public void send(Destination destination, MessageCreator messageCreator)
		throws JmsException {
	
}

@Override
public void send(MessageCreator messageCreator) throws JmsException {
	
}

@Override
public void send(String destinationName, MessageCreator messageCreator)
		throws JmsException {
	
}

@Override
public void setDefaultDestination(Destination destination) {
	
}

@Override
public void setDefaultDestinationName(String destinationName) {
	
}

@Override
public void setDeliveryMode(int deliveryMode) {
	
}

@Override
public void setDeliveryPersistent(boolean deliveryPersistent) {
	
}

@Override
public void setExplicitQosEnabled(boolean explicitQosEnabled) {
	
}

@Override
public void setMessageConverter(MessageConverter messageConverter) {
	
}

@Override
public void setMessageIdEnabled(boolean messageIdEnabled) {
	
}

@Override
public void setMessageTimestampEnabled(boolean messageTimestampEnabled) {
	
}

@Override
public void setPriority(int priority) {
	
}

@Override
public void setPubSubNoLocal(boolean pubSubNoLocal) {
	
}

@Override
public void setReceiveTimeout(long receiveTimeout) {
	
}

@Override
public void setTimeToLive(long timeToLive) {
	
}

}
