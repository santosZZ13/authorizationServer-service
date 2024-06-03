package org.authorizationserver.security.handler;

import lombok.RequiredArgsConstructor;
import org.authorizationserver.model.CustomOidcUser;
import org.authorizationserver.persistent.entity.Role;
import org.authorizationserver.persistent.entity.User;
import org.authorizationserver.service.RoleService;
import org.authorizationserver.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class UserServiceOAuth2UserHandler implements Consumer<OidcUser> {

	private final UserService userService;
	private final RoleService roleService;


	@Override
	public void accept(OidcUser user) {

		// Capture user in a local data store on first authentication
		CustomOidcUser oidcUser = (CustomOidcUser) user;

		if (Objects.isNull(oidcUser.getId()) && Objects.isNull(this.userService.getUserByEmail(user.getName()))) {
			Collection<GrantedAuthority> grantedAuthorities = (Collection<GrantedAuthority>)oidcUser.getAuthorities();
			User localUser = oidcUser.toInstantUser();
			Role defaultRole = roleService.getDefaultRole();

			if (defaultRole != null) {
				localUser.setRoles(Set.of(defaultRole));
			}

			this.userService.save(localUser);
		}
	}

	@Override
	public Consumer<OidcUser> andThen(Consumer<? super OidcUser> after) {
		return Consumer.super.andThen(after);
	}
}
