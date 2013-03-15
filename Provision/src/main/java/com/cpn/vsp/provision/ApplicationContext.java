package com.cpn.vsp.provision;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContext implements ApplicationContextAware{
	
	public static org.springframework.context.ApplicationContext context;

	public static EntityManager entityManager;
	
  /**
   * This method is called from within the ApplicationContext once it is 
   * done starting up, it will stick a reference to itself into this bean.
   * @param context a reference to the ApplicationContext.
   */

	@Override
	public void setApplicationContext(org.springframework.context.ApplicationContext arg0) throws BeansException {
		ApplicationContext.context = arg0;
		entityManager = context.getBean(EntityManagerFactory.class).createEntityManager();
	}


}
