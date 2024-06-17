package org.authorizationserver.service.impl;

import lombok.AllArgsConstructor;
import org.authorizationserver.configuration.security.repository.JpaRegisteredClientRepository;
import org.authorizationserver.dto.ClientDto;
import org.authorizationserver.service.JpaRegisteredClientService;
import org.authorizationserver.util.OauthResolver;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Set;

@Service
@AllArgsConstructor
public class JpaRegisteredClientServiceImpl implements JpaRegisteredClientService {

	private final JpaRegisteredClientRepository jpaRegisteredClientRepository;




	@Override
	public String createClient(ClientDto clientDto) {
//		private ClientSettings clientSettings;
//		private TokenSettings tokenSettings;
		Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(clientDto.getClientAuthenticationMethods());
		Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(clientDto.getAuthorizationGrantTypes());
		RegisteredClient registeredClient = RegisteredClient
				.withId(java.util.UUID.randomUUID().toString())
				.clientAuthenticationMethods(clientAuthenticationMethodsConsumer -> clientAuthenticationMethods.forEach(
						clientAuthenticationMethod -> clientAuthenticationMethodsConsumer.add(OauthResolver.resolveClientAuthenticationMethod(clientAuthenticationMethod))
				))
				.authorizationGrantTypes(authorizationGrantTypesConsumer -> authorizationGrantTypes.forEach(
						authorizationGrantType -> authorizationGrantTypesConsumer.add(OauthResolver.resolveAuthorizationGrantType(authorizationGrantType))
				))
				.clientName(clientDto.getClientName().strip().toLowerCase())
				.clientId(clientDto.getClientId().strip().toLowerCase())
				.clientSecret(clientDto.getClientSecret().strip().toLowerCase())
				.clientIdIssuedAt(clientDto.getClientIdIssuedAt())
				.clientSecretExpiresAt(clientDto.getClientSecretExpiresAt())
				.redirectUris((uris) -> uris.addAll(StringUtils.commaDelimitedListToSet(clientDto.getRedirectUris())))
				.postLogoutRedirectUris((uris) -> uris.addAll(StringUtils.commaDelimitedListToSet(clientDto.getPostLogoutRedirectUris())))
				.scopes((scopesConsumer) -> scopesConsumer.addAll(StringUtils.commaDelimitedListToSet(clientDto.getScopes())))
				.build();


		try {
			jpaRegisteredClientRepository.save(registeredClient);
			return "OK";
		} catch (Exception e) {
			return "ERROR";
		}
	}
}
