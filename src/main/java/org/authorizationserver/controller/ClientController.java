package org.authorizationserver.controller;

import lombok.AllArgsConstructor;
import org.authorizationserver.dto.ClientDto;
import org.authorizationserver.model.AuthorizationGrantTypePassword;
import org.authorizationserver.service.JpaRegisteredClientService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class ClientController {

	private final JpaRegisteredClientService jpaRegisteredClientService;


	@PostMapping("/client")
	public String createClient(@RequestBody ClientDto clientDto) {

		RegisteredClient demoClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientName("Demo client")
				.clientId("demo-client")
				// {noop} means "no operation," i.e., a raw password without any encoding applied.
				.clientSecret("{noop}demo-secret")
				.redirectUri("http://localhost:8080/auth")
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.authorizationGrantType(AuthorizationGrantTypePassword.GRANT_PASSWORD)
//				.tokenSettings(
//						TokenSettings.builder()
//								.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
//								.accessTokenTimeToLive(Duration.ofMinutes(300))
//								.refreshTokenTimeToLive(Duration.ofMinutes(600))
//								.authorizationCodeTimeToLive(Duration.ofMinutes(20))
//								.reuseRefreshTokens(false)
//								.build()
//				)
//				.clientSettings(
//						ClientSettings.builder()
//								.requireProofKey(true)
//								.requireAuthorizationConsent(true)
//								.jwkSetUrl("http://localhost:8080/jwk")
//								.tokenEndpointAuthenticationSigningAlgorithm(JWSAlgorithm.RS256.getName())
//								.build()
				.build();



		return jpaRegisteredClientService.createClient(clientDto);
	}
}
