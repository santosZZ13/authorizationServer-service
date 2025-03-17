package org.authorizationserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/index")
	public String home() {
		return "index";
	}

	@GetMapping("/login")
	public String customLogin() {
		return "login";
	}
}
