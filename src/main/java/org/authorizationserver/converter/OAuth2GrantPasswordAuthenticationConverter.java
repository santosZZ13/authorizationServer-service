package org.authorizationserver.converter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class OAuth2GrantPasswordAuthenticationConverter implements AuthenticationConverter {
	@Override
	public Authentication convert(HttpServletRequest request) {
		return null;
	}
}
