package com.cpn.vsp.provision.remediation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;


public class HealthMonitorTest {
	

	@Test
	public void testHealthMonitor() throws UnknownHostException, IOException{
		   // Resource mockResource = Mockito.mock(Resource.class);
		   // String hostName = mockResource.getHostName();
			HealthMonitor healthMonitor = new SocketHealthMonitor();
			assertTrue(healthMonitor.isHealthy("yahoo.com"));
			assertFalse(healthMonitor.isHealthy("notyahooblew.com"));
			
	}
	
	@Test
	public void testAlwaysHealthyHealthMonitor() throws Exception, IOException{
		HealthMonitor healthMonitor = new AlwaysHealthyHealthMonitor();
		assertTrue(healthMonitor.isHealthy("yahoo.com"));
		assertTrue(healthMonitor.isHealthy("notyahooblew.com"));
	}
}
