package com.cpn.vsp.provision.data;

import java.util.List;

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

@Controller
@RequestMapping(value = "/data")
@Transactional
public class DataController {

	@PersistenceContext
	EntityManager entityManager;
	
	@Transactional
	@RequestMapping(value="", method=RequestMethod.GET)
	@Logged
	public @ResponseBody List<Data> listData(){
		return entityManager.createQuery("from Data", Data.class).getResultList();
	}
	
	@Transactional
	@RequestMapping(value="/{name}", method=RequestMethod.GET)
	@Logged
	public @ResponseBody byte[] getDataByName(@PathVariable String name){
		return entityManager.find(Data.class, name).getData();
	}
	
	@Transactional
	@RequestMapping(value="/{name}", method={RequestMethod.POST, RequestMethod.PUT})
	@Logged
	public @ResponseBody void postData(@PathVariable String name, @RequestBody byte[] someBytes){
		Data data = entityManager.find(Data.class, name);
		if(data == null){
			data = new Data();
			data.setName(name);
			data.setData(someBytes);
			entityManager.persist(data);
			return;
		}
		data.setData(someBytes);
		entityManager.merge(data);
	}
}
