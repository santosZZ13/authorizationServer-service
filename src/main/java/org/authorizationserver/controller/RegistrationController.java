package org.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class RegistrationController {

	private final RegistrationService registrationService;

	@GetMapping("/sign-up")
	public String showRegistrationForm() {
		return "register";
	}

	@PostMapping("/register")
	public RegisterDto.Response register(@RequestParam("firstName") String firstName,
										 @RequestParam("lastName") String lastName,
										 @RequestParam("email") String email,
										 @RequestParam("password") String password,
										 @RequestParam("confirmPassword") String confirmPassword,
										 Model model) {
		RegisterDto.Request request = RegisterDto.Request.builder()
				.firstName(firstName)
				.lastName(lastName)
				.email(email)
				.password(password)
				.confirmPassword(confirmPassword)
				.build();
		return registrationService.register(request, model);
	}
}
