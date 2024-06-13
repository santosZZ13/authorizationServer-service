package org.authorizationserver.configuration.security.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.authorizationserver.model.AuthorizationGrantTypePassword;
import org.authorizationserver.persistent.entity.Client;
import org.authorizationserver.persistent.repository.ClientRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@AllArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

	private final ClientRepository clientRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void save(RegisteredClient registeredClient) {
		Assert.notNull(registeredClient, "registeredClient cannot be null");
		this.clientRepository.save(toClientEntity(registeredClient));
	}

	private Client toClientEntity(RegisteredClient registeredClient) {
		List<String> clientAuthenticationMethods = new ArrayList<>(registeredClient.getClientAuthenticationMethods().size());
		registeredClient.getClientAuthenticationMethods()
				.forEach(clientAuthenticationMethod -> clientAuthenticationMethods.add(clientAuthenticationMethod.getValue()));
		List<String> authorizationGrantTypes = new ArrayList<>(registeredClient.getAuthorizationGrantTypes().size());
		registeredClient.getAuthorizationGrantTypes().forEach(authorizationGrantType ->
				authorizationGrantTypes.add(authorizationGrantType.getValue()));
		Client client = new Client();
		client.setId(registeredClient.getId());
		client.setClientId(registeredClient.getClientId());
		client.setClientIdIssuedAt(registeredClient.getClientIdIssuedAt());
		client.setClientName(registeredClient.getClientName());
		client.setClientSecret(registeredClient.getClientSecret());
		client.setClientSecretExpiresAt(registeredClient.getClientSecretExpiresAt());
		client.setClientAuthenticationMethods(StringUtils.collectionToCommaDelimitedString(clientAuthenticationMethods));
		client.setAuthorizationGrantTypes(StringUtils.collectionToCommaDelimitedString(authorizationGrantTypes));
		client.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
		client.setPostLogoutRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getPostLogoutRedirectUris()));
		client.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
		client.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
		client.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));
		return client;
	}


	@Override
	public RegisteredClient findById(String id) {
		return null;
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		Assert.hasText(clientId, "clientId cannot be empty");
		return this.clientRepository.findByClientId(clientId)
				.map(this::toRegisterClient)
				.orElse(null);
	}

	public RegisteredClient toRegisterClient(@NotNull Client client) {
		Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(client.getClientAuthenticationMethods());
		Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(client.getAuthorizationGrantTypes());
		Set<String> redirectUris = StringUtils.commaDelimitedListToSet(client.getRedirectUris());
		Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(client.getPostLogoutRedirectUris());
		Set<String> clientScopes = StringUtils.commaDelimitedListToSet(client.getScopes());

		Map<String, Object> clientSettingsMap = client.getClientSettings().equals("null") ? ClientSettings.builder().build().getSettings() : parseMap(client.getClientSettings());
		Map<String, Object> tokenSettingsMap = client.getTokenSettings().equals("null") ? TokenSettings.builder().build().getSettings() : parseMap(client.getTokenSettings());


		return RegisteredClient.withId(client.getId())
				.clientName(client.getClientName())
				.clientId(client.getClientId())
				.clientSecret(client.getClientSecret())
				.clientIdIssuedAt(client.getClientIdIssuedAt())
				.clientSecretExpiresAt(client.getClientSecretExpiresAt())
				.clientAuthenticationMethods(authenticationMethods ->
						clientAuthenticationMethods.forEach(authenticationMethod ->
								authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
				.authorizationGrantTypes(grantTypes ->
						authorizationGrantTypes.forEach(authorizationGrantType -> grantTypes.add(resolveAuthorizationGrantType(authorizationGrantType))))
				.redirectUris((uris) -> uris.addAll(redirectUris))
				.postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
				.scopes((scopes) -> scopes.addAll(clientScopes))
				.clientSettings(ClientSettings.withSettings(clientSettingsMap).build())
				.tokenSettings(TokenSettings.withSettings(tokenSettingsMap).build())
				.build();
	}

	private AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
		if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		} else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.CLIENT_CREDENTIALS;
		} else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.REFRESH_TOKEN;
		} else if (AuthorizationGrantTypePassword.GRANT_PASSWORD.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantTypePassword.GRANT_PASSWORD;
		}
		return new AuthorizationGrantType(authorizationGrantType);
	}

	private ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
		if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
		} else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_POST;
		} else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.NONE;
		}
		return new ClientAuthenticationMethod(clientAuthenticationMethod);
	}


	private Map<String, Object> parseMap(String data) {
		try {
			return this.objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private String writeMap(Map<String, Object> data) {
		try {
			return this.objectMapper.writeValueAsString(data);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

}
