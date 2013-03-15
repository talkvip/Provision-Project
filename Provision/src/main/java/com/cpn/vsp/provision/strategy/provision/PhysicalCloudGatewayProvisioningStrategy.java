package com.cpn.vsp.provision.strategy.provision;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cpn.vsp.provision.activation.ActivationSerial;
import com.cpn.vsp.provision.metadata.Metadata;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.resource.PhysicalResource;
import com.cpn.vsp.provision.resource.Resource;
import com.cpn.vsp.provision.resource.ResourceVisitor;
import com.cpn.vsp.provision.resource.VirtualResource;

@Service
@ProvisioningStrategy(description = "A Physical Cloud Gateway Provisioning Strategy", type = { StrategyType.PHYSICAL })
public class PhysicalCloudGatewayProvisioningStrategy implements ResourceProvisioningStrategy {

	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	String activationServer;

	@Autowired
	String vspdomain;
	
	
	private static class MyVisitor implements ResourceVisitor{
		public PhysicalResource physicalResource;
		
		public MyVisitor(Resource aResource){
			aResource.accept(this);
		}
		@Override
		public void visit(PhysicalResource physicalResource) {
			this.physicalResource = physicalResource;
		}
		
		@Override
		public void visit(VirtualResource aResource) {
			throw new UnsupportedOperationException();
		}
	}
	@Override
	public void deprovision(Resource resource) {
		PhysicalResource p = new MyVisitor(resource).physicalResource;
		DefaultHttpClient client = new DefaultHttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("test", "test");
		client.getCredentialsProvider().setCredentials(new AuthScope("maestro.cpn.vsp", 8080, AuthScope.ANY_REALM), credentials);
		HttpComponentsClientHttpRequestFactory commons = new HttpComponentsClientHttpRequestFactory(client);
		RestTemplate template = new RestTemplate(commons);
		template.delete("http://maestro.cpn.vsp:8080/Provision/activation/" + p.getSerialKey());
		entityManager.remove(resource);
	}

	
	
	@Override
	public Resource provision(Product aProduct) throws Exception {
		PhysicalResource resource = new PhysicalResource();
		for(Metadata m : aProduct.getMetadata()){
			switch(m.getName()){
			case "hostname": resource.setHostName(m.getValue()); break;
			case "serial": resource.setSerialKey(m.getValue()); break;
			}
		}
		if(resource.getHostName() == null || "".equals(resource.getHostName().trim())){
			final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyz";
	    final int N = alphabet.length();
	    String hostname = "";
	    Random r = new Random();

	    for (int i = 0; i < 8; i++) {
	        hostname += alphabet.charAt(r.nextInt(N));
	    }
	    resource.setHostName("icg-" + hostname + "." + vspdomain);
		}
		resource.setProduct(aProduct);
		entityManager.persist(resource);
		DefaultHttpClient client = new DefaultHttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("test", "test");
		client.getCredentialsProvider().setCredentials(new AuthScope("maestro.cpn.vsp", 8080, AuthScope.ANY_REALM), credentials);
		HttpComponentsClientHttpRequestFactory commons = new HttpComponentsClientHttpRequestFactory(client);
		RestTemplate template = new RestTemplate(commons);
		ActivationSerial serial = new ActivationSerial();
		serial.setSerial(resource.getSerialKey());
		serial.setServer("https://" + activationServer + "/Provision/resource/physical/" + resource.getSerialKey());
		try{
			template.postForLocation("http://maestro.cpn.vsp:8080/Provision/activation", serial);
		}catch(Exception e){
			
		}
		return resource;
	}

	
}