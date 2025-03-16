//package org.authorizationserver.configuration.security.configs;
//
//import org.authorizationserver.configuration.security.converter.OAuth2GrantPasswordAuthenticationConverter;
//import org.authorizationserver.configuration.security.provider.GrantPasswordAuthenticationProvider;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
//
//public class AuthorizationEndpointConfigure {
//	/**
//	 * Configure Token Endpoint - /oauth2/token
//	 *
//	 * @param httpSecurity
//	 * @param grantPasswordAuthenticationProvider
//	 */
//	public static void configureTokenEndpoint(@NotNull HttpSecurity httpSecurity, @NotNull GrantPasswordAuthenticationProvider grantPasswordAuthenticationProvider) {
//		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//				.tokenEndpoint(tokenEndpoint ->
//						tokenEndpoint
//								.accessTokenRequestConverter(new OAuth2GrantPasswordAuthenticationConverter())
//								.authenticationProvider(grantPasswordAuthenticationProvider)
////								.accessTokenResponseHandler(new TokenEndpointSuccessHandler())
////								.errorResponseHandler(new TokenEndpointSuccessFailure())
//				);
//	}
//
//	/**
//	 * Configure Authorization Endpoint - /oauth2/authorize
//	 *
//	 * @param httpSecurity
//	 * @throws Exception
//	 */
//	public static void configureAuthorizationEndpoint(@NotNull HttpSecurity httpSecurity) {
//		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//				.authorizationEndpoint(authorizationEndpoint -> {
////					authorizationEndpoint.authorizationRequestConverter()
////					authorizationEndpoint.authenticationProvider()
////					authorizationEndpoint.authorizationResponseHandler()
////					authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI);
//				});
//	}
//
//
//	/**
//	 * Configure Device Authorization Endpoint - /oauth2/device
//	 *
//	 * @param httpSecurity
//	 * @throws Exception
//	 */
//	public static void configureDeviceAuthorizationEndpoint(@NotNull HttpSecurity httpSecurity) {
//		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//				.deviceAuthorizationEndpoint(deviceAuthorizationEndpoint -> {
//				});
//	}
//
//
//	/**
//	 * Configure Device Verification Endpoint - /oauth2/device/verification
//	 *
//	 * @param httpSecurity
//	 * @throws Exception
//	 */
//	public static void configureDeviceVerificationEndpoint(@NotNull HttpSecurity httpSecurity) {
//		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//				.deviceVerificationEndpoint(deviceVerificationEndpoint -> {
//				});
//	}
//
//	/**
//	 * Configure Token Introspection Endpoint - /oauth2/introspect
//	 * @param httpSecurity
//	 */
//	public static void configureTokenIntrospectionEndpoint(@NotNull HttpSecurity httpSecurity) {
//		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//				.tokenIntrospectionEndpoint(tokenIntrospectionEndpoint -> {
//				});
//	}
//
//
//	/**
//	 * Configure Token Revocation Endpoint - /oauth2/revoke
//	 * @param httpSecurity
//	 */
//	public static void configureTokenRevocationEndpoint(@NotNull HttpSecurity httpSecurity) {
//		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//				.tokenRevocationEndpoint(tokenRevocationEndpoint -> {
//				});
//	}
//
//
//	/**
//	 * Configure Authorization Server Metadata Endpoint - /.well-known/oauth-authorization-server
//	 * @param httpSecurity
//	 */
//	public static void configureAuthorizationServerMetadataEndpoint(@NotNull HttpSecurity httpSecurity) {
//		httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
//				.authorizationServerMetadataEndpoint(authorizationServerMetadataEndpoint -> {
//				});
//	}
//}
