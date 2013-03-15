package com.cpn.vsp.provision.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import com.cpn.vsp.provision.user.session.Session;
import com.cpn.vsp.provision.user.session.SessionDAO;

public class CollaberationGroupControllerTest {

	@Test
	public void testCreateNewGroupForUser() {
		CollaborationGroupController controller = new CollaborationGroupController();
		User user = Mockito.mock(User.class);
		CollaborationGroupDAO dao = Mockito.mock(CollaborationGroupDAO.class);
		controller.collaborationGroupDAO = dao;
		CollaborationGroup group = Mockito.mock(CollaborationGroup.class);
		Session session = Mockito.mock(Session.class);
		SessionDAO sessionDAO = Mockito.mock(SessionDAO.class);
		when(sessionDAO.getSessionById("sessionId")).thenReturn(session);
		
		when(session.getUser()).thenReturn(user);
		when(dao.createGroupForUser(user)).thenReturn(group);
		
		controller.sessionDAO = sessionDAO;
		CollaborationGroup createdGroup = controller.createGroupForUser("sessionId");
		assertEquals(createdGroup, group);
		verify(dao.createGroupForUser(user));
		
		
	}

	@Test
	public void testAddUserToGroup() {
		CollaborationGroupController controller = new CollaborationGroupController();
		//User user = Mockito.mock(User.class);
		CollaborationGroupDAO dao = Mockito.mock(CollaborationGroupDAO.class);
		controller.collaborationGroupDAO = dao;
		//CollaborationGroup group = Mockito.mock(CollaborationGroup.class);
		
		//controller.addUserToGroup("email");
		
		//verify(dao).addUserToGroup("email");
		
		
	}
}
