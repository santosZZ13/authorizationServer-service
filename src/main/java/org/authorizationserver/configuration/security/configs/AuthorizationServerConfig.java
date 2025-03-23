package org.authorizationserver.configuration.security.configs;

import org.authorizationserver.configuration.security.converter.OAuth2GrantPasswordAuthenticationConverter;
import org.authorizationserver.configuration.security.filter.CustomTokenEndpoint;
import org.authorizationserver.configuration.security.model.AuthorizationGrantTypePassword;
import org.authorizationserver.configuration.security.provider.GrantPasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


import java.util.Objects;
import java.util.UUID;

/**
 * For everything related to the OAuth2 authorization server
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationSecurityFilterChain(
			HttpSecurity http,
			GrantPasswordAuthenticationProvider grantPasswordAuthenticationProvider,
			DaoAuthenticationProvider daoAuthenticationProvider,
			OAuth2AuthorizationService authorizationService,
			CustomTokenEndpoint customTokenEndpoint
	) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		http
				.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.authorizationEndpoint(authorizationEndpoint ->
						authorizationEndpoint
								.authorizationResponseHandler((request, response, authentication) -> {
									// Lấy thông tin từ authentication
									// This runs after the user has authenticated and approved the authorization request
									// The authorization server will redirect the user to the client with an authorization code
									OAuth2AuthorizationCodeRequestAuthenticationToken authorization = (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
									String code = Objects.requireNonNull(authorization.getAuthorizationCode()).getTokenValue();
									String redirectUri = authorization.getRedirectUri();
									String redirectUrl = redirectUri + "?code=" + code;
									response.sendRedirect(redirectUrl);
								})
				)
				.tokenEndpoint(tokenEndpoint ->
						tokenEndpoint
								.accessTokenRequestConverter(new OAuth2GrantPasswordAuthenticationConverter())
								.authenticationProvider(grantPasswordAuthenticationProvider)
								.authenticationProvider(daoAuthenticationProvider)
								.accessTokenResponseHandler((request, response, authentication) -> {
									try {
										customTokenEndpoint.handleTokenResponse(response, authentication);
									} catch (Exception e) {
										throw new RuntimeException(e);
									}
								})
				);
//				.oidc(Customizer.withDefaults()); // Enable OpenID Connect 1.0;

		// Nó redirect mày đến trang login (thường là /login, dựa trên cấu hình LoginUrlAuthenticationEntryPoint trong code của mày).
		// Mày cần phải override nó để redirect mày đến trang login của mày.
		http.exceptionHandling(exceptions ->
				exceptions.authenticationEntryPoint(
						new LoginUrlAuthenticationEntryPoint("/login")
				)
		);

//		http
//				.exceptionHandling(exceptions ->
//						{
//							try {
//								exceptions.authenticationEntryPoint(
//										new LoginUrlAuthenticationEntryPoint("/login?client_id=" +
//												URLEncoder.encode("demo-client", StandardCharsets.UTF_8.toString()) +
//												"&redirect_uri=" + URLEncoder.encode("http://localhost:3000/auth", StandardCharsets.UTF_8.toString()) +
//												"&response_type=code")
//								);
//							} catch (UnsupportedEncodingException e) {
//								throw new RuntimeException(e);
//							}
//						}
//				);

		return http.build();
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder()
				.issuer("http://localhost:8002")
				.authorizationEndpoint("/oauth2/authorize")
				.tokenEndpoint("/oauth2/token")
				.build();
	}


	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
															   UserDetailsService userDetailsService) {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		return daoAuthenticationProvider;
	}


	@Bean
	public OAuth2AuthorizationService authorizationService() {
		return new InMemoryOAuth2AuthorizationService();
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
				.clientSecret("{noop}demo-secret")
				.redirectUri("http://localhost:3000/auth")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantTypePassword.GRANT_PASSWORD)
				.build();
		return new InMemoryRegisteredClientRepository(demoClient);
	}
}
