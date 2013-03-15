package com.cpn.vsp.provision.mobile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;


public class TestReadFromClasspath {

	@Test
	public void testRead() throws IOException{
		InputStream ins = this.getClass().getClassLoader().getResourceAsStream("certs/mobileConfigTrustedSignerKey.pem");
		BufferedReader in = new BufferedReader(new InputStreamReader(ins));
		String line = null;
		while((line = in.readLine()) != null) {
		  System.out.println(line);
		}
	}
}
