package com.cpn.vsp.provision.user;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClaimCheckDAO {

	@PersistenceContext
	EntityManager entityManager;

	public String createForUser(User aUser) {
		ClaimCheck claimCheck = new ClaimCheck(aUser);
		entityManager.persist(claimCheck);
		return claimCheck.getId();
	}

	public void claim(String claimCheckId) throws AlreadyClaimedException, NoSuchClaimCheckException {
		ClaimCheck claimCheck;
		try {
			claimCheck = entityManager.find(ClaimCheck.class, claimCheckId);
		} catch (Exception e) {
			throw new NoSuchClaimCheckException();
		}
		if (claimCheck == null) {
			throw new NoSuchClaimCheckException();
		}
		if (claimCheck.isClaimed()) {
			throw new AlreadyClaimedException();
		}
		claimCheck.setClaimed(true);
		claimCheck.getUser().setVerified(true);
		entityManager.merge(claimCheck);
	}

	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void expireOldClaimChecks() {
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -30);
		entityManager.createQuery("delete from ClaimCheck where createdOn < ?").setParameter(1, calendar.getTime()).executeUpdate();
	}

	public ClaimCheck makeClaimCheckForUser(User aUser) {
		ClaimCheck claimCheck = new ClaimCheck(aUser);
		entityManager.persist(claimCheck);
		return claimCheck;
	}

	public ClaimCheck getClaimCheckForUser(User aUser) {
		return entityManager.createQuery("from ClaimCheck where user = :user", ClaimCheck.class).setParameter("user", aUser).getSingleResult();
	}

}
