package org.authorizationserver.service.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.configuration.security.repository.JpaRegisteredClientRepository;
import org.authorizationserver.dto.clientdto.ClientDTORequest;
import org.authorizationserver.dto.clientdto.ClientDTO;
import org.authorizationserver.exception.client.RedirectIsNotExistInAuthorizationCodeModeException;
import org.authorizationserver.exception.util.GenericResponseSuccessWrapper;
import org.authorizationserver.service.JpaRegisteredClientService;
import org.authorizationserver.util.OauthResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.*;

@Service
@AllArgsConstructor
public class JpaRegisteredClientServiceImpl implements JpaRegisteredClientService {

	private final JpaRegisteredClientRepository jpaRegisteredClientRepository;
	private final PasswordEncoder passwordEncoder;


	@Override
	@Transactional
	public GenericResponseSuccessWrapper createClient(ClientDTORequest clientDTORequest) {
//		RegisteredClient byClientId = jpaRegisteredClientRepository.findByClientId(clientDTORequest.getClient().getClientId());
//		if (byClientId != null) {
//			throw new RegisterClientExistException("", "", "");
//		}

		String registerId = UUID.randomUUID().toString();
		RegisteredClient.Builder registerClientBuilder =
				RegisteredClient.withId(registerId)
						.clientId(clientDTORequest.getClient().getClientId())
						.clientName(Objects.isNull(clientDTORequest.getClient().getClientName()) ? registerId : clientDTORequest.getClient().getClientName())
						.clientSecret(Objects.isNull(clientDTORequest.getClient().getClientSecret()) ? null : passwordEncoder.encode(clientDTORequest.getClient().getClientSecret()));

		try {

			List<String> clientAuthenticationMethods = clientDTORequest.getClient().getClientAuthenticationMethods();
			if (!CollectionUtils.isEmpty(clientAuthenticationMethods)) {
				registerClientBuilder.clientAuthenticationMethods(clientAuthenticationMethodsConsumer -> clientAuthenticationMethods.forEach(
						clientAuthenticationMethod -> clientAuthenticationMethodsConsumer.add(OauthResolver.resolveClientAuthenticationMethod(clientAuthenticationMethod))
				));
			}


			List<String> authorizationGrantTypes = clientDTORequest.getClient().getAuthorizationGrantTypes();
			registerClientBuilder.authorizationGrantTypes(authorizationGrantTypesConsumer -> authorizationGrantTypes.forEach(
					authorizationGrantType -> authorizationGrantTypesConsumer.add(OauthResolver.resolveAuthorizationGrantType(authorizationGrantType))
			));


			List<String> redirectUris = clientDTORequest.getClient().getRedirectUris();

			if (!CollectionUtils.isEmpty(redirectUris)) {
				registerClientBuilder.redirectUris(redirectUriConsumer -> redirectUriConsumer.addAll(redirectUris));
			} else {
				if (authorizationGrantTypes.contains(AuthorizationGrantType.AUTHORIZATION_CODE.getValue())) {
					throw new RedirectIsNotExistInAuthorizationCodeModeException("Error ...", "", "Redirect URIs must be provided for clients that use authorization code grant");
				}
			}

			TokenSettings tokenSettings = getTokenSettings(clientDTORequest.getClient());
			ClientSettings clientSettings = getClientSettings(clientDTORequest.getClient());

			registerClientBuilder.tokenSettings(tokenSettings);
			registerClientBuilder.clientSettings(clientSettings);

			jpaRegisteredClientRepository.save(registerClientBuilder.build());

			return GenericResponseSuccessWrapper.builder()
					.data(clientDTORequest.getClient())
					.build();

		} catch (Exception e) {
			throw e;
		}
	}










	private ClientSettings getClientSettings(ClientDTO clientDto) {
		ClientDTO.ClientSetting clientSettings = clientDto.getClientSettings();
		if (Objects.isNull(clientSettings)) {
			return ClientSettings.builder()
					.build();
		}
		return ClientSettings.builder()
				.requireProofKey(clientSettings.getRequireProofKey())
				.requireAuthorizationConsent(clientSettings.getRequireAuthorizationConsent())
				.jwkSetUrl(clientSettings.getJwkSetUrl())
//				.tokenEndpointAuthenticationSigningAlgorithm()
				.build();
	}

	private TokenSettings getTokenSettings(ClientDTO clientDto) {
		ClientDTO.TokenSetting tokenSettings = clientDto.getTokenSettings();

		if (Objects.isNull(tokenSettings)) {
			return TokenSettings.builder()
					.build();
		}
		return TokenSettings.builder()
				.authorizationCodeTimeToLive(Duration.ofMinutes(tokenSettings.getAuthorizationCodeTimeToLive()))
				.accessTokenTimeToLive(Duration.ofMinutes(tokenSettings.getAccessTokenTimeToLive()))
				.refreshTokenTimeToLive(Duration.ofMinutes(tokenSettings.getRefreshTokenTimeToLive()))
				.accessTokenFormat(new OAuth2TokenFormat(tokenSettings.getAccessTokenFormat()))
				.deviceCodeTimeToLive(Duration.ofMinutes(tokenSettings.getDeviceCodeTimeToLive()))
				.reuseRefreshTokens(tokenSettings.getReuseRefreshTokens())
				.idTokenSignatureAlgorithm(SignatureAlgorithm.valueOf(tokenSettings.getIdTokenSignatureAlgorithm()))
				.build();
	}
}
