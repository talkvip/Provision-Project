package com.cpn.os4j.glance;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GlanceEndPoint {

	private static final Logger log = LoggerFactory.getLogger(GlanceEndPoint.class);
	private static String url = "http://archive:9292/v1/images";

	public GlanceEndPoint(String aUrl){
		url = aUrl;
	}
	static HttpClient client = new HttpClient();

	public static List<GlanceImage> listImages() {
		log.debug("You Are Inside GlanceEndPoint listImages method------");
		RestTemplate template = new RestTemplate();
		log.debug("Glance Image Response is:------"+template.getForEntity(url, GlanceImageListResponse.class).getBody().getImages());
		return template.getForEntity(url, GlanceImageListResponse.class).getBody().getImages();
	}
	
	
	public static GlanceImage upload(String aName, InputStream stream, long length) {
//		RestTemplate template = new RestTemplate();
//		template.postForEntity(url, new InputStreamEntity(stream, length), GlanceImageResponse.class).getBody().getImage();
		log.debug("You Are Inside GlanceEndPoint upload method------");
		PostMethod method = new PostMethod(url);
		try {
			method.setRequestEntity(new InputStreamRequestEntity(stream, length, "application/octet-stream"));
			method.setContentChunked(false);
			method.addRequestHeader("x-image-meta-name", aName);
			method.addRequestHeader("content-type", "application/octet-stream");
			log.debug("Before Execute Method Is called-------");
			client.executeMethod(method);
			log.debug("After Execute Method Is called-------");
			ObjectMapper mapper = new ObjectMapper();
		  ObjectReader reader = mapper.reader(GlanceImageResponse.class);
		  log.debug("GlanceImageResponse is:-----"+reader.<GlanceImageResponse>readValue(new String(method.getResponseBody())).getImage());
		  return reader.<GlanceImageResponse>readValue(new String(method.getResponseBody())).getImage();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			method.releaseConnection();
			
		}
	}
}
