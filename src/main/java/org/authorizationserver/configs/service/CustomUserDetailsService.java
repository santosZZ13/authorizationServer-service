package org.authorizationserver.configs.service;

import lombok.AllArgsConstructor;
import org.authorizationserver.dao.UserDaoRepository;
import org.authorizationserver.model.UserModel;
import org.authorizationserver.configs.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserDaoRepository userDaoRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserModel userModel = userDaoRepository.findByEmail(username);

		if (Objects.isNull(userModel)) {
			throw new UsernameNotFoundException("Unable to found user: " + username);
		}

		return new CustomUserDetails(userModel);
	}
}
