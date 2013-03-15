package com.cpn.vsp.provision.certificate;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;

@Controller
@RequestMapping(value = "/certificate/provider")
public class CertificateProviderController {

	@PersistenceContext
	EntityManager entityManager;

	public Class<CertificateProvider> getPersistenceClass() {
		return CertificateProvider.class;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET)
	@Transactional
	@Logged
	public @ResponseBody
	List<CertificateProvider> list() throws IOException {
		return entityManager.createQuery("from " + getPersistenceClass().getName()).getResultList();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Transactional
	@Logged
	public @ResponseBody
	CertificateProvider show(@PathVariable String id) {
		return entityManager.find(getPersistenceClass(), id);
	}

	@RequestMapping(method = RequestMethod.POST)
	@Transactional
	@Logged
	public @ResponseBody
	CertificateProvider add(@RequestBody CertificateProvider aT) {
		entityManager.persist(aT);
		return aT;
	}

	@RequestMapping(value = { "", "/{id}" }, method = RequestMethod.PUT)
	@Transactional
	@Logged
	public @ResponseBody
	CertificateProvider update(@RequestBody CertificateProvider aT) {
		entityManager.persist(aT);
		return aT;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@Transactional
	@Logged
	public @ResponseBody
	void delete(@PathVariable String id, HttpServletRequest aRequest) {
		Matcher matcher = Pattern.compile(".*/certificate/provider/(.*)").matcher(aRequest.getRequestURI());
		matcher.find();
		id = matcher.group(1);
		CertificateProvider p = entityManager.find(CertificateProvider.class, id);
		entityManager.remove(p);
	}
}
