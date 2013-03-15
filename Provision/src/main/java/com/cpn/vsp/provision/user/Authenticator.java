package com.cpn.vsp.provision.user;

import com.cpn.vsp.provision.user.session.Session;

public interface Authenticator {

	Session authenticate(User aCredential) throws NoSuchUserException;

	void invalidateSession(String sessionId);

}
