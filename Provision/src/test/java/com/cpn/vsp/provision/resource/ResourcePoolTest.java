package com.cpn.vsp.provision.resource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cpn.vsp.provision.pool.ResourcePool;


public class ResourcePoolTest {

	@Autowired
	ResourcePool resourcePool;

	@PersistenceContext
	EntityManager entityManager;
	
	@Test
	public void testResourcePool() throws Exception {

	}
}
