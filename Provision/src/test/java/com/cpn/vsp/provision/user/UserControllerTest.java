package com.cpn.vsp.provision.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import com.cpn.vsp.provision.user.session.Session;
public class UserControllerTest {

	
	@Test
	public void testUserControllerSignIn() throws NoSuchUserException{
		UserController userController = new UserController();
		User credential = Mockito.mock(User.class);
		Authenticator authenticator = Mockito.mock(Authenticator.class);
		Session session = Mockito.mock(Session.class);
		
		userController.authenticator = authenticator;
		when(authenticator.authenticate(credential)).thenReturn(session);
		
		assertEquals(userController.authenticate(credential), session);
	}
	
	public void testUserControllerSignOut(){
		UserController userController = new UserController();
		Authenticator authenticator = Mockito.mock(Authenticator.class);
		userController.authenticator = authenticator;
		Session session = Mockito.mock(Session.class);
		when(session.getId()).thenReturn("12345");
		userController.signOut(session.getId());
		verify(authenticator).invalidateSession(session.getId());
	}
}
