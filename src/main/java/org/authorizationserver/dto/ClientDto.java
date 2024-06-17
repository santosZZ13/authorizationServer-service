package org.authorizationserver.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * DTO for {@link org.authorizationserver.persistent.entity.Client}
 */
@Data
@Builder
public class ClientDto implements Serializable {
	String clientName;
	String clientId;
	String clientSecret;
	Instant clientIdIssuedAt;
	Instant clientSecretExpiresAt;
	String clientAuthenticationMethods;
	String authorizationGrantTypes;
	String redirectUris;
	String postLogoutRedirectUris;
	String scopes;
	Map<String, Object> clientSettings;
	Map<String, Object> tokenSettings;
}