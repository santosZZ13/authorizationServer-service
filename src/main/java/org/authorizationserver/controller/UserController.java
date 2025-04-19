package org.authorizationserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.authorizationserver.dto.RegisterDto;
import org.authorizationserver.dto.UserInfoDto;
import org.authorizationserver.exception.RegistrationException;
import org.authorizationserver.service.UserService;
import org.authorizationserver.util.DateUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
//@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<UserInfoDto.Response> getCurrentUser() {
		return ResponseEntity.ok(userService.getCurrentUser());
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("registerRequest", RegisterDto.Request.builder().build());
		addDateAttributesToModel(model);
		return "signup";
	}

	@PostMapping("/signup")
	public String handleSignup(@Valid RegisterDto.Request registerRequest,
							   BindingResult bindingResult,
							   HttpServletRequest request,
							   HttpServletResponse response,
							   Model model) {
		try {
			if (hasError(bindingResult, model, registerRequest)) {
				return "signup";
			}
			userService.registerUser(registerRequest, request, response);
			return null; // Redirect được xử lý trong service
		} catch (RegistrationException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("registerRequest", registerRequest);
			addDateAttributesToModel(model);
			return "signup";
		} catch (Exception e) {
			model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
			model.addAttribute("registerRequest", registerRequest);
			addDateAttributesToModel(model);
			return "signup";
		}
	}

	@GetMapping("/days-in-month")
	@ResponseBody
	public List<String> getDaysInMonth(@RequestParam("month") int month, @RequestParam("year") int year) {
		return DateUtils.getDaysInMonth(month, year);
	}

	private boolean hasError(BindingResult bindingResult, Model model, RegisterDto.Request registerRequest) {
		if (bindingResult.hasErrors()) {
			String errorMessage = bindingResult.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.joining("; "));
			model.addAttribute("error", "Please correct the errors in the form: " + errorMessage);
			model.addAttribute("registerRequest", registerRequest);
			addDateAttributesToModel(model);
			return true;
		}
		return false;
	}

	private void addDateAttributesToModel(Model model) {
		model.addAttribute("monthNames", DateUtils.getMonthNames());
		model.addAttribute("months", DateUtils.getMonths());
		model.addAttribute("days", DateUtils.getDays(31)); // Ban đầu hiển thị 1-31
		model.addAttribute("years", DateUtils.getYears());
	}
}
