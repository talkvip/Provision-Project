package com.cpn.vsp.provision;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("certificateAuthenticator")
public class CertificateAuthenticator implements UserDetailsService {
	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		System.out.println(arg0);
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
    authList.add(new SimpleGrantedAuthority("ROLE_USER"));
    authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		return new User("", "", true, true, true, true, authList);
	}

}
