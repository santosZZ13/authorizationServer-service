package org.authorizationserver.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@AllArgsConstructor
public class RedirectHandler {

	private final static String REDIRECT_ROOT = "redirect:";
	private final static String REDIRECT_OAUTH2 = "/oauth2/authorize?response_type=code&client_id=demo-client&redirect_uri=http://localhost:3000/auth";

	public void redirectOauth2Flow(HttpServletResponse response) {

	}

	public String redirectTo(String redirectUri) {
		return REDIRECT_ROOT + redirectUri;
	}

	public String redirectToHome() {
		return redirectTo("/");
	}
}
