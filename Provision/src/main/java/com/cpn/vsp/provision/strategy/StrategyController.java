package com.cpn.vsp.provision.strategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.vsp.provision.strategy.provision.ProvisioningStrategy;
import com.cpn.vsp.provision.strategy.provision.StrategyType;

@Controller
@RequestMapping(value = "/strategy")
public class StrategyController {

	private static class StrategyDescription implements Serializable{

		private static final long serialVersionUID = -6296054617223748830L;
		private String name, description;

		public StrategyDescription(String aName, String aDescription) {
			name = aName;
			description = aDescription;
		}

		@SuppressWarnings("unused")
		public String getName() {
			return name;
		}

		@SuppressWarnings("unused")
		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			ToStringBuilder builder = new ToStringBuilder(this);
			builder.append("name", name).append("description", description);
			return builder.toString();
		}
		
	}

	private static List<StrategyDescription> makeDescriptions(Set<Class<?>> aSet) {
		return makeDescriptions(aSet, null);
	}

	private static List<StrategyDescription> makeDescriptions(Set<Class<?>> aSet, StrategyType aType) {
		Set<Class<?>> annotated = new Reflections("com.cpn.vsp.provision.strategy.provision").getTypesAnnotatedWith(ProvisioningStrategy.class);
		List<StrategyDescription> results = new ArrayList<>();
		for (Class<?> c : annotated) {
			if (aType == null || Arrays.asList(c.getAnnotation(ProvisioningStrategy.class).type()).contains(aType)) {
				results.add(new StrategyDescription(c.getCanonicalName(), c.getAnnotation(ProvisioningStrategy.class).description()));
			}
		}
		return results;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/virtual")
	@Logged
	public @ResponseBody
	List<StrategyDescription> listVirtualStrategies() {
		return makeDescriptions(new Reflections("com.cpn.vsp.provision.strategy.provision").getTypesAnnotatedWith(ProvisioningStrategy.class), StrategyType.VIRTUAL);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/physical")
	@Logged
	public @ResponseBody
	List<StrategyDescription> listPhysicalStrategies() {
		return makeDescriptions(new Reflections("com.cpn.vsp.provision.strategy.provision").getTypesAnnotatedWith(ProvisioningStrategy.class), StrategyType.PHYSICAL);
	}

	@RequestMapping(method = RequestMethod.GET)
	@Logged
	public @ResponseBody
	List<StrategyDescription> listStrategies() {
		return makeDescriptions(new Reflections("com.cpn.vsp.provision.strategy.provision").getTypesAnnotatedWith(ProvisioningStrategy.class));
	}
}
