package com.cpn.vsp.provision.user;


import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;
public class ClaimCheckControllerTest {

	
	@Test
	public void testClaimCheck() throws AlreadyClaimedException, NoSuchClaimCheckException, IOException{
		ClaimCheckController claimCheckController = new ClaimCheckController();
		ClaimCheckDAO claimCheckDAO = Mockito.mock(ClaimCheckDAO.class);
		claimCheckController.claimCheckDAO = claimCheckDAO;
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		claimCheckController.claim("12345", response);
		verify(claimCheckDAO).claim("12345");
	}

}
