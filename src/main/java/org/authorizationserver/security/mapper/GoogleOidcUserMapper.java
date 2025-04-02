package org.authorizationserver.security.mapper;

import org.authorizationserver.security.model.CustomOidcUser;
import org.authorizationserver.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component("google")
public class GoogleOidcUserMapper implements OidcUserMapper {

	@Override
	public OidcUser map(OidcUser oidcUser) {
		CustomOidcUser customOidcUser = new CustomOidcUser(oidcUser.getIdToken(), oidcUser.getUserInfo());
		customOidcUser.setUsername(oidcUser.getEmail());
		customOidcUser.setActive(true);
		return customOidcUser;
	}

	/**
	 * Maps the OidcUser to a CustomOidcUser with additional claims and authorities.
	 *
	 * @param idToken
	 * @param userInfo
	 * @param userModel
	 * @return
	 */
	@Override
	public OidcUser map(OidcIdToken idToken, OidcUserInfo userInfo, UserModel userModel) {
		Set<GrantedAuthority> authorities = userModel
				.getRoles()
				.stream()
				.flatMap(role -> role.getAuthorities()
						.stream().map(authorityModel -> new SimpleGrantedAuthority(authorityModel.getName()))
				)
				.collect(Collectors.toSet());

		Map<String, Object> claims = new HashMap<>();
		claims.putAll(idToken.getClaims());
		claims.put(StandardClaimNames.GIVEN_NAME, userModel.getFirstName());
		claims.put(StandardClaimNames.FAMILY_NAME, userModel.getLastName());
		claims.put(StandardClaimNames.LOCALE, userModel.getLocale());
		claims.put(StandardClaimNames.PICTURE, userModel.getAvatarUrl());

		OidcIdToken customIdToken = new OidcIdToken(
				idToken.getTokenValue(), idToken.getIssuedAt(), idToken.getExpiresAt(), claims
		);

		CustomOidcUser oidcUser = new CustomOidcUser(authorities, customIdToken, userInfo);
		oidcUser.setId(userModel.getId());
		oidcUser.setUsername(userModel.getEmail());
//		oidcUser.setCreatedAt(userModel.getCreatedAt());
//		oidcUser.setActive(userModel.get());
		return oidcUser;
	}
}
