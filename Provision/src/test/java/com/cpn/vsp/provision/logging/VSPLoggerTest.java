package com.cpn.vsp.provision.logging;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.mockito.Mockito;

public class VSPLoggerTest {

	@Test
	public void testLogAPICallsBefore() throws  Throwable{
		TopicLogger logger = Mockito.mock(TopicLogger.class);
		VSPLogger vspLogger = new VSPLogger(logger);
		JoinPoint jPoint =Mockito.mock(JoinPoint.class);
		Signature signature = Mockito.mock(Signature.class);
		when(signature.getDeclaringTypeName()).thenReturn("DeclaringTypeName");
		when(signature.getName()).thenReturn("Signature name");
		when(jPoint.getArgs()).thenReturn(new Object[0]);
		when(jPoint.getSignature()).thenReturn(signature);
		vspLogger.logAPICallsBefore(jPoint);
	  verify(logger).publish(anyString());
		
	}

	@Test
	public void testLogAPICallsAfter()  {
		TopicLogger logger = Mockito.mock(TopicLogger.class);
		VSPLogger vspLogger = new VSPLogger(logger);
		JoinPoint jPoint =Mockito.mock(JoinPoint.class);
		Signature signature = Mockito.mock(Signature.class);
		when(signature.getDeclaringTypeName()).thenReturn("DeclaringTypeName");
		when(signature.getName()).thenReturn("Signature name");
		when(jPoint.getArgs()).thenReturn(new Object[0]);
		when(jPoint.getSignature()).thenReturn(signature);
		vspLogger.logAPICallsAfter(jPoint, null);
	    verify(logger).publish(anyString());
	}

}
