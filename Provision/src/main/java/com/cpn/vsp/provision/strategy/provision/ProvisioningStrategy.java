package com.cpn.vsp.provision.strategy.provision;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProvisioningStrategy {
	String description();
	StrategyType[] type();
}

