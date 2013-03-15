package com.cpn.vsp.provision.logging;

import java.io.Serializable;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.cpn.vsp.provision.logging.LoggingMessage.State;

@Component
@Aspect
public class VSPLogger {

	private TopicLogger logger = null;

	public VSPLogger(TopicLogger logger) {
		this.logger = logger;
	}

	@Before(value = "execution(@com.cpn.logging.Logged * * (..))")
	public void logAPICallsBefore(final JoinPoint joinPoint) throws Throwable {
		logger.publish(makeMessage(State.ENTER, joinPoint, null));
	}

	@AfterReturning(pointcut = "execution(@com.cpn.logging.Logged * * (..))", returning = "result")
	public void logAPICallsAfter(final JoinPoint joinPoint, Object result) {

		logger.publish(makeMessage(State.EXIT, joinPoint, result));
	}

	@AfterThrowing(pointcut = "execution(@com.cpn.logging.Logged * * (..))", throwing = "error")
	public void logAPICallsAfterThrow(final JoinPoint joinPoint, Throwable error) {
		logger.publish(makeMessage(State.THROWN, joinPoint, error));
	}

	private static LoggingMessage makeMessage(State aState, JoinPoint aJoinPoint, Object aResult) {
		LoggingMessage message = new LoggingMessage();
		message.state = aState;
		message.className = aJoinPoint.getSignature().getDeclaringTypeName().toString();
		message.methodName = aJoinPoint.getSignature().getName();
		for (Object o : aJoinPoint.getArgs()) {
			if ((o instanceof Serializable)) {
				message.arguments.add(o);
			} else {
				message.arguments.add(o.toString());
			}
		}
		message.threadId = Thread.currentThread().toString();
		if (aResult instanceof Serializable) {
			message.result = aResult;
		} else if (aResult != null) {
			message.result = aResult.toString();
		}
		return message;
	}

}