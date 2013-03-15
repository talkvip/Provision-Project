package com.cpn.vsp.provision.product;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.vsp.provision.AbstractRestController;

@Controller
@RequestMapping(value = "/product")
public class ProductController extends AbstractRestController<Product> {

	@RequestMapping(method = RequestMethod.GET)
	@Transactional
	@Override
	@Logged
	public @ResponseBody
	Product[] list() throws IOException {
		return entityManager
				.createQuery("from " + getPersistenceClass().getName(),
						getPersistenceClass()).getResultList()
				.toArray(new Product[0]);
	}

	@Override
	public Class<Product> getPersistenceClass() {
		return Product.class;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Transactional
	@Override
	@Logged
	public @ResponseBody
	Product show(@PathVariable String id) {
		return entityManager.find(getPersistenceClass(), id);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	@Override
	@Logged
	public @ResponseBody
	Product add(@RequestBody Product aT) throws Exception {
		//String projectId = aT.getProject();
		//Project project = entityManager
		//		.createQuery("from Project where id=" + projectId,
		//				Project.class).getSingleResult();
		//String projectName = project.getName();
		//aT.setProjectName(projectName);
		entityManager.persist(aT);
		return aT;
	}

	@RequestMapping(value = { "", "/{id}" }, method = RequestMethod.PUT)
	@Transactional
	@Override
	@Logged
	public @ResponseBody
	Product update(@RequestBody Product aT) {
		return entityManager.merge(aT);
	}
	


}
