package com.cpn.vsp.provision.remediation;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketHealthMonitor implements HealthMonitor {

	@Override
	public boolean isHealthy(String aResourceName) throws UnknownHostException, IOException {
		try {
			new Socket(aResourceName,22);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

}
