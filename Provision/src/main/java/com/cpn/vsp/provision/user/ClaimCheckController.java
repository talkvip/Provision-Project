package com.cpn.vsp.provision.user;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/claimCheck")
@Transactional
public class ClaimCheckController {

	@Autowired
	ClaimCheckDAO claimCheckDAO;

	@RequestMapping(value="/{claimCheckId}", method=RequestMethod.GET)
	public void claim(@PathVariable("claimCheckId") String aClaimCheckId, HttpServletResponse aResponse) throws IOException {
		try {
			claimCheckDAO.claim(aClaimCheckId);
		} catch (AlreadyClaimedException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchClaimCheckException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		aResponse.sendRedirect("/VPNClient/#signIn");
	}

}
