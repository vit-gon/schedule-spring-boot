package com.vitgon.schedule.service.auth;

import com.vitgon.schedule.model.UserDetailsImpl;
import com.vitgon.schedule.model.database.auth.User;
import com.vitgon.schedule.service.database.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserService userService;

	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
		User user = userService.findByEmailAndProviderId(emailOrUsername, "local");
		if (user == null) {
			user = userService.findByUsername(emailOrUsername);
		}
		
		if (user == null) {
			throw new UsernameNotFoundException(emailOrUsername);
		}
		
		final UserDetailsImpl userDetails = new UserDetailsImpl(
				user.getUsername(),
				user.getPassword(),
				user.getFirstname(),
				user.getLastname(),
				user.getRoles()
		);
		return userDetails;
	}

}
