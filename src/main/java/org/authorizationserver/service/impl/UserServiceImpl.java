package org.authorizationserver.service.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.persistent.entity.User;
import org.authorizationserver.persistent.repository.UserRepository;
import org.authorizationserver.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public User getUserByEmail(String email) {
		if (!StringUtils.hasText(email)) {
			return null;
		}
		return userRepository.findByEmail(email).orElse(null);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}
}
