package org.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * DTO for {@link org.authorizationserver.persistent.entity.Client}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto implements Serializable {
	private String clientName;
	private String clientId;
	private String clientSecret;
	private Instant clientIdIssuedAt;
	private Instant clientSecretExpiresAt;
	/**
	 * <p>client_secret_basic</p>
	 * <p>client_secret_post</p>
	 * <p>client_secret_jwt</p>
	 * <p>private_key_jwt</p>
	 * <p>none</p>
	 * Default:
	 */
	private String clientAuthenticationMethods;
	/**
	 * <p>authorization_code</p>
	 * <p>refresh_token</p>
	 * <p>client_credentials</p>
	 * <p>password</p>
	 * <p>urn:ietf:params:oauth:grant-type:jwt-bearer</p>
	 * <p>urn:ietf:params:oauth:grant-type:device_code</p>
	 * Default:
	 */
	private String authorizationGrantTypes;
	private String redirectUris;
	private String postLogoutRedirectUris;
	private String scopes;
	private ClientSetting clientSettings;
	private TokenSetting tokenSettings;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ClientSetting {
		@JsonProperty("require-proof-key")
		private String requireProofKey;
		@JsonProperty("require-authorization-consent")
		private String requireAuthorizationConsent;
		@JsonProperty("jwk-set-url")
		private String jwkSetUrl;
		@JsonProperty("token-endpoint-authentication-signing-algorithm")
		private String tokenEndpointAuthenticationSigningAlgorithm;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TokenSetting {
		@JsonProperty("authorization-code-time-to-live")
		private String authorizationCodeTimeToLive;
		@JsonProperty("access-token-time-to-live")
		private String accessTokenTimeToLive;
		@JsonProperty("access-token-format")
		private String accessTokenFormat;
		@JsonProperty("device-code-time-to-live")
		private String deviceCodeTimeToLive;
		@JsonProperty("reuse-refresh-tokens")
		private String reuseRefreshTokens;
		@JsonProperty("refresh-token-time-to-live")
		private String refreshTokenTimeToLive;
		@JsonProperty("id-token-signature-algorithm")
		private String idTokenSignatureAlgorithm;
	}
}