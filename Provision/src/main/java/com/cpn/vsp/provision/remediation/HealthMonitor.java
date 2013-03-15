package com.cpn.vsp.provision.remediation;

import java.io.IOException;
import java.net.UnknownHostException;

public interface HealthMonitor {

	public boolean isHealthy(String aResourceName) throws UnknownHostException, IOException;
}
