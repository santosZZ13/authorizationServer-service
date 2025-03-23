package org.authorizationserver.configuration.security.service;

import lombok.RequiredArgsConstructor;
import org.authorizationserver.dao.UserDaoRepository;
import org.authorizationserver.model.CustomOidcUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class UserServiceOAuth2UserHandler implements Consumer<OidcUser> {

	private final UserDaoRepository userDaoRepository;

	@Override
	public void accept(OidcUser user) {
		// Capture user in a local data store on first authentication
		CustomOidcUser oidcUser = (CustomOidcUser) user;
		if (oidcUser.getId() == null && this.userDaoRepository.findByEmail(user.getName()) == null) {
//			Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>) oidcUser.getAuthorities();
//			User localUser = oidcUser.toInstantUser();
//			Role defaultRole = roleService.getDefaultRole();
//
//			if (defaultRole != null) {
//				localUser.setRoles(Set.of(defaultRole));
//			}
//
//			this.userService.save(localUser);
//
//			if (!CollectionUtils.isEmpty(localUser.getRoles())) {
//				Set<? extends GrantedAuthority> authorities = localUser.getRoles().stream()
//						.flatMap(role -> role.getAuthorities().stream()
//								.map(authority -> new SimpleGrantedAuthority(authority.getName()))
//						)
//						.collect(Collectors.toSet());
//
//				grantedAuthorities.addAll(authorities);
//			}
//
//			oidcUser.setId(localUser.getId());
		}
	}

	@Override
	public @NotNull Consumer<OidcUser> andThen(@NotNull Consumer<? super OidcUser> after) {
		return Consumer.super.andThen(after);
	}
}
