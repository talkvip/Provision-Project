package com.cpn.vsp.provision.strategy.provision;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cpn.vsp.provision.certificate.CertificateController;
import com.cpn.vsp.provision.product.Product;
import com.cpn.vsp.provision.resource.Resource;

@ProvisioningStrategy(description="A strategy for provsioning an internal servers that runs a start up script.", type=StrategyType.VIRTUAL)
@Service
public class InternalServerProvisioningStrategy implements ResourceProvisioningStrategy {

	@Autowired
	CertificateController certController;

	@Autowired
	ApplicationContext context;

	@PersistenceContext
	EntityManager entityManager;



	
	@Autowired
	String managementHostName;


	
//		private Instance createInstance(String anImageLocation, InstanceType aSize, String anAvailabilityZone, String startupScript) throws Exception {
//		//endPoint=project.getEndPoint();
//		final Image image = endPoint.getImageByLocation(anImageLocation);
//		final KeyPair keyPair = endPoint.getKeyPairs().get(0);
//		final SecurityGroup sg = endPoint.getSecurityGroups().get(0);
//		return endPoint.runInstance(image, keyPair, aSize.getName(), "public", 1, 1, anAvailabilityZone, startupScript, sg);
//	}


	private synchronized Resource newInstance(final Product aProduct) throws Exception {
//		final String availabilityZone = availabilityZoneChooserStrategy.chooseZone(entityManager.find(com.cpn.vsp.provision.region.Region.class, Integer.parseInt(aProduct.getMetadata("region"))));
//		//String address = addressPool.getAvailableAddress(PoolType.PUBLIC);
//		//String hostName = DNS.getDomain(address);
//		//Certificate certificate = makeSnapCertificate(aProduct.getName());
//		//Project project=aProduct.getProject();
//		Instance instance = createInstance(aProduct.getMetadata("image"), entityManager.find(InstanceType.class, Integer.parseInt(aProduct.getMetadata("instanceType"))), availabilityZone, Base64.encodeBase64String(aProduct.getMetadata("startupScript").getBytes()));
//		try {
//			
//				instance = instance.waitUntilRunning(120000);
//		} catch (Exception e) {
//			//entityManager.remove(entityManager.find(Certificate.class, certificate.getId()));
//			try {
//				instance.terminate().waitUntilTerminated(120000);
//			} catch (Exception e2) {
//				CompositeException ce = new CompositeException();
//				ce.add(e);
//				ce.add(e2);
//				throw ce;
//			}
//			throw e;
//		}
//		VirtualResource d = new VirtualResource();
//		d.setInstanceId(instance.getInstanceId());
//		d.setAvailabilityZone(availabilityZone);
//		d.setProduct(aProduct);
//		//d.setHostName(hostName);
//		//d.setCertificate(entityManager.find(Certificate.class, certificate.getId()));
//		entityManager.persist(d);
//		return d;
		return null;
	}

	

	@Override
	public Resource provision(Product aProduct) throws Exception {
		return newInstance(aProduct);
	}

	@Override
	public void deprovision(Resource resource) {
//		ResourceVisitor deprovisioner = new ResourceVisitor() {
//			@Override
//			public void visit(VirtualResource aResource) {
//				try {
//					try{
//						instanceManager.terminateInstance(aResource.getInstanceId());
//					}catch(Exception e){
//						logger.error(e.getMessage(), e);
//					}
//					entityManager.remove(aResource);
//				} catch (Exception e) {
//					throw new RuntimeException(e.getMessage(), e);
//				}
//			}
//
//			@Override
//			public void visit(PhysicalResource physicalResource) {
//				throw new UnsupportedOperationException();
//			}
//		};
//		resource.accept(deprovisioner);
	}
}
