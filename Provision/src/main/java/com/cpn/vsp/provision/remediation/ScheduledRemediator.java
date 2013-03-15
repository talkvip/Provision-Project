package com.cpn.vsp.provision.remediation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpn.vsp.provision.resource.VirtualResource;

@Service
public class ScheduledRemediator {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	VirtualResourceRemediator virtualResourceRemediator;
	//@Scheduled(fixedRate=100000)
	public void runRemediation() throws Exception{
	System.out.println("Remediation runs...");
		List <VirtualResource> virtualResourceList= entityManager.createQuery("from VirtualResource",VirtualResource.class).getResultList();
		for(VirtualResource resource:virtualResourceList){
			virtualResourceRemediator.remediateIfNecessary(resource);
		}
		
	}
	

}
