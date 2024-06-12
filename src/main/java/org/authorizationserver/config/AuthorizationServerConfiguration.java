package org.authorizationserver.config;

import lombok.AllArgsConstructor;
import org.authorizationserver.security.converter.OAuth2GrantPasswordAuthenticationConverter;
import org.authorizationserver.security.provider.GrantPasswordAuthenticationProvider;
import org.authorizationserver.security.repository.JpaRegisteredClientRepository;
import org.authorizationserver.security.service.JpaOAuth2AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;



import static org.springframework.security.config.Customizer.withDefaults;


@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
public class AuthorizationServerConfiguration {


	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationSecurityFilterChain(
			HttpSecurity httpSecurity,
			GrantPasswordAuthenticationProvider grantPasswordAuthenticationProvider,
			JpaOAuth2AuthorizationService  jpaOAuth2AuthorizationService,
			JpaRegisteredClientRepository jpaRegisteredClientRepository
	) throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.tokenEndpoint(tokenEndpoint ->
								tokenEndpoint
										.accessTokenRequestConverter(new OAuth2GrantPasswordAuthenticationConverter())
										.authenticationProvider(grantPasswordAuthenticationProvider)
//								.authenticationProvider(daoAuthenticationProvider)
				)
				.oidc(withDefaults()) // Enable OpenID Connect 1.0
//		.registeredClientRepository()
//				.authorizationService(authorizationService())
//		.authorizationConsentService()
//		.authorizationServerSettings()
//				.tokenGenerator()
//				.clientAuthentication(clientAuthentication ->
//						clientAuthentication
//								.authenticationConverter(authenticationConverter)
//								.authenticationConverters(authenticationConvertersConsumer)
//								.authenticationProvider(authenticationProvider)
//								.authenticationProviders(authenticationProvidersConsumer)
//								.authenticationSuccessHandler(authenticationSuccessHandler)
//								.errorResponseHandler(errorResponseHandler)
//				)
				/*
				 * OAuth 2.0 Authorization Service Configuration
				 */
				.authorizationService(jpaOAuth2AuthorizationService)
				/*
				 * OAuth 2.0 Registered Client Repository
				 */
				.registeredClientRepository(jpaRegisteredClientRepository)

				.authorizationEndpoint(authorizationEndpoint -> { })
				.deviceAuthorizationEndpoint(deviceAuthorizationEndpoint -> { })
				.deviceVerificationEndpoint(deviceVerificationEndpoint -> { })
				.tokenEndpoint(tokenEndpoint -> { })
				.tokenIntrospectionEndpoint(tokenIntrospectionEndpoint -> { })
				.tokenRevocationEndpoint(tokenRevocationEndpoint -> { })
				.authorizationServerMetadataEndpoint(authorizationServerMetadataEndpoint -> { })
				.oidc(oidc -> oidc
						.providerConfigurationEndpoint(providerConfigurationEndpoint -> { })
						.logoutEndpoint(logoutEndpoint -> { })
						.userInfoEndpoint(userInfoEndpoint -> { })
						.clientRegistrationEndpoint(clientRegistrationEndpoint -> { })
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
		return new GrantPasswordAuthenticationProvider(userDetailsService, passwordEncoder, jwtTokenCustomizer, authorizationService);
	}

//	@Bean
//	public OAuth2AuthorizationService authorizationService() {
//		return new InMemoryOAuth2AuthorizationService();
//	}


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


//	@Bean
//	public RegisteredClientRepository registeredClientRepository() {
//
//		RegisteredClient demoClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientName("Demo client")
//				.clientId("demo-client")
//				// {noop} means "no operation," i.e., a raw password without any encoding applied.
//				.clientSecret("{noop}demo-secret")
//				.redirectUri("http://localhost:8080/auth")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.authorizationGrantType(AuthorizationGrantTypePassword.GRANT_PASSWORD)
////				.tokenSettings(
////						TokenSettings.builder()
////								.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
////								.accessTokenTimeToLive(Duration.ofMinutes(300))
////								.refreshTokenTimeToLive(Duration.ofMinutes(600))
////								.authorizationCodeTimeToLive(Duration.ofMinutes(20))
////								.reuseRefreshTokens(false)
////								.build()
////				)
//				.build();
//
//		return new InMemoryRegisteredClientRepository(demoClient);
//	}
}
