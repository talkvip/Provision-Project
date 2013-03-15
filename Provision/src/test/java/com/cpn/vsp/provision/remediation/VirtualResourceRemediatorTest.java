package com.cpn.vsp.provision.remediation;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import com.cpn.vsp.provision.resource.VirtualResource;

public class VirtualResourceRemediatorTest {

	@Test
	public void testRemediateVirtualResouce() throws Exception, IOException {
		String ipAddress = "1.2.3.4";
		String ipAddress2 = "2.3.4.5";
		HealthMonitor healthMonitor = Mockito.mock(HealthMonitor.class);
		InstanceRemediationStrategy instanceRemediationStrategy = Mockito.mock(InstanceRemediationStrategy.class);
		VirtualResourceRemediator remediator = new VirtualResourceRemediator(healthMonitor, instanceRemediationStrategy);

		VirtualResource r1 = Mockito.mock(VirtualResource.class);
		VirtualResource r2 = Mockito.mock(VirtualResource.class);

		when(r1.getIpAddress()).thenReturn(ipAddress);
		when(r2.getIpAddress()).thenReturn(ipAddress2);
		when(healthMonitor.isHealthy(r1.getIpAddress())).thenReturn(true);
		when(healthMonitor.isHealthy(r2.getIpAddress())).thenReturn(false);
		when(instanceRemediationStrategy.remediate(r1)).thenThrow(new Exception());

		remediator.remediateIfNecessary(r1);
		verify(healthMonitor).isHealthy(r1.getIpAddress());
		verify(instanceRemediationStrategy, never()).remediate(r1);

		remediator.remediateIfNecessary(r2);
		verify(healthMonitor).isHealthy(r2.getIpAddress());
		verify(instanceRemediationStrategy).remediate(r2);

	}

}
