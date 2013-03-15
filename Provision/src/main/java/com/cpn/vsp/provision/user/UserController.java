package com.cpn.vsp.provision.user;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cpn.logging.Logged;
import com.cpn.vsp.provision.certificate.Certificate;
import com.cpn.vsp.provision.certificate.CertificateController;
import com.cpn.vsp.provision.mail.Mailer;
import com.cpn.vsp.provision.user.session.Session;

@Controller
@RequestMapping(value = "/user")
@Transactional
public class UserController {

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	CertificateController certificateController;

	@Autowired
	Authenticator authenticator;

	@Autowired
	UserDAO userDAO;

	@Autowired
	ClaimCheckDAO claimCheckDAO;

	@Autowired
	Mailer mailer;

	@Autowired
	String publicAddress;

	@Autowired
	String publicPort;

	@RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
	@Logged
	public @ResponseBody
	User findByName(@PathVariable String name) {
		return userDAO.findByName(name);
	}

	@Logged
	@RequestMapping(method = RequestMethod.POST, consumes={"application/json", "application/xml"})
	
	public @ResponseBody
	void addUser(@RequestBody User aUser) throws Exception {
		User existingUser = userDAO.findByEmail(aUser.getEmail());
		if (existingUser == null) {
			Certificate certificate = new Certificate();
			certificate.getSubject().put("C", "US");
			certificate.getSubject().put("ST", "California");
			certificate.getSubject().put("O", "ClearPath Networks");
			certificate.getSubject().put("OU", "Engineering Department");
			certificate.getSubject().put("CN", aUser.getFirstName());
			certificate.getSubject().put("L", "El Segundo");
			certificate.getSubject().put("emailAddress", "certs@clearpathnet.com");
			certificate.getSubject().put("subjectAltName", "email:copy");
			certificate.getSubject().setCA(false);
			Certificate signer = entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Certificate.Role.USER.value).getSingleResult();
			certificate.setSelfSigned(false);
			certificate.setDaysValidFor(365 * 5);
			certificate.setRole(0);
			certificate = entityManager.find(Certificate.class, certificateController.sign(signer, certificate).getId());
			aUser.setCertificate(certificate);
			userDAO.addUser(aUser);
			if (aUser.getCompany() == null) {
				sendClaimCheck(aUser);
			}
			return;
		}
		if (existingUser.isPending()) {
			existingUser.setPassword(aUser.getPassword());
			existingUser.setPending(false);
			userDAO.mergeUser(existingUser);
			sendClaimCheck(aUser);
			return;
		}
		if (!existingUser.isVerified()) {
			sendClaimCheck(aUser);
			return;
		}
		throw new UserAlreadyExistsException();

	}

	public void sendClaimCheck(User aUser) {
		ClaimCheck claimCheck = claimCheckDAO.makeClaimCheckForUser(aUser);
		String body = "You (or someone with your email) signed up for our Private Cloud. Please click on the following link to activate your account http://" + publicAddress + (publicPort == "80" ? "" : ":" + publicPort) + "/Provision/claimCheck/"
				+ claimCheck.getId();
		mailer.sendMail("tsavo@clearpathnet.com", aUser.getEmail(), "Welcome to ClearPath Networks Private Cloud!", body);
		// return claimCheckDAO.getClaimCheckForUser(aUser).getId();
	}

	public User createPendingUser(String anEmail) throws Exception {
		Certificate certificate = new Certificate();
		certificate.getSubject().put("C", "US");
		certificate.getSubject().put("ST", "California");
		certificate.getSubject().put("O", "ClearPath Networks");
		certificate.getSubject().put("OU", "Engineering Department");
		certificate.getSubject().put("CN", anEmail);
		certificate.getSubject().put("L", "El Segundo");
		certificate.getSubject().put("emailAddress", "certs@clearpathnet.com");
		certificate.getSubject().put("subjectAltName", "email:copy");
		certificate.getSubject().setCA(false);
		Certificate signer = entityManager.createQuery("from Certificate where bitwise_and(role, :role) = :role", Certificate.class).setParameter("role", Certificate.Role.USER.value).getSingleResult();
		certificate.setSelfSigned(false);
		certificate.setDaysValidFor(365 * 5);
		certificate.setRole(0);
		certificate = entityManager.find(Certificate.class, certificateController.sign(signer, certificate).getId());
		User user = new User();
		user.setCertificate(certificate);
		user.setEmail(anEmail);
		user.setPending(true);
		userDAO.addUser(user);
		return user;
		// return claimCheckDAO.getClaimCheckForUser(aUser).getId();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/session")
	public @ResponseBody
	Session authenticate(@RequestBody User aCredential) throws NoSuchUserException {
		return authenticator.authenticate(aCredential);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/session/{sessionId}")
	public @ResponseBody
	void signOut(@PathVariable String sessionId) {
		authenticator.invalidateSession(sessionId);
	}
}
