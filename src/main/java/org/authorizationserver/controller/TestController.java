package org.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.authorizationserver.persistent.entity.User;
import org.authorizationserver.persistent.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

	private final UserRepository userRepository;

	@GetMapping("/foo")
	public Iterable<User> foo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findAll();
	}
}
