package org.authorizationserver.security.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.authorizationserver.enums.Provider;
import org.authorizationserver.persistent.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Getter
@Setter
public class CustomOidcUser extends DefaultOidcUser implements UserDetails {
	private UUID id;
	private String username;
	private boolean active;
	private Provider provider;
	private LocalDateTime createdAt;
	private Collection<? extends GrantedAuthority> authorities = new HashSet<>();

	public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
						  OidcIdToken idToken) {
		super(authorities, idToken, null, IdTokenClaimNames.SUB);
	}

	public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
						  OidcIdToken idToken,
						  String nameAttributeKey) {
		super(authorities, idToken, null, nameAttributeKey);
	}

	public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
						  OidcIdToken idToken,
						  OidcUserInfo userInfo) {
		this(authorities, idToken, userInfo, IdTokenClaimNames.SUB);
	}

	public CustomOidcUser(Collection<? extends GrantedAuthority> authorities,
						  OidcIdToken idToken,
						  OidcUserInfo userInfo,
						  String nameAttributeKey) {
		super(AuthorityUtils.NO_AUTHORITIES, idToken, userInfo, nameAttributeKey);
		/**
		 * Keep the authorities mutable
		 */
		if (authorities != null) {
			this.authorities = authorities;
		}
	}

	public CustomOidcUser(OidcIdToken idToken, OidcUserInfo userInfo) {
		super(AuthorityUtils.NO_AUTHORITIES, idToken, userInfo);
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	public UserEntity toInstantUserEntity() {
		return UserEntity.builder()
				.id(getId())
				.email(getEmail())
				.firstName(getGivenName())
				.lastName(getFamilyName())
				.avatarUrl(getPicture())
				.locale(getLocale())
				.active(isActive())
				.provider(getProvider())
//				.providerId(getId())
				.emailVerified(getEmailVerified())
//				.roleModels(new HashSet<>())
				.build();
	}
}


