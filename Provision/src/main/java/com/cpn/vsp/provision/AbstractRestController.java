package com.cpn.vsp.provision;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


public abstract class AbstractRestController<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	public abstract Class<T> getPersistenceClass();


	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	T[] list() throws IOException {
		return (T[]) entityManager.createQuery("from " + getPersistenceClass().getName(), getPersistenceClass()).getResultList().toArray();

	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	@Transactional
	public @ResponseBody
	T show(@PathVariable String id){
		return entityManager.find(getPersistenceClass(), id);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	public @ResponseBody
	T add(@RequestBody T aT) throws Exception {
		entityManager.persist(aT);
		return aT;
	}

	@RequestMapping(value = { "", "/{id}" }, method = RequestMethod.PUT)
	@Transactional
	public @ResponseBody
	T update(@RequestBody T aT) {
		return entityManager.merge(aT);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@Transactional
	public @ResponseBody
	void delete(@PathVariable String id) {
		entityManager.remove(entityManager.find(getPersistenceClass(), id));
	}

}
