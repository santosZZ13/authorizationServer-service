package org.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;


@SpringBootApplication
//@EnableEurekaClient
public class AuthorizationServerApplication {
	OAuth2AuthorizationCodeRequestAuthenticationProvider provider;
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
//		FilterSecurityInterceptor
//		OAuth2TokenEndpointFilter
//		BearerTokenAuthenticationFilter
//		InMemoryOAuth2AuthorizationService
//		RegisteredClientRepository
//		JdbcOAuth2AuthorizationService
//		OAuth2TokenIntrospectionAuthenticationProvider
//		OAuth2AuthorizationEndpointFilter

		;
	}
}
