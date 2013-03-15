package com.cpn.vsp.provision.user;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDAO {

	private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
	@PersistenceContext
	EntityManager entityManager;

	public void addUser(User aUser) {
		entityManager.persist(aUser);
	}

	public User findByName(String name) {
		try {
			return entityManager.createQuery("from User where name like ?", User.class).setParameter(1, name).getSingleResult();
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return null;
		}
	}

	public User findByEmail(String anEmail) {
		try {
			return entityManager.createQuery("from User where email like :email", User.class).setParameter("email", anEmail).getSingleResult();
		} catch (Exception e) {
			logger.info(e.getMessage(), e);
			return null;
		}
	}

	public User mergeUser(User existingUser) {
		return entityManager.merge(existingUser);
	}

	public User findByOpenId(String id) {
		return entityManager.createQuery("from User where openId like :openId", User.class).setParameter("openId", id).getSingleResult();
	}

}
