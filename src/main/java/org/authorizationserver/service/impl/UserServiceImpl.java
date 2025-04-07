package org.authorizationserver.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.authorizationserver.dao.RoleDaoRepository;
import org.authorizationserver.dao.UserDaoRepository;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.dto.UserDto;
import org.authorizationserver.enums.Provider;
import org.authorizationserver.exception.PasswordNotMatchException;
import org.authorizationserver.exception.RegistrationException;
import org.authorizationserver.exception.UserAlreadyExistException;
import org.authorizationserver.model.RoleModel;
import org.authorizationserver.model.UserModel;
import org.authorizationserver.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserDaoRepository userDaoRepository;
	private final RoleDaoRepository roleDaoRepository;
	private final PasswordEncoder passwordEncoder;

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

	@Override
	public void registerUser(RegisterDto.Request registerRequest, HttpServletRequest request, HttpServletResponse response) {
		try {
			String password = registerRequest.getPassword();
			String confirmPassword = registerRequest.getConfirmPassword();
			if (!password.equals(confirmPassword)) {
				throw new PasswordNotMatchException("Password and confirm password do not match", "", "");
			}

			String email = registerRequest.getEmail();
			UserModel userModelByEmail = userDaoRepository.findByEmail(email);
			if (!Objects.equals(userModelByEmail, null)) {
				throw new UserAlreadyExistException("User already exists", "", "");
			}

			UserModel userModel = toUserModel(registerRequest);
			userDaoRepository.saveUserModel(userModel);

			Authentication auth = new UsernamePasswordAuthenticationToken(
					registerRequest.getEmail(),
					registerRequest.getPassword(),
					Collections.emptyList()
			);

			SecurityContextHolder.getContext().setAuthentication(auth);
			// Lấy URL ban đầu từ SavedRequest (OAuth 2.0 authorize request)
			SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
			String redirectUrl = savedRequest != null ?
					savedRequest.getRedirectUrl() : "/oauth2/authorize?response_type=code&client_id=demo-client&redirect_uri=http://localhost:3000/auth";
			// Redirect về URL ban đầu để tiếp tục OAuth 2.0 flow
			response.sendRedirect(redirectUrl);
		} catch (Exception e) {
			throw new RegistrationException("An unexpected error occurred during registration", e.getMessage(), "");
		}
	}


	private void validateBirthDate(RegisterDto.Request registerRequest) {
		int month = registerRequest.getBirthMonth();
		int day = registerRequest.getBirthDay();
		int year = registerRequest.getBirthYear();

		YearMonth yearMonth = YearMonth.of(year, month);
		int daysInMonth = yearMonth.lengthOfMonth();

		if (day > daysInMonth) {
			throw new RegistrationException("Invalid birth date: Day " + day + " is not valid for month " + month + " in year " + year, "", "");
		}
	}


	private UserModel toUserModel(RegisterDto.Request registerRequest) {
		return UserModel.builder()
				.email(registerRequest.getEmail())
				.password(passwordEncoder.encode(registerRequest.getPassword()))
				.firstName(registerRequest.getFirstName())
				.lastName(registerRequest.getLastName())
				.locale(null)
				.avatarUrl(null)
				.emailVerified(false)
				.active(false)
				.provider(Provider.LOCAL)
				.birthMonth(registerRequest.getBirthMonth())
				.birthDay(registerRequest.getBirthDay())
				.birthYear(registerRequest.getBirthYear())
				.build();
	}
}
