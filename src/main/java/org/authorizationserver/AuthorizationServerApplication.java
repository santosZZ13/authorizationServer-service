package org.authorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationCodeGrantFilter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.web.OAuth2AuthorizationEndpointFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@SpringBootApplication
//@EnableEurekaClient
public class AuthorizationServerApplication {
	OAuth2AuthorizationCodeRequestAuthenticationProvider provider;
	public static void main(String[] args) {
		SpringApplication.run(AuthorizationServerApplication.class, args);
//		OAuth2LoginAuthenticationProvider
//		OAuth2AuthorizationCodeAuthenticationFilter
//		OAuth2AuthorizationCodeGrantFilter
//		OAuth2AuthorizationEndpointFilter
//		UsernamePasswordAuthenticationFilter
//		DaoAuthenticationProvider
//		DaoAuthenticationProvider
//		FilterSecurityInterceptor
//		OAuth2TokenEndpointFilter
//		BearerTokenAuthenticationFilter
//		InMemoryOAuth2AuthorizationService
//		RegisteredClientRepository
//		JdbcOAuth2AuthorizationService
//		OAuth2TokenIntrospectionAuthenticationProvider
//		OAuth2AuthorizationEndpointFilter
//		OidcUserService
		;
	}
}
