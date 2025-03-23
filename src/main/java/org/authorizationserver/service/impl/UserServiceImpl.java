package org.authorizationserver.service.impl;

import org.authorizationserver.dto.UserDto;
import org.authorizationserver.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


	@Override
	public UserDto.Response getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
//			return ResponseEntity
//					.status(HttpStatus.UNAUTHORIZED)
//					.body(Map.of("status", "error", "message", "Not authenticated"));
			return null;
		}
		String username;
		Set<String> roles;

		if (authentication instanceof JwtAuthenticationToken jwtAuth) {
			// Xử lý khi xác thực bằng JWT (access_token từ cookie)
			username = (String) jwtAuth.getTokenAttributes().get("username");
			roles = ((Set<String>) jwtAuth.getTokenAttributes().get("authorities"));

		} else if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuth) {
			// Xử lý khi xác thực bằng form login
			UserDetails userDetails = (UserDetails) usernamePasswordAuth.getPrincipal();
			username = userDetails.getUsername();
			roles = userDetails.getAuthorities().stream()
					.map(Object::toString)
					.collect(Collectors.toSet());
		} else {
			// Nếu không phải loại Authentication mong muốn, trả về null hoặc lỗi
			return null;
		}

		return UserDto.Response.builder()
				.data(UserDto.ResponseData.builder()
						.username(username)
						.roles(roles)
						.build())
				.build();
	}
}
