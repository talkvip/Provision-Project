package com.cpn.vsp.provision.activation;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.Certificate.Role;

@Controller
@RequestMapping(value="/activation")
@Transactional
public class ActivationController {

	@PersistenceContext
	EntityManager entityManager;
	
	@RequestMapping(method=RequestMethod.POST)
	@Logged
	public @ResponseBody void putSerialServer(@RequestBody ActivationSerial aSerial){
		entityManager.persist(aSerial);
	}
	
	@RequestMapping(value="/{serial}", method=RequestMethod.GET)
	@Logged
	public @ResponseBody String getServerForSerial(@PathVariable String serial){
		return entityManager.createQuery("from ActivationSerial where serial = ?", ActivationSerial.class).setParameter(1, serial).getSingleResult().getServer();
	}
	
	@RequestMapping(value="/{serial}", method=RequestMethod.DELETE)
	@Logged
	public @ResponseBody void remove(@PathVariable String serial){
		entityManager.remove(entityManager.createQuery("from ActivationSerial where serial = ?", ActivationSerial.class).setParameter(1, serial).getSingleResult());
	}
	
	@RequestMapping(value="/{serial}/cert", method=RequestMethod.GET)
	@Logged
	public @ResponseBody String getCertForSerial(@PathVariable String serial){
		if(null == entityManager.createQuery("from ActivationSerial where serial = ?", ActivationSerial.class).setParameter(1, serial).getSingleResult().getServer()){
			throw new IllegalArgumentException("No such serial exists!");
		}
		return entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Role.SSL_PROXY_CLIENT.value).getSingleResult().getCert();
	}
	
	@RequestMapping(value="/{serial}/privateKey", method=RequestMethod.GET)
	@Logged
	public @ResponseBody String getPrivateKeyForSerial(@PathVariable String serial){
		if(null == entityManager.createQuery("from ActivationSerial where serial = ?", ActivationSerial.class).setParameter(1, serial).getSingleResult().getServer()){
			throw new IllegalArgumentException("No such serial exists!");
		}
		return entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Role.SSL_PROXY_CLIENT.value).getSingleResult().getPrivateKey();
	}	
	@RequestMapping(value="/{serial}/caBundle", method=RequestMethod.GET)
	@Logged
	public @ResponseBody String getCABundleForSerial(@PathVariable String serial){
		if(null == entityManager.createQuery("from ActivationSerial where serial = ?", ActivationSerial.class).setParameter(1, serial).getSingleResult().getServer()){
			throw new IllegalArgumentException("No such serial exists!");
		}
		return Certificate.buildCABundle(entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Role.SSL_PROXY_SERVER.value).getSingleResult().getSignerChain());
	}
	
	
	
}
