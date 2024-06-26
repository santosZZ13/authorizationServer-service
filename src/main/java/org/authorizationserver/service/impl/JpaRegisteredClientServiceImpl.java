package org.authorizationserver.service.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.configuration.security.repository.JpaRegisteredClientRepository;
import org.authorizationserver.dto.clientdto.ClientDTORequest;
import org.authorizationserver.dto.clientdto.ClientDto;
import org.authorizationserver.exception.GenericResponseSuccessWrapper;
import org.authorizationserver.service.JpaRegisteredClientService;
import org.authorizationserver.util.OauthResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class JpaRegisteredClientServiceImpl implements JpaRegisteredClientService {

	private final JpaRegisteredClientRepository jpaRegisteredClientRepository;
	private final PasswordEncoder passwordEncoder;



	@Override
	@Transactional
	public GenericResponseSuccessWrapper createClient(ClientDTORequest clientDTORequest) {
		GenericResponseSuccessWrapper genericResponseSuccessWrapper = new GenericResponseSuccessWrapper();

		try {
			Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(clientDTORequest.getClient().getClientAuthenticationMethods());
			Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(clientDTORequest.getClient().getAuthorizationGrantTypes());


			Set<ClientAuthenticationMethod>  clientAuthenticationMethodsConsumer = new HashSet<>();
			clientAuthenticationMethods.forEach(
					clientAuthenticationMethod -> clientAuthenticationMethodsConsumer.add(OauthResolver.resolveClientAuthenticationMethod(clientAuthenticationMethod))
			);

			Set<AuthorizationGrantType> authorizationGrantTypesConsumer = new HashSet<>();
			authorizationGrantTypes.forEach(
					authorizationGrantType -> authorizationGrantTypesConsumer.add(OauthResolver.resolveAuthorizationGrantType(authorizationGrantType))
			);

			Set<String> redirectUris = StringUtils.commaDelimitedListToSet(clientDTORequest.getClient().getRedirectUris());

			if (authorizationGrantTypesConsumer.contains(AuthorizationGrantType.AUTHORIZATION_CODE)
					&& CollectionUtils.isEmpty(redirectUris)) {
//			throw new IllegalArgumentException("Redirect URIs must be provided for clients that use authorization code grant");
			}

			TokenSettings tokenSettings = getTokenSettings(clientDTORequest.getClient());
			ClientSettings clientSettings = getClientSettings(clientDTORequest.getClient());
			String registerId = UUID.randomUUID().toString();
			RegisteredClient.Builder registerClientBuilder = RegisteredClient
					.withId(registerId)
					.clientId(clientDTORequest.getClient().getClientId())
					.clientName(Objects.isNull(clientDTORequest.getClient().getClientName()) ? registerId : clientDTORequest.getClient().getClientName())
					.clientSecret(Objects.isNull(clientDTORequest.getClient().getClientSecret()) ? null : passwordEncoder.encode(clientDTORequest.getClient().getClientSecret()))
					.clientIdIssuedAt(clientDTORequest.getClient().getClientIdIssuedAt())
					.clientSecretExpiresAt(clientDTORequest.getClient().getClientSecretExpiresAt())
					.redirectUris((uris) -> uris.addAll(StringUtils.commaDelimitedListToSet(clientDTORequest.getClient().getRedirectUris())))
					.postLogoutRedirectUris((uris) -> uris.addAll(StringUtils.commaDelimitedListToSet(clientDTORequest.getClient().getPostLogoutRedirectUris())))
					.scopes((scopesConsumer) -> scopesConsumer.addAll(StringUtils.commaDelimitedListToSet(clientDTORequest.getClient().getScopes())))
					.authorizationGrantTypes(authorizationGrantTypeConsumer-> authorizationGrantTypes.forEach(
							authorizationGrantType -> authorizationGrantTypeConsumer.add(OauthResolver.resolveAuthorizationGrantType(authorizationGrantType))
					))
//				.clientAuthenticationMethod(clientAuthenticationMethodsConsumer)
					.tokenSettings(tokenSettings)
					.clientSettings(clientSettings);

			jpaRegisteredClientRepository.save(registerClientBuilder.build());



			return genericResponseSuccessWrapper;

		} catch (Exception e) {
			genericResponseSuccessWrapper.setSuccess(Boolean.FALSE);
			return genericResponseSuccessWrapper;
		}
	}

	private ClientSettings getClientSettings(ClientDto clientDto) {
		ClientDto.ClientSetting clientSettings = clientDto.getClientSettings();
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

	private TokenSettings getTokenSettings(ClientDto clientDto) {
		ClientDto.TokenSetting tokenSettings = clientDto.getTokenSettings();

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
