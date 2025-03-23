package org.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.authorizationserver.dto.UserDto;
import org.authorizationserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<UserDto.Response> getCurrentUser() {
		return ResponseEntity.ok(userService.getCurrentUser());
	}
}
