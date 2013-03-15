package com.cpn.vsp.provision.database;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



public abstract class AbstractDAO<T extends DataTransferObject> {

	@PersistenceContext
	EntityManager entityManager;
	
	public T find(Object o){
		return entityManager.find(getDTOClass(), o);
	}
	
	public T merge(T aT){
		return entityManager.merge(aT);
	}
	
	public void persist(T aT){
		entityManager.persist(aT);
	}
	
	public T persistOrMerge(T aT){
		T t = entityManager.find(getDTOClass(), aT.getId());
		if(t != null){
			return t;
		}
		entityManager.persist(aT);
		return aT;
	}
	
	public abstract Class<T> getDTOClass();
	
}
