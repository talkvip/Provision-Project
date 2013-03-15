package com.cpn.vsp.provision.user;

import java.net.UnknownHostException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.CertificateController;

@Service
public class UserService {

	@Autowired
	CertificateController certificateController;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	UserDAO userDAO;

	private static final String masterUserEmail = "masteruser@intercloud.net";

	public User findOrCreate(User aUser) throws UnknownHostException {
		User u = entityManager.find(User.class, aUser.getId());
		if (u != null) {
			return u;
		}
		prepareUser(aUser);
		userDAO.addUser(aUser);
		return aUser;
	}

	public void prepareUser(User aUser) throws UnknownHostException {
		Certificate certificate = new Certificate();
		certificate.getSubject().put("C", "US");
		certificate.getSubject().put("ST", "California");
		certificate.getSubject().put("O", "ClearPath Networks");
		certificate.getSubject().put("OU", "Engineering Department");
		certificate.getSubject().put("CN", aUser.getEmail());
		certificate.getSubject().put("L", "El Segundo");
		certificate.getSubject().put("emailAddress", aUser.getEmail());
		certificate.getSubject().put("subjectAltName", "email:copy");
		certificate.getSubject().setCA(false);
		Certificate signer = entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Certificate.Role.USER.value).getSingleResult();
		certificate.setSelfSigned(false);
		certificate.setDaysValidFor(365 * 5);
		certificate.setRole(0);
		certificate.setSigner(signer);
		certificate = certificateController.add(certificate);
		aUser.setCertificate(certificate);
	}

	public User findMasterUser() {
		return userDAO.findByEmail(masterUserEmail);
	}

	public void setupMasterUser() throws UnknownHostException {
		List<User> users = entityManager.createQuery("from User where email like ?", User.class).setParameter(1, masterUserEmail).getResultList();
		if (users.size() < 1) {
			User user = new User();
			user.setEmail(masterUserEmail);
			user.setFirstName("Master");
			user.setLastName("User");
			prepareUser(user);
			userDAO.addUser(user);
		}
	}
}
