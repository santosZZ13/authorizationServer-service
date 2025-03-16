//package org.authorizationserver.mapper;
//
//import org.authorizationserver.model.CustomOidcUser;
//import org.authorizationserver.persistent.entity.UserEntity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.core.oidc.OidcIdToken;
//import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
//import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
//import org.springframework.security.oauth2.core.oidc.user.OidcUser;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component("google")
//public class GoogleOidcUserMapper implements OidcUserMapper {
//	@Override
//	public OidcUser map(OidcUser oidcUser) {
//		CustomOidcUser user = new CustomOidcUser(oidcUser.getIdToken(), oidcUser.getUserInfo());
//		user.setUsername(oidcUser.getEmail());
//		//Enable new users by default
//		user.setActive(true);
//		return user;
//	}
//
//	@Override
//	public OidcUser map(OidcIdToken idToken, OidcUserInfo userInfo, UserEntity userEntity) {
//		Set<GrantedAuthority> authorities = userEntity.getRoles().stream()
//				.flatMap(role -> role.getAuthorities().stream()
//						.map(authority -> new SimpleGrantedAuthority(authority.getName()))
//				)
//				.collect(Collectors.toSet());
//
//
//		Map<String, Object> claims = new HashMap<>(idToken.getClaims());
//		claims.put(StandardClaimNames.GIVEN_NAME, userEntity.getFirstName());
////		claims.put(StandardClaimNames.MIDDLE_NAME, user.getMiddleName());
//		claims.put(StandardClaimNames.FAMILY_NAME, userEntity.getLastName());
//		claims.put(StandardClaimNames.LOCALE, userEntity.getLocale());
//		claims.put(StandardClaimNames.PICTURE, userEntity.getAvatarUrl());
//		claims.put(StandardClaimNames.EMAIL, userEntity.getEmail());
//
//
//		OidcIdToken customIdToken = new OidcIdToken(
//				idToken.getTokenValue(), idToken.getIssuedAt(), idToken.getExpiresAt(), claims
//		);
//
//		CustomOidcUser oidcUser = new CustomOidcUser(authorities, customIdToken, userInfo);
//		oidcUser.setId(userEntity.getId());
//		oidcUser.setUsername(userEntity.getEmail());
//		oidcUser.setCreatedAt(userEntity.getCreatedDate());
//		oidcUser.setActive(userEntity.isEnable());
//		return oidcUser;
//	}
//}
