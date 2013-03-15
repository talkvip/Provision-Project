package com.cpn.vsp.provision.user.session;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpn.vsp.provision.user.User;

@Service
@Transactional
public class SessionDAO {

	@PersistenceContext
	EntityManager entityManager;

	public Session createSessionForUser(User aUser, Date anExpiresAt) {
		//removeAllSessionsForUser(aUser);
		Session session = new Session(aUser, anExpiresAt);
		entityManager.persist(session);
		return session;
	}
	
	public Session addDataToSession(Session session, String aKey, String aValue){
		for(SessionData data : session.getSessionData()){
			if(aKey.equals(data.getKey())){
				data.setValue(aValue);
				entityManager.merge(data);
				return entityManager.find(Session.class, session.getId());
			}
		}
		SessionData data = new SessionData(aKey, aValue);
		data.setSession(session);
		entityManager.persist(data);
		session.getSessionData().add(data);
		return entityManager.merge(session);
	}

	public Session getSessionForUser(User aUser) {
		return entityManager.createQuery("from Session where user = ? and expiresAt > current_date()", Session.class).getSingleResult();
	}

	public void removeAllSessionsForUser(User aUser) {
		List<Session> expiredSessions = entityManager.createQuery("from Session where user = ?", Session.class).setParameter(1, aUser).getResultList();
		for(Session s: expiredSessions){
			entityManager.remove(s);
		}
	}

	@Scheduled(fixedDelay = 1000 * 60 * 5)
	public void clearExpiredSessions() {
		List<Session> expiredSessions = entityManager.createQuery("from Session where expiresAt < current_date()", Session.class).getResultList();
		for(Session s: expiredSessions){
			entityManager.remove(s);
		}
	}

	public Session getSessionById(String sessionId) {
		return entityManager.find(Session.class, sessionId);
	}
}
