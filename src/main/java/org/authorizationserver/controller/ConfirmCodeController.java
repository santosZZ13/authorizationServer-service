package org.authorizationserver.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.authorizationserver.exception.CannotConfirmCodeException;
import org.authorizationserver.service.VerifyUserService;
import org.authorizationserver.util.RedirectHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
@AllArgsConstructor
public class ConfirmCodeController {

	private final VerifyUserService verifyUserService;
	private final RedirectHandler redirectHandler;

	@GetMapping("/confirmCode")
	public String confirmCode() {
		return "confirmCode";
	}

	@PostMapping("/confirmCode")
	public String confirmCode(
			@RequestParam("code") String code,
			Model model,
			HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String verificationEmail = request.getSession().getAttribute("verificationEmail").toString();
			verifyUserService.verifyCode(verificationEmail, code);
			SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
			if (Objects.isNull(savedRequest)) {
				request.getSession().removeAttribute("verificationEmail");
				return redirectHandler.redirectToHome();
			}
			request.getSession().removeAttribute("verificationEmail");
			redirectHandler.redirectOauth2Flow(response);
			return null;
		} catch (CannotConfirmCodeException e) {
			model.addAttribute("error", e.getMessage());
			return "confirmCode";
		} catch (Exception e) {
			return "error";
		}
	}
}
