package org.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;

@SpringBootApplication

public class AuthorizationServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
//		OAuth2TokenEndpointFilter
//		BearerTokenAuthenticationFilter
//		InMemoryOAuth2AuthorizationService
//		RegisteredClientRepository
//		JdbcOAuth2AuthorizationService
	}
}
