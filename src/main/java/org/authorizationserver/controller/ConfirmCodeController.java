package org.authorizationserver.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.authorizationserver.exception.RegistrationException;
import org.authorizationserver.service.UserService;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class ConfirmCodeController {
	private final UserService userService;

	@GetMapping("/confirmCode")
	public String confirmCode() {
		return "confirmCode";
	}


	@PostMapping("/confirmCode")
	public String confirmCode(@RequestParam("email") String email,
							  @RequestParam("code") String code,
							  Model model,
							  HttpServletRequest request, HttpServletResponse response) {
		try {
			userService.verifyCode(email, code);
			// Sau khi xác nhận thành công, tiếp tục OAuth2 flow
			SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
			String redirectUrl = savedRequest != null ? savedRequest.getRedirectUrl() :
					"/oauth2/authorize?response_type=code&client_id=demo-client&redirect_uri=http://localhost:3000/auth";
//			response.sendRedirect("/oauth2/authorize?response_type=code&client_id=demo-client&redirect_uri=http://localhost:3000/auth");
			response.sendRedirect(redirectUrl);
			return null; // Hoặc trả về view nếu cần
		} catch (RegistrationException e) {
			model.addAttribute("error", e.getMessage());
			return "confirmCode";
		} catch (IOException e) {
			model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
			return "confirmCode";
		}
	}
}
