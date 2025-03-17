package org.authorizationserver.service.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.dao.UserDaoRepository;
import org.authorizationserver.dao.UserDaoRepositoryImpl;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.model.UserModel;
import org.authorizationserver.service.RegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@AllArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
	private final PasswordEncoder passwordEncoder;
	private final UserDaoRepository userDaoRepository;


	@Override
	public RegisterDto.Response register(RegisterDto.Request registerDto, Model model) {

		final String password = registerDto.getPassword();

		if (!password.equals(registerDto.getConfirmPassword())) {
			model.addAttribute("error", "Passwords do not match!");
			return RegisterDto.Response
					.builder()
					.build();
		}

		if (userDaoRepository.findByEmail(registerDto.getEmail()) != null) {
			model.addAttribute("error", "An account already exists with that email!");
			return RegisterDto.Response
					.builder()
					.build();
		}

		// Create a new user

		String encodedPassword = passwordEncoder.encode(password);
		UserModel userModel = UserModel.builder()
				.email(registerDto.getEmail())
				.password(encodedPassword)
				.firstName(registerDto.getFirstName())
				.lastName(registerDto.getLastName())
				.build();

		userDaoRepository.saveUserModel(userModel);

		return null;
	}
}
