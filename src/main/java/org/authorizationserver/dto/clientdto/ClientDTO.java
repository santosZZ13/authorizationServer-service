package org.authorizationserver.dto.clientdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.authorizationserver.validation.ValidAuthorizationCodeTimeToLive;
import org.authorizationserver.validation.ValidField;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * DTO for {@link org.authorizationserver.persistent.entity.Client}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO implements Serializable {

	@Nullable
	private String clientName;

	@ValidField(message = "Mandatory field {%s} is not specified", code = "C1010003")
	private String clientId;

	private String clientSecret;

	@Nullable
	private Instant clientIdIssuedAt;

	@Nullable
	private Instant clientSecretExpiresAt;
	/**
	 * <p>client_secret_basic</p>
	 * <p>client_secret_post</p>
	 * <p>client_secret_jwt</p>
	 * <p>private_key_jwt</p>
	 * <p>none</p>
	 * Default:
	 */
	@Nullable
	private List<String> clientAuthenticationMethods;
	/**
	 * <p>authorization_code</p>
	 * <p>refresh_token</p>
	 * <p>client_credentials</p>
	 * <p>password</p>
	 * <p>urn:ietf:params:oauth:grant-type:jwt-bearer</p>
	 * <p>urn:ietf:params:oauth:grant-type:device_code</p>
	 * Default:
	 */
	@ValidField(message = "Mandatory field {%s} is not specified", code = "C1010003")
	private List<String> authorizationGrantTypes;

	@Nullable
	private List<String> redirectUris;

	@Nullable
	private List<String> postLogoutRedirectUris;

	@Nullable
	private List<String> scopes;

	@Nullable
	private ClientSetting clientSettings;
	@Nullable
	private TokenSetting tokenSettings;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ClientSetting {
		@JsonProperty("require-proof-key")
		private Boolean requireProofKey;
		@JsonProperty("require-authorization-consent")
		private Boolean requireAuthorizationConsent;
		@JsonProperty("jwk-set-url")
		private String jwkSetUrl;
		@JsonProperty("token-endpoint-authentication-signing-algorithm")
		private String tokenEndpointAuthenticationSigningAlgorithm;
	}

	//				.authorizationCodeTimeToLive(Duration.ofMinutes(5))
//			.accessTokenTimeToLive(Duration.ofMinutes(5))
//			.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
//				.deviceCodeTimeToLive(Duration.ofMinutes(5))
//			.reuseRefreshTokens(true)
//				.refreshTokenTimeToLive(Duration.ofMinutes(60))
//			.idTokenSignatureAlgorithm(SignatureAlgorithm.RS256);
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TokenSetting {
		@JsonProperty("authorization-code-time-to-live")
		@ValidAuthorizationCodeTimeToLive
		private Integer authorizationCodeTimeToLive;
		@JsonProperty("access-token-time-to-live")
		private Integer accessTokenTimeToLive;
		/**
		 * <p>self-contained</p>
		 * <p>reference</p>
		 */
		@JsonProperty("access-token-format")
		private String accessTokenFormat;
		@JsonProperty("device-code-time-to-live")
		private Integer deviceCodeTimeToLive;
		@JsonProperty("reuse-refresh-tokens")
		private Boolean reuseRefreshTokens;
		@JsonProperty("refresh-token-time-to-live")
		private Integer refreshTokenTimeToLive;
		/**
		 * <p>RS256</p>
		 * <p>RS384</p>
		 * <p>RS512</p>
		 * <p>ES256</p>
		 * <p>ES384</p>
		 * <p>ES512</p>
		 * <p>PS256</p>
		 * <p>PS384</p>
		 * <p>PS512</p>
		 */
		@JsonProperty("id-token-signature-algorithm")
		private String idTokenSignatureAlgorithm;
	}
}