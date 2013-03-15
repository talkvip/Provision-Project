package com.cpn.vsp.provision.user;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.cpn.vsp.provision.user.session.Session;
import com.cpn.vsp.provision.user.session.SessionDAO;

@Service
@Primary
public class DatabaseAuthenticator implements Authenticator {

	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	SessionDAO sessionDAO;
	
	@Override
	public Session authenticate(User aUser) throws NoSuchUserException{
		List<User> users = entityManager.createQuery("from User where email = :email and password = :password and verified=true", User.class).setParameter("email", aUser.getEmail()).setParameter("password", aUser.getPassword()).getResultList();
		if(users.size() == 0){
			throw new NoSuchUserException();
		}
		aUser = users.iterator().next();
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DAY_OF_MONTH, 2);
		return sessionDAO.createSessionForUser(aUser, cal.getTime());
		
	}

	@Override
	public void invalidateSession(String sessionId) {
		// TODO Auto-generated method stub

	}

}
