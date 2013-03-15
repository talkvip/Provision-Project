package com.cpn.vsp.provision.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cpn.vsp.provision.mail.Mailer;
import com.cpn.vsp.provision.user.session.Session;
import com.cpn.vsp.provision.user.session.SessionDAO;

@RequestMapping("/collaberationGroup")
public class CollaborationGroupController {

	@Autowired
	CollaborationGroupDAO collaborationGroupDAO;

	@Autowired
	SessionDAO sessionDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	Mailer mailer;

	@Autowired
	UserController userController;

	@Autowired
	String publicAddress;
	@Autowired
	String publicPort;

	@RequestMapping(method = RequestMethod.GET, value = "/member/{sessionId}")
	public List<CollaborationGroup> getGroupsForSession(@PathVariable String sessionId) {
		Session session = sessionDAO.getSessionById(sessionId);
		return collaborationGroupDAO.getGroupsForUser(session.getUser());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{sessionId}")
	public CollaborationGroup createGroupForUser(String sessionId) {
		Session session = sessionDAO.getSessionById(sessionId);
		return collaborationGroupDAO.createGroupForUser(session.getUser());
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{sessionId}/{email}")
	public void addUserToGroup(@PathVariable("sessionId") String sessionId, @PathVariable("email") String anEmail) throws Exception {
		Session session = sessionDAO.getSessionById(sessionId);
		//CollaborationGroup group = session.getUser().getCollaborationGroups().get(0);
		User user = userDAO.findByEmail(anEmail);
		if (user == null) {
			user = userController.createPendingUser(anEmail);
		}
		//collaborationGroupDAO.addUserToGroup(user, group);
		String body = "You (or someone with your email) were invited by " + session.getUser().getEmail() + " to join a Private Cloud. Please click on the following link to sign in: http://" + publicAddress + (publicPort == "80" ? "" : ":" + publicPort)
				+ "/VPNClient";
		mailer.sendMail("tsavo@clearpathnet.com", anEmail, "You've been invited to join a private cloud by " + session.getUser().getEmail() + "!", body);

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{sessionId}/{email}")
	public void removeUserFromGroup(@PathVariable("sessionId") String sessionId, @PathVariable("email") String anEmail) throws Exception {
		//Session session = sessionDAO.getSessionById(sessionId);
		//CollaborationGroup group = session.getUser().getCollaborationGroups().get(0);
		User user = userDAO.findByEmail(anEmail);
		if (user == null) {
			throw new NoSuchUserException();
		}
		//collaborationGroupDAO.removeUserFromGroup(user, group);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/{sessionId}")
	public CollaborationGroup getCollaborationGroup(@PathVariable String sessionId){
		return null;
		//return sessionDAO.getSessionById(sessionId).getUser().getCollaborationGroups().get(0);
	}
}
