package org.authorizationserver.config;

import lombok.AllArgsConstructor;
import org.authorizationserver.converter.OAuth2GrantPasswordAuthenticationConverter;
import org.authorizationserver.model.AuthorizationGrantTypePassword;
import org.authorizationserver.properties.OAuth2ClientCustomProperties;
import org.authorizationserver.provider.GrantPasswordAuthenticationProvider;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;


@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
public class AuthorizationServerConfiguration {

	private final OAuth2ClientCustomProperties oAuth2ClientProperties;


	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationSecurityFilterChain(
			HttpSecurity httpSecurity,
			GrantPasswordAuthenticationProvider grantPasswordAuthenticationProvider
	) throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.tokenEndpoint(tokenEndpoint ->
						tokenEndpoint
								.accessTokenRequestConverter(new OAuth2GrantPasswordAuthenticationConverter())
								.authenticationProvider(grantPasswordAuthenticationProvider)
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
	public GrantPasswordAuthenticationProvider grantPasswordAuthenticationProvider(
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder,
			OAuth2TokenGenerator<?> jwtTokenCustomizer,
			OAuth2AuthorizationService authorizationService

	) {
		return new GrantPasswordAuthenticationProvider(userDetailsService,passwordEncoder,jwtTokenCustomizer, authorizationService);
	}

	@Bean
	public OAuth2AuthorizationService authorizationService() {
		return new InMemoryOAuth2AuthorizationService();
	}


	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(
			PasswordEncoder passwordEncoder, UserDetailsService userDetailsService
	) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}



	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
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
				.authorizationGrantType(AuthorizationGrantTypePassword.GRANT_PASSWORD)
				.build();

		return new InMemoryRegisteredClientRepository(demoClient);
	}
}
