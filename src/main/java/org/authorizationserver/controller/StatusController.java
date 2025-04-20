package org.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.authorizationserver.dto.UserInfoDto;
import org.authorizationserver.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class StatusController {

	private final UserService userService;


	@GetMapping("/notFoundError")
	public String notFoundError() {
		return "notFoundError";
	}

	@GetMapping("/error")
	public String internalServerError() {
		return "error";
	}

	@GetMapping("/")
	public String success(Model model) {
		try {
			UserInfoDto.Response currentUser = userService.getCurrentUser();
			UserInfoDto.ResponseData currentUserData = currentUser.getData();
			model.addAttribute("email", currentUserData.getEmail());
			model.addAttribute("firstName", currentUserData.getFirstName());
			model.addAttribute("lastName", currentUserData.getLastName());
		} catch (Exception e) {
			return "redirect:/login"; // TODO: redirect to error page, not login again.
		}
		return "successLogin";
	}
}
