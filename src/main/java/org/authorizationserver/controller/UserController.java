package org.authorizationserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.dto.UserDto;
import org.authorizationserver.exception.RegistrationException;
import org.authorizationserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
//@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<UserDto.Response> getCurrentUser() {
		return ResponseEntity.ok(userService.getCurrentUser());
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("registerRequest", RegisterDto.Request.builder().build());
		return "signup";
	}

	@PostMapping("/signup")
	public String handleSignup(RegisterDto.Request registerRequest,
							   HttpServletRequest request,
							   HttpServletResponse response,
							   Model model) {
		try {
			userService.registerUser(registerRequest, request, response);
			return null; // Redirect được xử lý trong service
		} catch (RegistrationException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("registerRequest", registerRequest); // Giữ lại dữ liệu đã nhập
			return "signup"; // Trả về trang signup với thông báo lỗi
		} catch (Exception e) {
			model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
			model.addAttribute("registerRequest", registerRequest);
			return "signup";
		}
	}
}
