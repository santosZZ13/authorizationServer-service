package org.authorizationserver.config;

import lombok.AllArgsConstructor;
import org.authorizationserver.converter.OAuth2GrantPasswordAuthenticationConverter;
import org.authorizationserver.properties.OAuth2ClientCustomProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;


@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
public class AuthorizationServerConfiguration {

	private final OAuth2ClientCustomProperties oAuth2ClientProperties;



	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.tokenEndpoint(tokenEndpoint ->
						tokenEndpoint
								.accessTokenRequestConverter(new OAuth2GrantPasswordAuthenticationConverter())
//								.authenticationProvider(grantPasswordAuthenticationProvider)
//								.authenticationProvider(daoAuthenticationProvider)
				);



		httpSecurity
				.exceptionHandling(
						exceptions ->
								exceptions.authenticationEntryPoint(
										new LoginUrlAuthenticationEntryPoint("/login")
								)
				);




		return httpSecurity.build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {

		RegisteredClient demoClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientName("Demo client")
				.clientId("demo-client")
				// {noop} means "no operation," i.e., a raw password without any encoding applied.
				.clientSecret("{noop}demo-secret")
				.redirectUri("http://localhost:8080/auth")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.build();

		return new InMemoryRegisteredClientRepository(demoClient);
	}
}
