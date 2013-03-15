package com.cpn.vsp.provision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpn.os4j.ComputeEndpoint;
import com.cpn.os4j.model.IPAddressPool;
import com.cpn.vsp.provision.user.UserService;

@Service
public class StartupScript implements InitializingBean {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	UserService userService;

	@Autowired
	EndpointFactory endPointFactory;

	private static final Logger log = LoggerFactory.getLogger(StartupScript.class);

	public StartupScript() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ComputeEndpoint endPoint = endPointFactory.getComputeEndpoint();
		log.trace("Startup script running.");
		// mapper.getObjectMapper().enableDefaultTyping(); // defaults for defaults
		// (see below); include as wrapper-array, non-concrete types
		// mapper.getObjectMapper().enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL,
		// As.WRAPPER_OBJECT); // all non-final types
		try {
			for (int x = 0; x < 1000000; ++x) {
				endPoint.allocateIPAddress(new IPAddressPool("vcgs"));
			}

		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}

		userService.setupMasterUser();

		log.trace("Startup script finished.");
	}
}
