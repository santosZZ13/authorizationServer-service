package org.authorizationserver.security.mapper;

import org.authorizationserver.persistent.entity.UserEntity;
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
		customOidcUser.setActive(Boolean.TRUE);
		return customOidcUser;
	}


	@Override
	public OidcUser map(OidcIdToken idToken, OidcUserInfo userInfo, UserEntity userEntity) {
		Set<GrantedAuthority> authorities = userEntity.getAuthorities();
		Map<String, Object> claims = getClaims(idToken, userEntity);
		OidcIdToken customIdToken = new OidcIdToken(idToken.getTokenValue(), idToken.getIssuedAt(), idToken.getExpiresAt(), claims);
		return getCustomOidcUser(authorities, customIdToken, userInfo, userEntity);
	}

	private Map<String, Object> getClaims(OidcIdToken idToken, UserEntity userEntity) {
		Map<String, Object> claims = new HashMap<>();
		claims.putAll(idToken.getClaims());
		claims.put(StandardClaimNames.GIVEN_NAME, userEntity.getFirstName());
		claims.put(StandardClaimNames.FAMILY_NAME, userEntity.getLastName());
		claims.put(StandardClaimNames.LOCALE, userEntity.getLocale());
		claims.put(StandardClaimNames.PICTURE, userEntity.getAvatarUrl());
		return claims;
	}

	private CustomOidcUser getCustomOidcUser(Set<GrantedAuthority> authorities,
											 OidcIdToken idToken,
											 OidcUserInfo userInfo,
											 UserEntity userEntity
	) {
		CustomOidcUser customOidcUser = new CustomOidcUser(authorities, idToken, userInfo);
		customOidcUser.setId(userEntity.getId());
		customOidcUser.setUsername(userEntity.getEmail());
		customOidcUser.setCreatedAt(userEntity.getCreatedAt());
		customOidcUser.setProvider(userEntity.getProvider());
		customOidcUser.setActive(userEntity.isActive());
		return customOidcUser;
	}
}
