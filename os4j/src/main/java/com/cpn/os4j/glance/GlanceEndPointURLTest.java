package com.cpn.os4j.glance;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.cpn.os4j.glance.GlanceEndPoint;

public class GlanceEndPointURLTest {

	@Test
	public void testUploadMonitor() throws Exception{
				
		File f = new File("E:/test.txt");
		InputStream stream = new FileInputStream(f);
		String aName="upload";
		long length=5000;
		GlanceEndPoint.upload(aName, stream, length);
			
					
	}
	
	
}
