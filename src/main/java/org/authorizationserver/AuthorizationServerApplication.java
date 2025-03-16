package org.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.security.oauth2.server.authorization.web.OAuth2TokenEndpointFilter;

@SpringBootApplication
//@EnableEurekaClient
public class AuthorizationServerApplication {
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
//		OAuth2AuthorizationCodeGenerator
	}
}
