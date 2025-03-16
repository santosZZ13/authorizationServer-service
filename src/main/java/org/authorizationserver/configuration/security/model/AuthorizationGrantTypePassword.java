package org.authorizationserver.configuration.security.model;


import org.springframework.security.oauth2.core.AuthorizationGrantType;

public final class AuthorizationGrantTypePassword {
	public static final AuthorizationGrantType GRANT_PASSWORD =
			new AuthorizationGrantType("grant_password");
}
